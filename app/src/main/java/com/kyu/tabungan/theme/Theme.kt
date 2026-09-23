package com.kyu.tabungan.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrightBlue,
    onPrimary = Surface,
    primaryContainer = LightBlue,
    onPrimaryContainer = DeepBlue,
    secondary = SkyBlue,
    onSecondary = TextMain,
    background = Background,
    onBackground = TextMain,
    surface = Surface,
    onSurface = TextMain,
    surfaceVariant = LightBlue,
    onSurfaceVariant = TextMuted,
    outline = BorderColor,
    error = StatusDanger,
    onError = Surface
)

@Composable
fun TabunganTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BrightBlue.toArgb()
            window.navigationBarColor = Surface.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
