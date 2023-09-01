package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.repositories.NotesRepo

@Composable
fun Note(
    modifier: Modifier = Modifier,
    note: DummyNotesData.Note,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .fillMaxWidth()
            .background(
                color = Color(0xFF6F6F6F).copy(.1f), shape = shapes.medium
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.formattedDate,
                style = typography.labelSmall,
                color = colorScheme.onBackground.copy(.80f),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = note.tag.title,
                style = typography.labelSmall,
                color = colorScheme.onBackground.copy(.80f),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = note.title,
            style = typography.bodyMedium,
            color = colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = note.content,
            style = typography.labelMedium,
            color = colorScheme.onBackground.copy(.9f),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}