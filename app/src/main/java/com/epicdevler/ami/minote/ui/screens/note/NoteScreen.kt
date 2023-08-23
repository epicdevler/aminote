package com.epicdevler.ami.minote.ui.screens.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.AppBar

@Composable
fun NoteScreen(
    noteId: String?,
    onNavigate: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppBar(
            onBackPressed = onNavigateUp,
            actions = {

                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorScheme.onBackground
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(true) {
                            Text(
                                text = "Tag",
                                style = typography.labelLarge
                            )
                        }
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
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
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

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    style: TextStyle,
    placeHolder: String,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(colorScheme.onBackground),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        readOnly = readOnly,
        enabled = enabled,
        textStyle = style,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            innerTextField()
            if (value.text.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = style.copy(color = style.color.copy(.8f))
                )
            }
        },
    )
}

@Preview(name = "NoteScreen")
@Composable
private fun PreviewNoteScreen() {
    NoteScreen(noteId = null)
}
