package com.epicdevler.ami.minote.ui.screens.home.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.AppBar
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.screens.components.Note
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value

@Composable
fun NotesContent(
    onNavigate: (String) -> Unit = {}
) {
    val vm: NotesVM = viewModel()
    val uiState = vm.uiState
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            AppBar(
                actions = {
                    AnimatedVisibility(visible = uiState.state is State.Success) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "search"
                            )
                        }
                    }
                }
            )
            Column(
                Modifier
                    .padding(16.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = uiState.greeting,
                    style = typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = uiState.message.value(context),
                    style = typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedContent(targetState = uiState.state, label = "note_content") { state ->
                when (state) {
                    is State.Idle -> Unit
                    is State.Loading -> {
                        Loader(modifier = Modifier)
                    }

                    is State.Error -> {
                        when (state.reason) {
                            State.Error.Reason.EmptyData -> {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.empty
                                    )
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    LottieAnimation(
                                        modifier = Modifier.size(200.dp),
                                        composition = composition,
                                        iterations = Int.MAX_VALUE,
                                    )
                                    Text(
                                        text = "Start taking digital versions of your note, meetings, ideas and more.",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }
                            }

                            else -> {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.loader
                                    )
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    LottieAnimation(
                                        modifier = Modifier.size(200.dp),
                                        composition = composition,
                                        iterations = Int.MAX_VALUE,
                                    )
                                    Text(
                                        text = state.message.value(context),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                    Button(
                                        onClick = { /*TODO*/ },
                                        elevation = null,
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorScheme.secondary,
                                            contentColor = colorScheme.onSecondary
                                        )
                                    ) {
                                        Text(text = "Reload")
                                    }
                                }
                            }
                        }
                    }

                    is State.Success -> {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalItemSpacing = 10.dp,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            repeat(state.data.size) { itemsIndex ->
                                val note = state.data[itemsIndex]
                                item(
                                    key = note.id,
                                    span = calculateSpan(
                                        currentNoteId = note.id,
                                        lastNoteId = state.data.last().id,
                                        totalNoteSize = state.data.size
                                    )
                                ) {
                                    Note(
                                        note = note,
                                        onClick = { onNavigate("note/?noteId=${note.id}") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.state is State.Success || try {
                (uiState.state as State.Error).reason == State.Error.Reason.EmptyData
            } catch (e: Exception) {
                false
            },
            modifier = Modifier.padding(bottom = 50.dp, end = 20.dp)
        ) {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                onClick = { onNavigate("note/?noteId") },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "add")
            }
        }

    }

}

fun calculateSpan(currentNoteId: Int, lastNoteId: Int, totalNoteSize: Int): StaggeredGridItemSpan {
    val condition = totalNoteSize % 2 != 0 && lastNoteId == currentNoteId
    return if (false) StaggeredGridItemSpan.FullLine else StaggeredGridItemSpan.SingleLane
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewNoteContent() {
    NotesContent()
}


