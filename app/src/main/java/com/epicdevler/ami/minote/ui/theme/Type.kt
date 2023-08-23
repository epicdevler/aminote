package com.epicdevler.ami.minote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.epicdevler.ami.minote.R

// Set of Material typography styles to start with

private val Jost = FontFamily(
    Font(
        resId = R.font.jost_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.jost_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.jost_medium,
        weight = FontWeight.Medium
    )
)

private val ReemKufi = FontFamily(
    Font(
        resId = R.font.reem_kufi_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.reem_kufi_medium,
        weight = FontWeight.Medium
    )
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = ReemKufi,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ReemKufi,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ReemKufi,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Jost,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Jost,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Jost,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Jost,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp
    )
    /*bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )*/
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */

)