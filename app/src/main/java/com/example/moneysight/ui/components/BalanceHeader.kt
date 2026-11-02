package com.moneysight.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.ui.theme.ForestCardBorder
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.IncomeBorder
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.IncomeGreenContainer
import com.moneysight.app.ui.theme.IncomeSurface
import com.moneysight.app.ui.theme.MintGreenPrimary
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.ThemeMode
import com.moneysight.app.ui.theme.WarningAmber
import com.moneysight.app.ui.theme.WarningRed
import com.moneysight.app.util.CurrencyUtils

@Composable
fun BalanceHeader(
    remainingBalance: Double,
    totalIncome: Double,
    incomeSourcesCount: Int,
    isIncomeExpanded: Boolean,
    currentThemeMode: ThemeMode = ThemeMode.DARK,
    onToggleIncomeExpand: () -> Unit,
    onManageIncomeClicked: () -> Unit,
    onToggleThemeClicked: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ForestSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Controls Row: Theme Button on Top-Left, Collapse Toggle on Top-Right (Unobscured click targets)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top-Left: Compact Theme Mode Switcher Button
                if (onToggleThemeClicked != null) {
                    val isSystemDark = isSystemInDarkTheme()
                    val (themeIcon, themeLabel) = when (currentThemeMode) {
                        ThemeMode.DARK -> Icons.Default.DarkMode to "Dark"
                        ThemeMode.LIGHT -> Icons.Default.WbSunny to "Light"
                        ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness to if (isSystemDark) "Auto (Dark)" else "Auto (Light)"
                    }
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ForestSurfaceVariant)
                            .clickable { onToggleThemeClicked() }
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = themeIcon,
                            contentDescription = "Switch Theme Mode",
                            tint = MintGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = themeLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Top-Right: Collapse / Expand Income Toggle Button
                IconButton(
                    onClick = onToggleIncomeExpand,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (isIncomeExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isIncomeExpanded) "Collapse Income" else "Expand Income",
                        tint = MintGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // CENTRAL HERO COLUMN: Perfectly Centered Title, Label & Big Balance Amount
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleIncomeExpand() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Centralized App Title: MoneySight
                Text(
                    text = "MoneySight",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MintGreenPrimary,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "REMAINING BALANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextMutedOnDark,
                    letterSpacing = 0.8.sp,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                val isNegative = remainingBalance < 0
                val isZero = remainingBalance == 0.0 && totalIncome == 0.0

                val balanceColor = when {
                    isNegative -> WarningRed
                    isZero -> WarningAmber
                    else -> MintGreenPrimary
                }

                Text(
                    text = CurrencyUtils.formatPounds(remainingBalance),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 30.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = balanceColor
                )

                // Income Sources Banner (FULLY COLLAPSIBLE)
                AnimatedVisibility(
                    visible = isIncomeExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier.padding(top = 14.dp)
                    ) {
                        Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(IncomeSurface)
                            .border(1.dp, IncomeBorder, RoundedCornerShape(12.dp))
                            .clickable { onManageIncomeClicked() }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                val subtitleText = when (incomeSourcesCount) {
                                    0 -> "Tap to add income sources"
                                    1 -> "1 Income Source"
                                    else -> "$incomeSourcesCount Income Sources"
                                }
                                Text(
                                    text = subtitleText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = IncomeGreen.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "+${CurrencyUtils.formatPounds(totalIncome)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = IncomeGreen
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(IncomeGreenContainer)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (incomeSourcesCount == 0) Icons.Default.Add else Icons.Default.Edit,
                                    contentDescription = "Manage Income",
                                    tint = IncomeGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (incomeSourcesCount == 0) "Add Income" else "Manage Income",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = IncomeGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
}
