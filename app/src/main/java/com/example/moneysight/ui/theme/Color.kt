package com.moneysight.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode(val label: String) {
    DARK("Dark Theme"),
    LIGHT("Light Theme"),
    SYSTEM("Follow System")
}

data class MoneySightThemeColors(
    val mainBackground: Color,
    val mainSurface: Color,
    val mainSurfaceVariant: Color,
    val mainCardBorder: Color,

    val incomeSurface: Color,
    val incomeBorder: Color,
    val incomeGreen: Color,
    val incomeGreenContainer: Color,

    val entrySurface: Color,
    val entryBorder: Color,
    val entryTeal: Color,
    val entryTealContainer: Color,
    val onEntryTeal: Color,

    val expenseSurface: Color,
    val expenseBorder: Color,
    val expenseRose: Color,
    val expenseRoseContainer: Color,

    val checklistDarkBackground: Color,
    val checklistSurface: Color,
    val checklistSurfaceVariant: Color,
    val checklistBorder: Color,
    val checklistPurplePrimary: Color,
    val checklistPurpleContainer: Color,
    val checklistPurpleLight: Color,
    val onChecklistPurple: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val warningAmber: Color,
    val warningRed: Color,

    val isDark: Boolean
)

// 1. Dark Theme Palette (The exact current dark theme)
val DarkThemeColors = MoneySightThemeColors(
    mainBackground = Color(0xFF101815),
    mainSurface = Color(0xFF182420),
    mainSurfaceVariant = Color(0xFF202F2A),
    mainCardBorder = Color(0xFF2A3C36),

    incomeSurface = Color(0xFF13251E),
    incomeBorder = Color(0xFF2D5240),
    incomeGreen = Color(0xFF82C498),
    incomeGreenContainer = Color(0xFF1E3A2E),

    entrySurface = Color(0xFF122527),
    entryBorder = Color(0xFF2B5355),
    entryTeal = Color(0xFF72B8B0),
    entryTealContainer = Color(0xFF1E3E40),
    onEntryTeal = Color(0xFF081C1D),

    expenseSurface = Color(0xFF241618),
    expenseBorder = Color(0xFF54282F),
    expenseRose = Color(0xFFE0828A),
    expenseRoseContainer = Color(0xFF3B1C22),

    checklistDarkBackground = Color(0xFF13121A),
    checklistSurface = Color(0xFF1B1926),
    checklistSurfaceVariant = Color(0xFF252233),
    checklistBorder = Color(0xFF38344A),
    checklistPurplePrimary = Color(0xFF8E7CC3),
    checklistPurpleContainer = Color(0xFF282238),
    checklistPurpleLight = Color(0xFF8E7CC3),
    onChecklistPurple = Color(0xFFFFFFFF),

    textPrimary = Color(0xFFEBF2EE),
    textSecondary = Color(0xFF94A3B8),
    textMuted = Color(0xFF82C498),
    warningAmber = Color(0xFFD99B52),
    warningRed = Color(0xFFD97373),

    isDark = true
)

// 2. Crisp, Clean, Daytime Light Theme Palette
val LightThemeColors = MoneySightThemeColors(
    mainBackground = Color(0xFFF0F5F2),
    mainSurface = Color(0xFFFFFFFF),
    mainSurfaceVariant = Color(0xFFE4EDE7),
    mainCardBorder = Color(0xFFCBD8D0),

    incomeSurface = Color(0xFFECFDF5),
    incomeBorder = Color(0xFFA7F3D0),
    incomeGreen = Color(0xFF15803D),
    incomeGreenContainer = Color(0xFFDCFCE7),

    entrySurface = Color(0xFFF0FDFA),
    entryBorder = Color(0xFF99F6E4),
    entryTeal = Color(0xFF0D9488),
    entryTealContainer = Color(0xFFCCFBF1),
    onEntryTeal = Color(0xFFFFFFFF),

    expenseSurface = Color(0xFFFFF1F2),
    expenseBorder = Color(0xFFFECDD3),
    expenseRose = Color(0xFFE11D48),
    expenseRoseContainer = Color(0xFFFFE4E6),

    checklistDarkBackground = Color(0xFFF5F3FF),
    checklistSurface = Color(0xFFFFFFFF),
    checklistSurfaceVariant = Color(0xFFEDE9FE),
    checklistBorder = Color(0xFFDDD6FE),
    checklistPurplePrimary = Color(0xFF8E7CC3),
    checklistPurpleContainer = Color(0xFFEFEBF7),
    checklistPurpleLight = Color(0xFF8E7CC3),
    onChecklistPurple = Color(0xFFFFFFFF),

    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textMuted = Color(0xFF15803D),
    warningAmber = Color(0xFFB45309),
    warningRed = Color(0xFFDC2626),

    isDark = false
)

