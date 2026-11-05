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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
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
import androidx.compose.ui.graphics.Color
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
            verticalAlignment = Alignment.Top
        ) {
            // Left: Expense Icon + Description + PAID Stamp Badge + Due Date Status + Note Indicator
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-1).dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (expense.isStamped) IncomeGreenContainer else ExpenseRoseContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (expense.isStamped) Icons.Default.Verified else Icons.Default.Receipt,
                        contentDescription = null,
                        tint = if (expense.isStamped) IncomeGreen else ExpenseRose,
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

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

            // Right Side: Amount (Top Right) + Due Date Indicator Underneath
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "-${CurrencyUtils.formatPounds(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (expense.isStamped) IncomeGreen else ExpenseRose
                )

                if (!expense.isStamped && expense.dueDate.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    val dueStatus = DateUtils.calculateDueDateStatus(expense.dueDate)
                    val (statusText, textColor) = when (dueStatus) {
                        is DueDateStatus.Today -> "Due Today" to WarningAmber
                        is DueDateStatus.DaysRemaining -> {
                            val days = dueStatus.days
                            val label = if (days == 1) "1 day left" else "$days days left"
                            label to (if (days <= 3) WarningAmber else IncomeGreen)
                        }
                        is DueDateStatus.Overdue -> {
                            val days = dueStatus.days
                            val label = if (days == 1) "Overdue 1 day" else "Overdue $days days"
                            label to WarningRed
                        }
                        is DueDateStatus.Invalid -> "Due Date" to TextSecondaryOnDark
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        CalendarDayIcon(
                            dayText = expense.dueDate,
                            tint = textColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom Mini Calendar Icon rendering the day/date number directly inside the calendar page.
 */
@Composable
fun CalendarDayIcon(
    dayText: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val numericDay = dayText.replace(Regex("[^0-9]"), "").ifBlank { dayText.take(2) }

    Box(
        modifier = modifier
            .size(width = 18.dp, height = 18.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.2.dp, tint, RoundedCornerShape(4.dp))
            .background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(tint)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = numericDay,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = tint,
                    fontSize = 8.sp,
                    maxLines = 1
                )
            }
        }
    }
}
