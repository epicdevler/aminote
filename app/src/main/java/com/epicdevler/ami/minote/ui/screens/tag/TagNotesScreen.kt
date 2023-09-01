package com.epicdevler.ami.minote.ui.screens.tag

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.AppBar
import com.epicdevler.ami.minote.ui.screens.components.EmptyListState
import com.epicdevler.ami.minote.ui.screens.components.ErrorState
import com.epicdevler.ami.minote.ui.screens.components.FloatingActionBarScreen
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.screens.components.Note
import com.epicdevler.ami.minote.ui.screens.search.SearchReason
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value

@Composable
fun TagNotesScreen(
    tagId: String,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val vm: TagVM = viewModel()
    LaunchedEffect(Unit) {
        vm.loadTagNotes(tagId)
    }
    val uiState = vm.uiState
    val context = LocalContext.current
    var showFab by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState) {
        showFab = uiState.state is State.Success || try {
            (uiState.state as State.Error).reason == State.Error.Reason.EmptyData
        } catch (_: Exception) {
            false
        }
    }

    FloatingActionBarScreen(
        fabVisible = showFab,
        onFabClick = { onNavigate("note/?noteId") },
    ) {
        Column {
            AppBar(
                onBackPressed = onNavigateUp,
                actionsVisible = true,
                actions = {
                    AnimatedVisibility(visible = uiState.state is State.Success && uiState.tagNotes != null && uiState.tagNotes.notes.size > 20) {
                        IconButton(onClick = { onNavigate("search/?tagId=${tagId}/${SearchReason.FindNote}") }) {
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
                AnimatedVisibility(visible = showFab) {
                    Text(
                        text = try {
                            val numOfTags = uiState.tagNotes?.notes?.size ?: 0
                            "$numOfTags  Note${if (numOfTags > 1) "s" else ""}"

                        } catch (_: Exception) {
                            ""
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground.copy(.7f),
                        modifier = Modifier.fillMaxWidth()
                    )

                }
                Text(
                    text = "${uiState.tagNotes?.title}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedContent(targetState = uiState.state, label = "note_content") { state ->
                when (state) {
                    is State.Loading -> {
                        Loader(modifier = Modifier)
                    }

                    is State.Error -> {
                        when (state.reason) {
                            State.Error.Reason.EmptyData -> {
                                EmptyListState(message = stringResource(R.string.empty_notes_action_info))
                            }

                            else -> {
                                ErrorState(
                                    message = state.message.value(context)
                                )
                            }
                        }
                    }
                    else -> Unit

                }
            }
            AnimatedVisibility(visible = uiState.state is State.Success) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        key = { it.id },
                        items = uiState.tagNotes!!.notes
                    ) { note ->
                        Note(
                            modifier = Modifier.fillMaxSize(),
                            note = note,
                            onClick = { onNavigate("note/?noteId=${note.id}") }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewTagScreen() {
    TagNotesScreen(tagId = "0")
}
