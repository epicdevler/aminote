package com.epicdevler.ami.minote.ui.screens.note

import android.util.Log
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

class SelectTagVM : ViewModel() {

    private val tagsRepo = TagsRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set


    private fun loadTags(noteId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.None
            )
            tagsRepo.checkIfInTag(noteId).collect { noteResult ->
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
                        val tags = noteResult.data!!
                        uiState = when {
                            tags.isEmpty() -> {
                                uiState.copy(
                                    state = State.Error(
                                        reason = State.Error.Reason.EmptyData
                                    ),
                                    message = UiText.ResString(R.string.tag_create)
                                )
                            }

                            else -> {
                                uiState.copy(
                                    state = State.Success(null),
                                    tags = tags,
                                    message = UiText.ResString(R.string.tags_list_success_message)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: Events) {
        viewModelScope.launch {
            when (event) {
                is Events.Load -> loadTags(event.id)
                is Events.Select -> {
                    val selectResult =
                        tagsRepo.addNoteToTag(tagId = event.tagId, noteId = event.noteId)

                    Log.e("TAG", "onEvent: D -> ${selectResult.data}")
                    Log.e("TAG", "onEvent: M -> ${selectResult.message}")

                    when (selectResult) {
                        is Result.Idle -> Unit
                        is Result.Failed -> {
                            uiState = uiState.copy(
                                state = State.Error(
                                    reason = State.Error.Reason.UnClassified,
                                    message = UiText.NetworkString(selectResult.message)
                                )
                            )
                        }

                        is Result.Success -> {
                            uiState = uiState.copy(state = State.Success(null))
                        }
                    }
                }
            }
        }
    }

    sealed interface Events {
        class Load(val id: String) : Events
        class Select(val tagId: String, val noteId: String) : Events
    }

    data class UiState(
        val state: State<Any?> = State.Idle(),
        val tags: List<DummyNotesData.TagContains> = emptyList(),
        val message: UiText = UiText.None,
    )


}
