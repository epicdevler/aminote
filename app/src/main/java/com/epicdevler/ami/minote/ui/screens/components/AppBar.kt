package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.epicdevler.ami.minote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {
        Image(
            painter = painterResource(id = R.drawable.text_logo),
            contentDescription = "minote_logo",
            colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
        )
    },
    onBackPressed: (() -> Unit)? = null,
    actionsVisible: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            onBackPressed?.let { onClick ->
                BackBtn(onClick = onClick)
            }
        },
        title = title,
        actions = {
            AnimatedVisibility(visible = actionsVisible) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.background,
            navigationIconContentColor = colorScheme.onBackground,
            titleContentColor = colorScheme.onBackground,
            actionIconContentColor = colorScheme.onBackground,
        )
    )
}