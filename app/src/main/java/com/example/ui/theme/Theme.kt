package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = QuranGoldPrimary,
    onPrimary = QuranDarkGreen,
    primaryContainer = QuranSurfaceVariant,
    onPrimaryContainer = QuranGoldLight,
    secondary = QuranTeal,
    onSecondary = QuranDarkGreen,
    tertiary = QuranAccentCyan,
    background = QuranDarkGreen,
    onBackground = TextPrimary,
    surface = QuranSurface,
    onSurface = TextPrimary,
    surfaceVariant = QuranSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = QuranCardBorder
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
