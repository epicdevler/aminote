package com.epicdevler.ami.minote.ui.screens.home.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
fun NotesContent(
    noteUiState: NotesVM.UiState,
    onRequestLoadNote: (noteId: String?) -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val context = LocalContext.current

    FloatingActionBarScreen(
        fabVisible = noteUiState.state is State.Success || try {
            (noteUiState.state as State.Error).reason == State.Error.Reason.EmptyData
        } catch (_: Exception) {
            false
        },
        onFabClick = {
            onRequestLoadNote(null)
            onNavigate("note/?noteId")
        },
    ) {
        Column {
            AppBar(
                actionsVisible = true,
                actions = {
                    AnimatedVisibility(visible = noteUiState.state is State.Success && noteUiState.notes.size > 20) {
                        IconButton(onClick = { onNavigate("search/?tagId=${null}/${SearchReason.FindNote}") }) {
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
                    text = noteUiState.greeting.value(context),
                    style = typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = noteUiState.message.value(context),
                    style = typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedContent(targetState = noteUiState.state, label = "note_content") { state ->
                when (state) {
                    is State.Loading -> {
                        Loader(modifier = Modifier)
                    }

                    is State.Error -> {
                        when (state.reason) {
                            State.Error.Reason.EmptyData -> {
                                EmptyListState(message = "Start taking digital versions of your note, meetings, ideas and more.")
                            }

                            else -> {
                                ErrorState(message = state.message.value(context))
                            }
                        }
                    }

                    else -> LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalItemSpacing = 10.dp,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(
                            items = noteUiState.notes,
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
    }
}