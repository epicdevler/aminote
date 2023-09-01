package com.epicdevler.ami.minote.ui.screens.home.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.epicdevler.ami.minote.data.datasource.DummyNotesData

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    tag: DummyNotesData.Tag,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .fillMaxWidth()
            .background(
                color = Color(0xFF6F6F6F).copy(.1f), shape = shapes.medium
            )
            .clip(shapes.medium)
            .padding(10.dp)
    ) {
        Text(
            text = tag.title,
            style = typography.bodyLarge,
            color = colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp).padding(end = 10.dp),
        )
        Column(
            modifier = Modifier.weight(.5f)
        ) {
            Text(
                text = "${tag.availableNotes}",
                style = typography.headlineMedium,
                color = colorScheme.onBackground.copy(.4f),
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = if (tag.availableNotes < 1) "Note" else "Notes",
                style = typography.bodyMedium,
                color = colorScheme.onBackground.copy(.4f),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}