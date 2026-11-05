package com.moneysight.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.Expense
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.ExpenseBorder
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.ExpenseRoseContainer
import com.moneysight.app.ui.theme.ExpenseSurface
import com.moneysight.app.ui.theme.ForestCardBorder
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.IncomeGreenContainer
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.ui.theme.WarningAmber
import com.moneysight.app.ui.theme.WarningRed
import com.moneysight.app.util.CurrencyUtils
import com.moneysight.app.util.DateUtils
import com.moneysight.app.util.DueDateStatus

@Composable
fun ExpenseItem(
    expense: Expense,
    modifier: Modifier = Modifier,
    isStampModeActive: Boolean = false,
    onToggleStampedClicked: () -> Unit = {}
) {
    val cardBorder = when {
        isStampModeActive -> BorderStroke(1.5.dp, IncomeGreen)
        expense.isStamped -> BorderStroke(1.dp, IncomeGreen.copy(alpha = 0.5f))
        else -> BorderStroke(1.dp, ExpenseBorder)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isStampModeActive) {
                onToggleStampedClicked()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ExpenseSurface),
        border = cardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Expense Icon + Description + PAID Stamp Badge + Due Date Status + Note Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (expense.isStamped) IncomeGreenContainer else ExpenseRoseContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (expense.isStamped) Icons.Default.Verified else Icons.Default.Receipt,
                        contentDescription = null,
                        tint = if (expense.isStamped) IncomeGreen else ExpenseRose,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = expense.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )

                    if (expense.isStamped) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(IncomeGreenContainer)
                                .border(1.dp, IncomeGreen.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Paid Stamp",
                                    tint = IncomeGreen,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "PAID STAMP",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = IncomeGreen,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }

                    if (expense.dueDate.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        val dueStatus = DateUtils.calculateDueDateStatus(expense.dueDate)
                        val (statusText, badgeBg, badgeText, badgeBorder) = when (dueStatus) {
                            is DueDateStatus.Today -> Quadruple(
                                "Due Today (${expense.dueDate})",
                                WarningAmber.copy(alpha = 0.2f),
                                WarningAmber,
                                WarningAmber.copy(alpha = 0.6f)
                            )
                            is DueDateStatus.DaysRemaining -> {
                                val days = dueStatus.days
                                val label = if (days == 1) "1 day left (${expense.dueDate})" else "$days days left (${expense.dueDate})"
                                val isUrgent = days <= 3
                                Quadruple(
                                    label,
                                    if (isUrgent) WarningAmber.copy(alpha = 0.2f) else IncomeGreenContainer,
                                    if (isUrgent) WarningAmber else IncomeGreen,
                                    if (isUrgent) WarningAmber.copy(alpha = 0.5f) else IncomeGreen.copy(alpha = 0.5f)
                                )
                            }
                            is DueDateStatus.Overdue -> {
                                val days = dueStatus.days
                                val label = if (days == 1) "Overdue 1 day (${expense.dueDate})" else "Overdue $days days (${expense.dueDate})"
                                Quadruple(
                                    label,
                                    WarningRed.copy(alpha = 0.2f),
                                    WarningRed,
                                    WarningRed.copy(alpha = 0.6f)
                                )
                            }
                            is DueDateStatus.Invalid -> Quadruple(
                                "Due: ${expense.dueDate}",
                                ForestSurfaceVariant,
                                TextSecondaryOnDark,
                                ForestCardBorder
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeBg)
                                .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Days left to payment",
                                    tint = badgeText,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = badgeText,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (expense.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                                contentDescription = "Note",
                                tint = EntryTeal,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = expense.notes,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryOnDark,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Amount + Stamp Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "-${CurrencyUtils.formatPounds(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (expense.isStamped) IncomeGreen else ExpenseRose
                )

                if (isStampModeActive) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (expense.isStamped) IncomeGreenContainer else IncomeGreen.copy(alpha = 0.15f))
                            .border(1.5.dp, IncomeGreen, RoundedCornerShape(8.dp))
                            .clickable { onToggleStampedClicked() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Stamp Paid",
                            tint = IncomeGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
