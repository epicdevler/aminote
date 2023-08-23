package com.epicdevler.ami.minote.ui.screens.home.tags

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

class TagsVM : ViewModel() {

    private val tagsRepo = TagsRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set
    init {
        loadTags()
    }

    private fun loadTags() {
        viewModelScope.launch {
            uiState = uiState.copy(
                state = State.Loading(),
                message = UiText.ResString(R.string.loading_msg, "Tags")
            )
            tagsRepo.get()

            tagsRepo.tags.collect { noteResult ->
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
                        uiState = uiState.copy(
                            state = State.Success(
                                data = notes
                            ),
                            message = UiText.ResString(R.string.tags_list_success_message)
                        )
                    }
                }
            }
        }
    }

    data class UiState(
        val state: State<List<DummyNotesData.Tag>> = State.Idle(),
        val message: UiText = UiText.None
    )


}
