package com.moneysight.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moneysight.app.util.ExpenseCategories
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
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
import com.moneysight.app.util.ExpenseCategories
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
        expense.isStamped -> BorderStroke(1.5.dp, IncomeGreen.copy(alpha = 0.6f))
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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Card Row Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left: Group / Category Logo + Description + Note Indicator
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.weight(1f)
                ) {
                    val categoryName = remember(expense.category, expense.description) {
                        if (expense.category.isNotBlank() && !expense.category.equals("General", ignoreCase = true)) {
                            expense.category
                        } else {
                            ExpenseCategories.getCategoryForExpense(expense.description)
                        }
                    }

                    val categoryPreset = remember(categoryName) {
                        ExpenseCategories.getPresetForCategory(categoryName)
                    }

                    Box(
                        modifier = Modifier
                            .offset(y = 1.dp)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(categoryPreset.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ExpenseCategories.getCategoryIcon(categoryPreset.iconName),
                            contentDescription = categoryPreset.title,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
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
                                "Overdue" to WarningRed
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

            // Realistic Circular Rubber Paid Stamp Centered Right in the Middle of the Rectangle!
            if (expense.isStamped) {
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    RealisticCirclePaidStamp()
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

/**
 * Ultra-Realistic Circular Rubber Stamp for Paid Bills with distressed, uneven ink edges,
 * stippled rim erosion, and authentic ink texture.
 */
@Composable
fun RealisticCirclePaidStamp(
    modifier: Modifier = Modifier
) {
    val mainInkColor = Color(0xFF1B5E20)
    val lightInkColor = Color(0xFF2E7D32)
    val stampInkBrush = Brush.linearGradient(
        colors = listOf(
            mainInkColor.copy(alpha = 0.92f),
            lightInkColor.copy(alpha = 0.82f),
            mainInkColor.copy(alpha = 0.88f)
        )
    )

    Box(
        modifier = modifier
            .rotate(-11f)
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(0xFF1B5E20).copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        // Distressed, uneven ink texture border canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeOuter = 2.dp.toPx()
            val strokeInner = 0.9.dp.toPx()
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = (size.width / 2f) - strokeOuter

            // Outer primary distressed dashed ring
            drawCircle(
                brush = stampInkBrush,
                radius = outerRadius,
                center = centerOffset,
                style = Stroke(
                    width = strokeOuter,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 2f, 28f, 1.5f, 10f, 2.5f), 0f)
                )
            )

            // Outer secondary offset ring for uneven edge stippling
            drawCircle(
                color = mainInkColor.copy(alpha = 0.6f),
                radius = outerRadius + 0.5.dp.toPx(),
                center = centerOffset,
                style = Stroke(
                    width = 0.8.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f, 12f, 3f, 5f, 6f), 3f)
                )
            )

            // Inner distressed ring
            drawCircle(
                brush = stampInkBrush,
                radius = outerRadius - 3.5.dp.toPx(),
                center = centerOffset,
                style = Stroke(
                    width = strokeInner,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 2.5f, 9f, 2f, 14f, 3f), 4f)
                )
            )

            // Micro ink bleed dots around outer rim
            for (angle in 0 until 360 step 20) {
                val rad = Math.toRadians(angle.toDouble())
                val jitter = ((angle * 13) % 5) - 2.5f
                val dotX = centerOffset.x + (outerRadius + jitter) * kotlin.math.cos(rad).toFloat()
                val dotY = centerOffset.y + (outerRadius + jitter) * kotlin.math.sin(rad).toFloat()
                drawCircle(
                    color = mainInkColor.copy(alpha = 0.45f),
                    radius = 0.7.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }
        }

        // Stamp Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "★ ★ ★",
                style = TextStyle(
                    brush = stampInkBrush,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 7.sp
                )
            )
            Text(
                text = "PAID",
                style = TextStyle(
                    brush = stampInkBrush,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    letterSpacing = 1.2.sp
                )
            )
        }
    }
}
