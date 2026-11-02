package com.moneysight.app.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkThemeColors.incomeGreen,
    onPrimary = DarkThemeColors.onEntryTeal,
    primaryContainer = DarkThemeColors.incomeGreenContainer,
    onPrimaryContainer = DarkThemeColors.textPrimary,
    background = DarkThemeColors.mainBackground,
    onBackground = DarkThemeColors.textPrimary,
    surface = DarkThemeColors.mainSurface,
    onSurface = DarkThemeColors.textPrimary,
    surfaceVariant = DarkThemeColors.mainSurfaceVariant,
    onSurfaceVariant = DarkThemeColors.textSecondary,
    outline = DarkThemeColors.mainCardBorder,
    error = DarkThemeColors.warningRed
)

private val LightColorScheme = lightColorScheme(
    primary = LightThemeColors.incomeGreen,
    onPrimary = LightThemeColors.onEntryTeal,
    primaryContainer = LightThemeColors.incomeGreenContainer,
    onPrimaryContainer = LightThemeColors.textPrimary,
    background = LightThemeColors.mainBackground,
    onBackground = LightThemeColors.textPrimary,
    surface = LightThemeColors.mainSurface,
    onSurface = LightThemeColors.textPrimary,
    surfaceVariant = LightThemeColors.mainSurfaceVariant,
    onSurfaceVariant = LightThemeColors.textSecondary,
    outline = LightThemeColors.mainCardBorder,
    error = LightThemeColors.warningRed
)

@Composable
fun MoneySightTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemDark
    }

    val themeColors = if (isDark) DarkThemeColors else LightThemeColors
    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity()
            activity?.window?.let { window ->
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalMoneySightColors provides themeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
