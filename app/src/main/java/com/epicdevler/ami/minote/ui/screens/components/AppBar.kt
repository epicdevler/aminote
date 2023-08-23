package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.epicdevler.ami.minote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            onBackPressed?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colorScheme.onBackground
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "back_button"
                    )
                }
            }
        },
        title = {
            Image(
                painter = painterResource(id = R.drawable.text_logo),
                contentDescription = "minote_logo",
                colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
            )
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.background,
            navigationIconContentColor = colorScheme.onBackground,
            titleContentColor = colorScheme.onBackground,
            actionIconContentColor = colorScheme.onBackground,
        )
    )
}