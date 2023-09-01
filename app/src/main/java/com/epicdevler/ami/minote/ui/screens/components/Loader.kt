package com.epicdevler.ami.minote.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epicdevler.ami.minote.R

@Composable
fun Loader(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        LottieAnimation(
            modifier = Modifier.size(100.dp),
            composition = composition,
            iterations = Int.MAX_VALUE,
        )
    }
}