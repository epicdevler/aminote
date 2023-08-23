package com.epicdevler.ami.minote.ui.screens.home.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.repositories.NotesRepo
import com.epicdevler.ami.minote.data.utils.Result
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText
import kotlinx.coroutines.launch

class NotesVM : ViewModel() {

    private val notesRepo = NotesRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.ResString(R.string.loading_msg, "Notes")
            )
            notesRepo.get()

            notesRepo.notes.collect { noteResult ->
                when (noteResult) {
                    is Result.Idle -> Unit

                    is Result.Failed -> {
                        uiState = uiState.copy(
                            state = State.Error(
                                reason = State.Error.Reason.UnClassified,
                                message = UiText.NetworkString(noteResult.message)
                            )
                        )
                    }

                    is Result.Success -> {
                        val notes = noteResult.data!!
                        uiState = when {
                            notes.isEmpty() -> {
                                uiState.copy(
                                    state = State.Error(
                                        reason = State.Error.Reason.EmptyData
                                    ),
                                    message = UiText.ResString(R.string.note_create)
                                )
                            }

                            else -> {
                                uiState.copy(
                                    state = State.Success(
                                        data = notes
                                    ),
                                    message = UiText.ResString(if (notes.size > 1) R.string.notes_list_success_message else R.string.note_list_success_message)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val greeting: String = "Hello, Good evening",
        val state: State<List<DummyNotesData.Note>> = State.Idle(),
        val message: UiText = UiText.None
    )


}
