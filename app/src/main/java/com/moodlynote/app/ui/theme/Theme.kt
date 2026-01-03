package com.moodlynote.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class ThemeMode(val value: String) {
    Soft("soft"),
    Normal("normal"),
    Dark("dark");

    companion object {
        fun fromSetting(value: String): ThemeMode {
            return entries.firstOrNull { it.value == value } ?: Soft
        }
    }
}

private val SoftColorScheme = lightColorScheme(
    primary = SoftPrimary,
    secondary = SoftSecondary,
    background = SoftBackground,
    surface = SoftSurface,
    onPrimary = SoftOnPrimary,
    onSurface = SoftOnSurface,
    onBackground = SoftOnSurface
)

private val NormalColorScheme = lightColorScheme(
    primary = NormalPrimary,
    secondary = NormalSecondary,
    background = NormalBackground,
    surface = NormalSurface,
    onPrimary = NormalOnPrimary,
    onSurface = NormalOnSurface,
    onBackground = NormalOnSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSurface = DarkOnSurface,
    onBackground = DarkOnSurface
)

@Composable
fun MoodlyNoteTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val colors = when (themeMode) {
        ThemeMode.Soft -> SoftColorScheme
        ThemeMode.Normal -> NormalColorScheme
        ThemeMode.Dark -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
