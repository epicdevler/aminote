package com.epicdevler.ami.minote.ui.screens.search

import android.util.Log
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
import com.epicdevler.ami.minote.ui.utils.State.Error.Reason
import com.epicdevler.ami.minote.ui.utils.UiText
import kotlinx.coroutines.launch

class SearchVM : ViewModel() {

    companion object {
        private const val TAG = "SEARCH_VM"
    }

    private val notesRepo = NotesRepo
    private val notes: MutableList<DummyNotesData.Note> = mutableListOf()

    var uiState by mutableStateOf(UiState())

    init {

        viewModelScope.launch {

            when (val availableNotesResult = notesRepo.notes.value) {

                is Result.Idle -> {
                    Log.e(TAG, ": Idle ")
                    launch { notesRepo.get() }
                }

                is Result.Failed -> {
                    uiState = uiState.copy(
                        state = State.Error(
                            reason = Reason.UnClassified,
                            message = UiText.NetworkString(
                                message = availableNotesResult.message
                            )
                        )
                    )

                }

                is Result.Success -> {
                    notes.addAll(availableNotesResult.data!!)
                }
            }
        }

    }

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.Note -> {
                    uiState = uiState.copy(
                        state = State.Loading()
                    )
                    var filtered: List<DummyNotesData.Note> = listOf()

                    if (event.tagId != null) {
                        //                        RETURN ONLY NOTES IN A TAG BASE ON PROVIDED ID
                        filtered = notes.filter {
                            (it.tag.id == event.tagId)
                        }
                    }

                    filtered = filtered.ifEmpty { notes }
                        .filter {
                            it.title.contains(event.query, true) ||
                                    it.content.contains(event.query, true) ||
                                    it.tag.title.contains(event.query, true)
                        }

                    uiState = uiState.copy(
                        notesFound = filtered,
                        state = if (filtered.isNotEmpty()) State.Success(null) else State.Error(
                            reason = Reason.EmptyData, message = UiText.ResString(
                                R.string.search_error_not_found
                            )
                        )
                    )
                }

                SearchEvent.ResetUiState -> uiState = uiState.copy(state = State.Idle())
            }
        }
    }

    data class UiState(
        val notesFound: List<DummyNotesData.Note> = listOf(),
        val state: State<Nothing?> = State.Idle()
    )

    sealed interface SearchEvent {

        class Note(val tagId: String? = null, val query: String) : SearchEvent
        object ResetUiState : SearchEvent
    }

}