package com.smartlens.tcclosparcerosapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = BackgroundDark,
    onSurface = OnBackgroundDark,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = BackgroundLight,
    onSurface = OnBackgroundLight,
)

private val MonochromeLightScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = GrayLight,
    onPrimaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    secondary = GrayDark,
    onSecondary = White
)

private val MonochromeDarkScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = GrayDark,
    onPrimaryContainer = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    secondary = GrayLight,
    onSecondary = Black
)

@Composable
fun TCCLosparcerosAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    monochromeMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        monochromeMode && darkTheme -> MonochromeDarkScheme
        monochromeMode -> MonochromeLightScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
