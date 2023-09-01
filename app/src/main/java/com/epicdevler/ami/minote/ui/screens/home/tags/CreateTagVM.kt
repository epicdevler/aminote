package com.epicdevler.ami.minote.ui.screens.home.tags

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.data.repositories.TagsRepo
import com.epicdevler.ami.minote.data.utils.Result
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText
import kotlinx.coroutines.launch

class CreateTagVM : ViewModel() {

    private val tagsRepo = TagsRepo

    var uiState by mutableStateOf(UiState(state = State.Idle()))
        private set

    var _tempTitle: String? = null


    fun create(title: String) {
        viewModelScope.launch {
            if (_tempTitle != title){
                uiState = UiState(
                    state = State.Loading()
                )
                when {
                    title.isEmpty() -> {
                        uiState = UiState(
                            state = State.Error(
                                reason = State.Error.Reason.EmptyField, message = UiText.ResString(
                                    R.string.create_tag_error_empty_field
                                )
                            )
                        )
                        _tempTitle = title
                    }
                    title.trim().split(" ").size > 2 -> {
                        uiState = UiState(
                            state = State.Error(
                                reason = State.Error.Reason.EmptyField, message = UiText.ResString(
                                    R.string.create_tag_error_invalid_length
                                )
                            )
                        )
                        _tempTitle = title
                    }

                    else -> {
                        when (val createResult = tagsRepo.create(title)) {
                            is Result.Idle -> Unit
                            is Result.Failed -> {
                                uiState = UiState(
                                    state = State.Error(
                                        reason = State.Error.Reason.UnClassified,
                                        message = UiText.NetworkString(createResult.message)
                                    )
                                )
                                _tempTitle = title
                            }

                            is Result.Success -> {
                                uiState = UiState(
                                    state = State.Success(
                                        null,
                                        message = UiText.NetworkString("Successfully created $title")
                                    )
                                )
                                _tempTitle = null
                            }
                        }
                    }
                }
            }
        }
    }


    data class UiState(
        val state: State<Nothing?> = State.Idle()
    )


}
