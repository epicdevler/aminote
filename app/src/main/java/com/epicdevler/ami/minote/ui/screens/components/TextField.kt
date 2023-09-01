package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textColor: Color = colorScheme.onBackground,
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
        textStyle = style.copy(color = textColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            innerTextField()
            if (value.text.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = style.copy(color = textColor.copy(.6f)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
    )
}



@Composable
fun IconTextInput(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textColor: Color = colorScheme.onBackground,
    style: TextStyle,
    placeHolder: String,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(colorScheme.onBackground),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            textColor = textColor,
            style = style,
            placeHolder = placeHolder,
            readOnly = readOnly,
            singleLine = singleLine,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            cursorBrush = cursorBrush,
            modifier = Modifier.weight(1f),
        )
        icon?.let {
            Spacer(Modifier.width(10.dp))
            it()
        }
    }
}