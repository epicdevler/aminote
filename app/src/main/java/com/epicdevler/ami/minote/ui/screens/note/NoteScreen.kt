package com.epicdevler.ami.minote.ui.screens.note

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.AppBar
import com.epicdevler.ami.minote.ui.screens.components.ErrorState
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.screens.components.TextField
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

sealed interface NoteChangeRequest {
    class Save(val title: String, val content: String) : NoteChangeRequest
    class AddTag(val title: String, val content: String) : NoteChangeRequest
}

@Composable
fun NoteScreen(
    uiState: NoteVM.UiState,
    onRequestChange: (NoteChangeRequest) -> Unit,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val titleFocusRequester = remember {
        FocusRequester.Default
    }
    val contentFocusRequester = remember {
        FocusRequester.Default
    }
    var titleTextField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var contentTextField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var tagTitle by rememberSaveable {
        mutableStateOf("")
    }
    var notifyOfChanges by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = uiState) {
        when (uiState.state) {
            is State.Idle -> Unit
            is State.Error -> Toast.makeText(
                context,
                uiState.state.message.value(context),
                Toast.LENGTH_SHORT
            ).show()

            is State.Loading -> {}
            is State.Success -> {
                launch {
                    if (titleTextField.text.isNotEmpty() && notifyOfChanges) Toast.makeText(
                        context,
                        "${titleTextField.text} Saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!notifyOfChanges) {
                    val note = uiState.note
                    note?.let {
                        tagTitle = it.tag.title
                    }
                    note?.title?.let { titleTextField = titleTextField.copy(text = it) }
                    note?.content?.let { contentTextField = contentTextField.copy(text = it) }

                }
            }

        }
    }

    LaunchedEffect(key1 = titleTextField.text, key2 = contentTextField.text) {
        launch {
            val noteDataChanged =
                titleTextField.text != uiState.note?.title || contentTextField.text != uiState.note.content
            if (noteDataChanged && titleTextField.text.isNotEmpty()) {
                delay(1.seconds)
                onRequestChange(
                    NoteChangeRequest.Save(
                        title = titleTextField.text,
                        content = contentTextField.text
                    )
                )
                if (!notifyOfChanges) {
                    notifyOfChanges = true
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBar(
            title = {},
            onBackPressed = onNavigateUp,
            actionsVisible = uiState.state is State.Success || uiState.state is State.Idle,
            actions = {

                AnimatedVisibility(tagTitle.isNotEmpty()) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorScheme.onBackground
                        ),
                        onClick = { onNavigate("selectTag/${uiState.note?.id ?: "0"}") }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tagTitle,
                                style = typography.labelLarge
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_tag),
                                contentDescription = "tag",
                                modifier = Modifier.rotate(30f)
                            )
                        }
                    }
                }
                AnimatedVisibility(tagTitle.isEmpty()) {
                    IconButton(onClick = { onNavigate("selectTag/${uiState.note?.id ?: "0"}") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tag),
                            contentDescription = "tag",
                            modifier = Modifier.rotate(30f)
                        )
                    }
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "more"
                    )
                }
            }
        )

        AnimatedContent(targetState = uiState.state, label = "note_content") { state ->
            when (state) {
                is State.Error -> {
                    ErrorState(message = state.message.value(context))
                }

                is State.Loading -> {
                    Loader(modifier = Modifier.fillMaxSize())
                }

                else -> Unit
            }
        }

        AnimatedVisibility(visible = uiState.state is State.Success) {

            Column(modifier = Modifier.fillMaxSize()) {
                TextField(
                    value = titleTextField,
                    placeHolder = stringResource(R.string.note_new_title_label),
                    onValueChange = {
                        titleTextField = it
                    },
                    singleLine = true,
                    style = typography.headlineLarge.copy(color = colorScheme.onBackground),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Default
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester = titleFocusRequester)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                TextField(
                    value = contentTextField,
                    placeHolder = "more details here...",
                    onValueChange = {
                        contentTextField = it
                    },
                    style = typography.bodyMedium.copy(color = colorScheme.onBackground),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester = contentFocusRequester)
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                )
            }
        }


    }
}
