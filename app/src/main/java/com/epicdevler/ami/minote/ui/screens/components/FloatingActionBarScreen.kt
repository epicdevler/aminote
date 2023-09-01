package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.epicdevler.ami.minote.R

@Composable
fun FloatingActionBarScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onFabClick: () -> Unit,
    fabVisible: Boolean,
    fabIcon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "add"
        )
    },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(Modifier.fillMaxSize()){
            content()
        }
        FloatingButton(onClick = onFabClick, visible = fabVisible, icon = fabIcon
        )
    }
}