val LocalMoneySightColors = staticCompositionLocalOf { DarkThemeColors }

// Dynamic Composable Color Getters
val ForestDarkBackground: Color @Composable get() = LocalMoneySightColors.current.mainBackground
val ForestSurface: Color @Composable get() = LocalMoneySightColors.current.mainSurface
val ForestSurfaceVariant: Color @Composable get() = LocalMoneySightColors.current.mainSurfaceVariant
val ForestCardBorder: Color @Composable get() = LocalMoneySightColors.current.mainCardBorder

val IncomeSurface: Color @Composable get() = LocalMoneySightColors.current.incomeSurface
val IncomeBorder: Color @Composable get() = LocalMoneySightColors.current.incomeBorder
val IncomeGreen: Color @Composable get() = LocalMoneySightColors.current.incomeGreen
val IncomeGreenContainer: Color @Composable get() = LocalMoneySightColors.current.incomeGreenContainer

val EntrySurface: Color @Composable get() = LocalMoneySightColors.current.entrySurface
val EntryBorder: Color @Composable get() = LocalMoneySightColors.current.entryBorder
val EntryTeal: Color @Composable get() = LocalMoneySightColors.current.entryTeal
val EntryTealContainer: Color @Composable get() = LocalMoneySightColors.current.entryTealContainer
val OnEntryTeal: Color @Composable get() = LocalMoneySightColors.current.onEntryTeal

val ExpenseSurface: Color @Composable get() = LocalMoneySightColors.current.expenseSurface
val ExpenseBorder: Color @Composable get() = LocalMoneySightColors.current.expenseBorder
val ExpenseRose: Color @Composable get() = LocalMoneySightColors.current.expenseRose
val ExpenseRoseContainer: Color @Composable get() = LocalMoneySightColors.current.expenseRoseContainer

val ChecklistDarkBackground: Color @Composable get() = LocalMoneySightColors.current.checklistDarkBackground
val ChecklistSurface: Color @Composable get() = LocalMoneySightColors.current.checklistSurface
val ChecklistSurfaceVariant: Color @Composable get() = LocalMoneySightColors.current.checklistSurfaceVariant
val ChecklistBorder: Color @Composable get() = LocalMoneySightColors.current.checklistBorder

val ChecklistPurplePrimary: Color @Composable get() = LocalMoneySightColors.current.checklistPurplePrimary
val ChecklistPurpleContainer: Color @Composable get() = LocalMoneySightColors.current.checklistPurpleContainer
val ChecklistPurpleLight: Color @Composable get() = LocalMoneySightColors.current.checklistPurpleLight
val OnChecklistPurple: Color @Composable get() = LocalMoneySightColors.current.onChecklistPurple

val MintGreenPrimary: Color @Composable get() = LocalMoneySightColors.current.incomeGreen
val MintGreenContainer: Color @Composable get() = LocalMoneySightColors.current.incomeGreenContainer
val MintGreenLight: Color @Composable get() = LocalMoneySightColors.current.incomeGreen
val MintGreenDark: Color @Composable get() = LocalMoneySightColors.current.incomeGreenContainer
val OnMintGreen: Color @Composable get() = LocalMoneySightColors.current.onEntryTeal

val TextPrimaryOnDark: Color @Composable get() = LocalMoneySightColors.current.textPrimary
val TextSecondaryOnDark: Color @Composable get() = LocalMoneySightColors.current.textSecondary
val TextMutedOnDark: Color @Composable get() = LocalMoneySightColors.current.textMuted

val WarningAmber: Color @Composable get() = LocalMoneySightColors.current.warningAmber
val WarningRed: Color @Composable get() = LocalMoneySightColors.current.warningRed
