package com.epicdevler.ami.minote.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = minote_theme_light_primary,
    onPrimary = minote_theme_light_onPrimary,
    primaryContainer = minote_theme_light_primaryContainer,
    onPrimaryContainer = minote_theme_light_onPrimaryContainer,
    secondary = minote_theme_light_secondary,
    onSecondary = minote_theme_light_onSecondary,
    secondaryContainer = minote_theme_light_secondaryContainer,
    onSecondaryContainer = minote_theme_light_onSecondaryContainer,
    tertiary = minote_theme_light_tertiary,
    onTertiary = minote_theme_light_onTertiary,
    tertiaryContainer = minote_theme_light_tertiaryContainer,
    onTertiaryContainer = minote_theme_light_onTertiaryContainer,
    error = minote_theme_light_error,
    onError = minote_theme_light_onError,
    errorContainer = minote_theme_light_errorContainer,
    onErrorContainer = minote_theme_light_onErrorContainer,
    outline = minote_theme_light_outline,
    background = minote_theme_light_background,
    onBackground = minote_theme_light_onBackground,
    surface = minote_theme_light_surface,
    onSurface = minote_theme_light_onSurface,
    surfaceVariant = minote_theme_light_surfaceVariant,
    onSurfaceVariant = minote_theme_light_onSurfaceVariant,
    inverseSurface = minote_theme_light_inverseSurface,
    inverseOnSurface = minote_theme_light_inverseOnSurface,
    inversePrimary = minote_theme_light_inversePrimary,
    surfaceTint = minote_theme_light_surfaceTint,
    outlineVariant = minote_theme_light_outlineVariant,
    scrim = minote_theme_light_scrim,
)


private val DarkColorScheme = darkColorScheme(
    primary = minote_theme_dark_primary,
    onPrimary = minote_theme_dark_onPrimary,
    primaryContainer = minote_theme_dark_primaryContainer,
    onPrimaryContainer = minote_theme_dark_onPrimaryContainer,
    secondary = minote_theme_dark_secondary,
    onSecondary = minote_theme_dark_onSecondary,
    secondaryContainer = minote_theme_dark_secondaryContainer,
    onSecondaryContainer = minote_theme_dark_onSecondaryContainer,
    tertiary = minote_theme_dark_tertiary,
    onTertiary = minote_theme_dark_onTertiary,
    tertiaryContainer = minote_theme_dark_tertiaryContainer,
    onTertiaryContainer = minote_theme_dark_onTertiaryContainer,
    error = minote_theme_dark_error,
    onError = minote_theme_dark_onError,
    errorContainer = minote_theme_dark_errorContainer,
    onErrorContainer = minote_theme_dark_onErrorContainer,
    outline = minote_theme_dark_outline,
    background = minote_theme_dark_background,
    onBackground = minote_theme_dark_onBackground,
    surface = minote_theme_dark_surface,
    onSurface = minote_theme_dark_onSurface,
    surfaceVariant = minote_theme_dark_surfaceVariant,
    onSurfaceVariant = minote_theme_dark_onSurfaceVariant,
    inverseSurface = minote_theme_dark_inverseSurface,
    inverseOnSurface = minote_theme_dark_inverseOnSurface,
    inversePrimary = minote_theme_dark_inversePrimary,
    surfaceTint = minote_theme_dark_surfaceTint,
    outlineVariant = minote_theme_dark_outlineVariant,
    scrim = minote_theme_dark_scrim,
)

@Composable
fun MinoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}