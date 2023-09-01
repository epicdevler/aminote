package com.epicdevler.ami.minote.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.BackBtn
import com.epicdevler.ami.minote.ui.screens.components.IconTextInput
import com.epicdevler.ami.minote.ui.screens.search.SearchReason.FindNote
import com.epicdevler.ami.minote.ui.screens.search.SearchReason.FindNoteContent

enum class SearchReason {
    FindNote,
    FindNoteContent,
}

@Composable
fun SearchScreen(
    reason: SearchReason,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
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

        AnimatedContent(
            targetState = true,
            label = "search_content",
            modifier = Modifier.weight(1f)
        ) { isSuccess ->

            when (!isSuccess) {
                true -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search))
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

                false -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search_error))
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
            }

        }
    }
}


@Preview(name = "SearchScreen", showSystemUi = true, showBackground = true, fontScale = 1.15f)
@Composable
private fun PreviewSearchScreen() {
    SearchScreen(reason = FindNote)
}























