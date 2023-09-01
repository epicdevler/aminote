package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.epicdevler.ami.minote.R

@Composable
fun BackBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = onClick
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "search")
    }
}