package com.epicdevler.ami.minote.ui.screens.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epicdevler.ami.minote.R
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.ui.screens.components.Loader
import com.epicdevler.ami.minote.ui.utils.State

@Composable
fun SelectTagDialog(
    noteId: String,
    onNavigate: (String) -> Unit = {}
) {
    val vm: SelectTagVM = viewModel()
    LaunchedEffect(Unit) {
        vm.onEvent(SelectTagVM.Events.Load(noteId))
    }
    val uiState = vm.uiState
    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorScheme.background
            )
        ) {
            LazyColumn {
                item {
                    Column(
                        Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.tag_note),
                                style = typography.titleMedium,
                                color = colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { onNavigate("createTag") }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = "add"
                                )
                            }
                        }
                        Text(
                            text = "Tags helps you identity notes tied to a particular subject matter.",
                            style = typography.bodyMedium,
                            color = colorScheme.onBackground.copy(.8f)
                        )
                    }
                }

                if (uiState.state is State.Loading || uiState.state is State.Idle) {
                    item {
                        Loader(modifier = Modifier)
                    }
                }

                items(
                    items = uiState.tags
                ) { tag ->
                    SelectableTag(
                        selected = tag.contains,
                        tag,
                        onSelect = {
                            vm.onEvent(
                                SelectTagVM.Events.Select(
                                    tagId = tag.id,
                                    noteId = noteId
                                )
                            )
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun SelectableTag(selected: Boolean, tag: DummyNotesData.TagContains, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onSelect()
            }
            .fillMaxWidth()
            .clip(shapes.medium)
            .padding(16.dp),
    ) {
        Text(
            text = tag.title,
            style = typography.bodyMedium,
            color = colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )
        RadioButton(
            selected = selected,
            onClick = null
        )
    }
}
