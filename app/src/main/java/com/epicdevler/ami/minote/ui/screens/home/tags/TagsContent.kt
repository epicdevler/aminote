package com.epicdevler.ami.minote.ui.screens.home.tags

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.ui.screens.components.AppBar
import com.epicdevler.ami.minote.ui.screens.components.EmptyListState
import com.epicdevler.ami.minote.ui.screens.components.ErrorState
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.utils.BackPressHandler
import com.epicdevler.ami.minote.ui.utils.State
import com.epicdevler.ami.minote.ui.utils.UiText.None.value

@Composable
fun TagsContent(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val vm: TagsVM = viewModel()
    val uiState = vm.uiState
    val context = LocalContext.current

    BackPressHandler(onBackPressed = onNavigateUp)

    Column {
        AppBar()
        Column(
            Modifier
                .padding(16.dp)
                .padding(top = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState.message.value(context),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                )

                AnimatedVisibility(
                    visible = uiState.state is State.Success || try {
                        (uiState.state as State.Error).reason == State.Error.Reason.EmptyData
                    } catch (_: Exception) {
                        false
                    }
                ) {
                    IconButton(onClick = { onNavigate("createTag") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add"
                        )
                    }
                }

            }
            AnimatedVisibility(visible = uiState.state is State.Success) {
                Text(
                    text = "Create custom tags to help categories your notes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AnimatedContent(targetState = uiState.state, label = "note_content") { state ->
            when (state) {
                is State.Loading -> {
                    Loader(modifier = Modifier)
                }

                is State.Error -> {
                    when (state.reason) {
                        State.Error.Reason.EmptyData -> {
                            EmptyListState(message = "Create tags to categories your notes.")
                        }

                        else -> {
                            ErrorState(message = state.message.value(context))
                        }
                    }
                }

                else -> Unit

            }
        }
        AnimatedVisibility(visible = uiState.state is State.Success) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    key = { it.id },
                    items = uiState.tags
                ) { tag ->
                    Tag(
                        modifier = Modifier.fillMaxSize(),
                        tag = tag,
                        onClick = {
                            onNavigate("tag/?tagId=${tag.id}/notes")
                        }
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewTagsContent() {
    TagsContent()
}
