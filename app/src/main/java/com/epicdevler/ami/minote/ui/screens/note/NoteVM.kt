package com.epicdevler.ami.minote.ui.screens.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.repositories.NotesRepo
import com.epicdevler.ami.minote.data.utils.Result
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoteVM : ViewModel() {

    companion object {
        private const val TAG = "NOTE_VM"
    }

    private val notesRepo = NotesRepo

    private var hasRequested: Boolean = false

    var uiState by mutableStateOf(UiState())

    private var saveNoteJob: Job? = null
    private var noteJob: Job? = null

    private var isFirstSave by mutableStateOf(true)

    init {
        noteJob = viewModelScope.launch {
            notesRepo.note.collect { noteResult ->
                when (noteResult) {
                    is Result.Idle -> Unit
                    is Result.Failed -> {
                        uiState = uiState.copy(
                            state = State.Error(
                                message = UiText.NetworkString(message = noteResult.message),
                                reason = State.Error.Reason.UnClassified
                            )
                        )
                    }

                    is Result.Success -> {
                        uiState = uiState.copy(
                            state = State.Success(null),
                            note = noteResult.data!!
                        )
                    }
                }
            }
        }

    }

    fun onEvent(
        event: Event
    ) {
        viewModelScope.launch {
            when (event) {
                is Event.LoadNote -> {
                    isFirstSave = event.noteId == null
                    notesRepo.get(event.noteId)
                    Log.e(TAG, "onEvent ${event.javaClass}: ${event.noteId}")
                }

                is Event.Save -> {
                    val updateNoteRequest =
                        uiState.note!!.copy(title = event.title, content = event.content)
                    if (saveNoteJob?.isActive == true) saveNoteJob?.cancel()
                    saveNoteJob = launch {
                        when (val saveResult = notesRepo.save(updateNoteRequest)) {
                            is Result.Idle -> Unit
                            is Result.Failed -> {
                                uiState = UiState(
                                    state = State.Error(
                                        reason = State.Error.Reason.CouldNotSaveNote,
                                        message = UiText.NetworkString(message = saveResult.message)
                                    )
                                )
                            }

                            is Result.Success -> {
                                if (isFirstSave) {
                                    uiState.note?.id?.let {
                                        notesRepo.get(it)
                                    }
                                }
                                uiState = uiState.copy(state = State.Success(null))
                            }
                        }
                        cancel("Operation Finished")
                    }
                }
            }
        }
    }

    data class UiState(
        val state: State<Any?> = State.Idle(),
        val note: DummyNotesData.Note? = null
    )

    sealed interface Event {
        class LoadNote(val noteId: String? = null) : Event
        class Save(val noteId: String? = null, val title: String, val content: String) : Event
    }

    override fun onCleared() {

        super.onCleared()

        noteJob?.cancel()
        uiState = UiState()

    }

}
