package com.epicdevler.ami.minote.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

internal object Shapes {

    val extraSmall: CornerBasedShape
        @Composable get() = shapes.extraSmall

    val small: CornerBasedShape
        @Composable get() = shapes.small

    val medium: CornerBasedShape
        @Composable get() = shapes.medium

    val large: CornerBasedShape
        @Composable get() = shapes.large

    val extraLarge: CornerBasedShape
        @Composable get() = shapes.extraLarge


    val CornerBasedShape.top: CornerBasedShape
        get() = copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp),
        )

    val CornerBasedShape.bottom: CornerBasedShape
        get() = copy(
            topStart = CornerSize(0.dp),
            topEnd = CornerSize(0.dp),
        )

}