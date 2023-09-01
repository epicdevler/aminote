package com.epicdevler.ami.minote.ui.screens.tag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.repositories.TagsRepo
import com.epicdevler.ami.minote.data.utils.Result
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText
import kotlinx.coroutines.launch

class TagVM : ViewModel() {

    private val tagsRepo = TagsRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set


    fun loadTagNotes(
        tagId: String
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.ResString(R.string.loading_msg, "Notes")
            )
            launch { tagsRepo.getNotes(tagId = tagId) }
            tagsRepo.tagNotes.collect { noteResult ->
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
                        val tagNote = noteResult.data!!
                        val tagNotes = tagNote.notes
                        uiState = when {
                            tagNotes.isEmpty() -> {
                                uiState.copy(
                                    state = State.Error(
                                        reason = State.Error.Reason.EmptyData
                                    ),
                                    tagNotes = tagNote,
                                    message = UiText.ResString(R.string.note_create)
                                )
                            }

                            else -> {
                                uiState.copy(
                                    state = State.Success(null),
                                    tagNotes = tagNote,
                                    message = UiText.ResString(if (tagNotes.size > 1) R.string.notes_list_success_message else R.string.note_list_success_message)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val state: State<Nothing?> = State.Idle(),
        val tagNotes: DummyNotesData.TagNotes? = null,
        val message: UiText = UiText.None,
    )


}
