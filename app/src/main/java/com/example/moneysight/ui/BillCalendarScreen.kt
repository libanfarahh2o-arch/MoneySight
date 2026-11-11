package com.moneysight.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneysight.app.model.Expense
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.EntryTealContainer
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.ForestDarkBackground
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.util.CurrencyUtils
import com.moneysight.app.viewmodel.AppScreen
import com.moneysight.app.viewmodel.MoneySightViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillCalendarScreen(
    viewModel: MoneySightViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.MAIN)
    }

    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    var selectedYear by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    val calendar = remember(selectedYear, selectedMonth) {
        java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, selectedYear)
            set(java.util.Calendar.MONTH, selectedMonth)
            set(java.util.Calendar.DAY_OF_MONTH, 1)
        }
    }

    val monthName = remember(selectedYear, selectedMonth) {
        java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.UK).format(calendar.time)
    }

    val daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
    val startDayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
    val emptySlotsBefore = startDayOfWeek - 1

    val billsByDay = remember(expenses, selectedYear, selectedMonth) {
        val map = mutableMapOf<Int, MutableList<Expense>>()
        expenses.forEach { expense ->
            if (expense.dueDate.isNotBlank()) {
                val dayNum = expense.dueDate.replace(Regex("[^0-9]"), "").toIntOrNull()
                if (dayNum != null && dayNum in 1..daysInMonth) {
                    map.getOrPut(dayNum) { mutableListOf() }.add(expense)
                }
            }
        }
        map
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ForestDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = EntryTeal,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bill Calendar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.MAIN) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Main",
                            tint = TextPrimaryOnDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ForestSurface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month & Year Navigation Banner
            item(key = "calendar_nav") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (selectedMonth == 0) {
                                    selectedMonth = 11
                                    selectedYear--
                                } else {
                                    selectedMonth--
                                }
                                selectedDay = null
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous Month",
                                tint = EntryTeal
                            )
                        }

                        Text(
                            text = monthName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EntryTeal
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (selectedDay != null) {
                                TextButton(
                                    onClick = { selectedDay = null },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "Show All",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextSecondaryOnDark
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    if (selectedMonth == 11) {
                                        selectedMonth = 0
                                        selectedYear++
                                    } else {
                                        selectedMonth++
                                    }
                                    selectedDay = null
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Next Month",
                                    tint = EntryTeal
                                )
                            }
                        }
                    }
                }
            }

            // Weekday Headers
            item(key = "weekday_headers") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayLabel ->
                        Text(
                            text = dayLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryOnDark,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Calendar Grid
            item(key = "calendar_grid") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val totalCells = emptySlotsBefore + daysInMonth
                        val numRows = (totalCells + 6) / 7

                        val todayCal = java.util.Calendar.getInstance()
                        val todayYear = todayCal.get(java.util.Calendar.YEAR)
                        val todayMonth = todayCal.get(java.util.Calendar.MONTH)
                        val todayDay = todayCal.get(java.util.Calendar.DAY_OF_MONTH)

                        for (row in 0 until numRows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0 until 7) {
                                    val cellIndex = row * 7 + col
                                    val dayNum = cellIndex - emptySlotsBefore + 1
                                    val isValidDay = dayNum in 1..daysInMonth

                                    if (isValidDay) {
                                        val dayBills = billsByDay[dayNum] ?: emptyList()
                                        val hasBills = dayBills.isNotEmpty()
                                        val isSelected = selectedDay == dayNum
                                        val isToday = dayNum == todayDay && selectedMonth == todayMonth && selectedYear == todayYear

                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isSelected -> EntryTeal
                                                        hasBills -> EntryTealContainer
                                                        isToday -> ForestSurfaceVariant
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .border(
                                                    width = if (isToday && !isSelected) 1.5.dp else 0.dp,
                                                    color = if (isToday) EntryTeal else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    selectedDay = if (isSelected) null else dayNum
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = dayNum.toString(),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (hasBills || isToday) FontWeight.Bold else FontWeight.Normal,
                                                    color = when {
                                                        isSelected -> OnEntryTeal
                                                        hasBills -> EntryTeal
                                                        else -> TextPrimaryOnDark
                                                    },
                                                    fontSize = 13.sp
                                                )
                                                if (hasBills) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(5.dp)
                                                            .clip(CircleShape)
                                                            .background(if (isSelected) OnEntryTeal else ExpenseRose)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(42.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bills List Header & Items
            item(key = "bills_header") {
                val displayedBills = if (selectedDay != null) {
                    billsByDay[selectedDay] ?: emptyList()
                } else {
                    expenses.sortedBy { expense ->
                        expense.dueDate.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 999
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (selectedDay != null) "Bills due on Day $selectedDay:" else "All Scheduled Bills (${expenses.size}):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )

                    if (displayedBills.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ForestSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (selectedDay != null) "No bills due on this day." else "No bills recorded.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryOnDark
                                )
                            }
                        }
                    } else {
                        displayedBills.forEach { expense ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = ForestSurface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = expense.description,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryOnDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (expense.dueDate.isNotBlank()) {
                                                Text(
                                                    text = "Due: ${expense.dueDate}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = EntryTeal,
                                                    fontSize = 12.sp
                                                )
                                            }
                                            Text(
                                                text = "Group: ${expense.groupName}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextSecondaryOnDark,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = CurrencyUtils.formatPounds(expense.amount),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExpenseRose
                                        )
                                        val statusText = if (expense.isStamped) "Stamped" else if (expense.isPaid) "Paid" else "Unpaid"
                                        Text(
                                            text = statusText,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (expense.isStamped) IncomeGreen else TextSecondaryOnDark,
                                            fontSize = 11.sp
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
}
