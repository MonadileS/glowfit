package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  onPrimary = Color.White,
  primaryContainer = DarkSurfaceElevated,
  onPrimaryContainer = DarkPrimary,
  secondary = DarkSecondary,
  onSecondary = Color.White,
  secondaryContainer = DarkSurfaceElevated,
  onSecondaryContainer = DarkSecondary,
  tertiary = DarkTertiary,
  onTertiary = Color.White,
  background = DarkBackground,
  onBackground = TextPrimaryDark,
  surface = DarkSurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondaryDark,
  outline = OutlineDark,
  outlineVariant = OutlineDark.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
  primary = ActivePrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFEECEE),
  onPrimaryContainer = ActivePrimaryDark,
  secondary = ActiveSecondary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFFF1EE),
  onSecondaryContainer = ActiveSecondaryDark,
  tertiary = SageMatcha,
  onTertiary = Color.White,
  tertiaryContainer = SageMatchaLight,
  onTertiaryContainer = SageMatchaDark,
  background = MinimalBackgroundLight,
  onBackground = TextPrimaryLight,
  surface = MinimalSurfaceLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = Color(0xFFF3F4F6),
  onSurfaceVariant = TextSecondaryLight,
  outline = OutlineLight,
  outlineVariant = OutlineLight.copy(alpha = 0.5f)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
