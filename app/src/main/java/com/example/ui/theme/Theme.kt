package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    secondary = CyanTeal,
    onSecondary = Color.White,
    tertiary = EmeraldGreen,
    onTertiary = Color.White,
    background = NavyDark,
    onBackground = Color.White,
    surface = Color(0xFF132342),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1C2E52),
    onSurfaceVariant = Color(0xFF9FB0C8),
    outline = Color(0xFF283D68),
    error = DangerRed,
    onError = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    secondary = NavyDark,
    onSecondary = Color.White,
    tertiary = EmeraldGreen,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SoftBlue,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    error = DangerRed,
    onError = Color.White
  )

@Composable
fun KaamKhataTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
