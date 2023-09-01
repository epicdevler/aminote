package com.epicdevler.ami.minote.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.BackBtn
import com.epicdevler.ami.minote.ui.screens.components.IconTextInput
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.screens.components.Note
import com.epicdevler.ami.minote.ui.screens.search.SearchReason.FindNote
import com.epicdevler.ami.minote.ui.screens.search.SearchReason.FindNoteContent
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

enum class SearchReason {
    FindNote,
    FindNoteContent,
}

@Composable
fun SearchScreen(
    reason: SearchReason,
    forTag: String?,
    onRequestLoadNote: (noteId: String?) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val vm: SearchVM = viewModel()
    val uiState = vm.uiState

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    LaunchedEffect(searchQuery.text) {

        val query = searchQuery.text
        if (query.isNotEmpty()) {
            delay(300.milliseconds)
            vm.onEvent(
                SearchVM.SearchEvent.Note(
                    tagId = forTag,
                    query = query
                )
            )
        } else {
            if (uiState.state is State.Error) {
                delay(300.milliseconds)
                vm.onEvent(SearchVM.SearchEvent.ResetUiState)
            }
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(end = 16.dp)
        ) {
            BackBtn(onClick = onNavigateUp)
            IconTextInput(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search"
                    )
                },
                value = searchQuery,
                onValueChange = { searchQuery = it },
                style = typography.bodyMedium,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {},
                placeHolder = when (reason) {
                    FindNote -> "Search by, tags, date, title, or note content"
                    FindNoteContent -> "Enter a word"
                },
                modifier = Modifier
                    .weight(1f)
                    .background(colorScheme.secondary.copy(0.1f), shape = CircleShape)
                    .border(
                        1.dp,
                        shape = CircleShape,
                        color = colorScheme.secondary.copy(.5f)
                    )
                    .padding(12.dp)
            )
        }
        AnimatedVisibility(visible = uiState.state is State.Loading) {
            Loader(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
                    .padding(bottom = 20.dp)
            )
        }

        AnimatedContent(
            targetState = uiState.state,
            label = "search_content",
        ) { state ->
            when (state) {
                is State.Error -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(
                                R.raw.search_error
                            )
                        )
                        LottieAnimation(
                            modifier = Modifier.size(100.dp),
                            composition = composition,
                            iterations = Int.MAX_VALUE,
                        )
                        Text(
                            text = state.message.value(context),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                        )
                    }
                }

                is State.Idle -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(
                                R.raw.search
                            )
                        )
                        LottieAnimation(
                            modifier = Modifier.size(100.dp),
                            composition = composition,
                            iterations = Int.MAX_VALUE,
                        )
                        Text(
                            text = stringResource(R.string.search_helper_info),
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium,
                        )
                    }
                }

                else -> Unit
            }
        }
        AnimatedVisibility(
            visible = uiState.state is State.Success,
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = uiState.notesFound,
                    key = {
                        it.id
                    }
                ) { note ->
                    Note(
                        note = note,
                        onClick = {
                            onRequestLoadNote(note.id)
                            onNavigate("note/?noteId=${note.id}")
                        }
                    )
                }
            }
        }
    }
}


@Preview(name = "SearchScreen", showSystemUi = true, showBackground = true, fontScale = 1.15f)
@Composable
private fun PreviewSearchScreen() {
    SearchScreen(reason = FindNote, forTag = null)
}























