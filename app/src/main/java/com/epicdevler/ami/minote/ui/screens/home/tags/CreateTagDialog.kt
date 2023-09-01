package com.epicdevler.ami.minote.ui.screens.home.tags

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.IconTextInput
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value

@Composable
fun CreateTagDialog(
    uiState: CreateTagVM.UiState,
    onNavigateUp: () -> Unit,
    onCreate: (String) -> Unit
) {
    val context = LocalContext.current
    var newTagName by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var errorMessage by rememberSaveable {
        mutableStateOf("")
    }
    var enabledInteractions by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = uiState.state) {
        val state = uiState.state
        Log.e("TAG", "CreateTagDialog: $state")
        errorMessage = ""
        enabledInteractions = state !is State.Loading
        when (state) {
            is State.Error -> {
                errorMessage = state.message.value(context)
            }

            is State.Success -> {
                onNavigateUp()
                newTagName = TextFieldValue()
                Toast.makeText(context, state.message.value(context), Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }
    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_tag),
                    style = typography.titleMedium,
                    color = colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.create_tag_helper_info),
                    style = typography.bodyMedium,
                    color = colorScheme.onBackground.copy(.8f)
                )
                IconTextInput(
                    enabled = enabledInteractions,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tag),
                            contentDescription = "tag"
                        )
                    },
                    value = newTagName,
                    onValueChange = {
                        newTagName = it
                    },
                    style = typography.bodyMedium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions { onCreate(newTagName.text.trim()) },
                    placeHolder = stringResource(R.string.create_tag_input_placeholder),
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .background(colorScheme.secondary.copy(0.1f), shape = CircleShape)
                        .border(
                            1.dp,
                            shape = CircleShape,
                            color = colorScheme.secondary.copy(.5f)
                        )
                        .padding(12.dp)
                )
                AnimatedVisibility(
                    visible = errorMessage.isNotEmpty(),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.error,
                        style = typography.labelMedium
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
                ) {
                    Button(
                        enabled = enabledInteractions,
                        onClick = onNavigateUp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary,
                            contentColor = colorScheme.onSecondary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    ElevatedButton(
                        enabled = enabledInteractions,
                        onClick = {
                            onCreate(newTagName.text.trim())
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.create),
                            style = typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}