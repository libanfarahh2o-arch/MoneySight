package com.moneysight.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import com.moneysight.app.ui.components.SimulateGraphDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneysight.app.model.Expense
import androidx.compose.ui.graphics.Color
import com.moneysight.app.ui.theme.ChecklistBorder
import com.moneysight.app.ui.components.RealisticCirclePaidStamp
import com.moneysight.app.ui.components.SimulateGraphDialog
import com.moneysight.app.ui.theme.ChecklistBorder
import com.moneysight.app.ui.theme.ChecklistDarkBackground
import com.moneysight.app.ui.theme.ChecklistPurpleContainer
import com.moneysight.app.ui.theme.ChecklistPurpleLight
import com.moneysight.app.ui.theme.ChecklistPurplePrimary
import com.moneysight.app.ui.theme.ChecklistSurface
import com.moneysight.app.ui.theme.ChecklistSurfaceVariant
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.ExpenseRoseContainer
import com.moneysight.app.ui.theme.IncomeBorder
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.IncomeGreenContainer
import com.moneysight.app.ui.theme.IncomeSurface
import com.moneysight.app.ui.theme.OnChecklistPurple
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.util.CurrencyUtils
import com.moneysight.app.viewmodel.AppScreen
import com.moneysight.app.viewmodel.ChecklistFilter
import com.moneysight.app.viewmodel.MoneySightViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillChecklistScreen(
    viewModel: MoneySightViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.MAIN)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val paidExpensesTotal by viewModel.paidExpensesTotal.collectAsStateWithLifecycle()
    val unpaidExpensesTotal by viewModel.unpaidExpensesTotal.collectAsStateWithLifecycle()
    val totalExpenses by viewModel.totalExpenses.collectAsStateWithLifecycle()
    val paidExpensesCount by viewModel.paidExpensesCount.collectAsStateWithLifecycle()
    val unpaidExpensesCount by viewModel.unpaidExpensesCount.collectAsStateWithLifecycle()
    val stampedExpensesCount by viewModel.stampedExpensesCount.collectAsStateWithLifecycle()
    val unstampedExpensesCount by viewModel.unstampedExpensesCount.collectAsStateWithLifecycle()
    val totalExpensesCount by viewModel.totalExpensesCount.collectAsStateWithLifecycle()

    val manualChecklistIncomeInput by viewModel.manualChecklistIncomeInput.collectAsStateWithLifecycle()

    var localIncomeText by remember { mutableStateOf(manualChecklistIncomeInput) }

    LaunchedEffect(manualChecklistIncomeInput) {
        if (localIncomeText.isEmpty() && manualChecklistIncomeInput.isNotEmpty()) {
            localIncomeText = manualChecklistIncomeInput
        }
    }

    val checklistFilter by viewModel.checklistFilter.collectAsStateWithLifecycle()
    val checklistExpenses by viewModel.checklistExpenses.collectAsStateWithLifecycle()
    val isGroupedChecklistMode by viewModel.isGroupedChecklistMode.collectAsStateWithLifecycle()

    val categoryBreakdowns by viewModel.categoryBreakdownList.collectAsStateWithLifecycle()
    val showSimulateGraphDialog by viewModel.showSimulateGraphDialog.collectAsStateWithLifecycle()
    val showResetAllBillsDialog by viewModel.showResetAllBillsDialog.collectAsStateWithLifecycle()

    // Map tracking expanded state for each group (collapsed by default)
    val expandedGroupsMap = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        containerColor = ChecklistDarkBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openSimulateGraphDialog() },
                containerColor = Color(0xFF8E7CC3),
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Where Your Money Goes Graph",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "Graph",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FactCheck,
                            contentDescription = null,
                            tint = ChecklistPurplePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Simulate",
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
                actions = {
                    IconButton(onClick = { viewModel.openSimulateGraphDialog() }) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = "Where Your Money Goes Graph",
                            tint = ChecklistPurplePrimary
                        )
                    }
                    IconButton(onClick = { viewModel.openResetAllBillsDialog() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset All Bills to Unticked",
                            tint = ChecklistPurplePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChecklistDarkBackground)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 0. Manual Income Allocation Space (On Top of Summary)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ChecklistSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ChecklistBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(IncomeGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = IncomeGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AVAILABLE INCOME / PAYDAY",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = IncomeGreen,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Enter amount to subtract unticked bills",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryOnDark,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Manual Income Input Field
                        OutlinedTextField(
                            value = localIncomeText,
                            onValueChange = { input ->
                                val clean = input.replace(",", ".")
                                if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                                    localIncomeText = clean
                                    viewModel.onChecklistIncomeInputChanged(clean)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Income Amount (£)", color = TextSecondaryOnDark, fontSize = 11.sp) },
                            placeholder = { Text("0.00", color = TextMutedOnDark.copy(alpha = 0.5f)) },
                            prefix = { Text("£ ", fontWeight = FontWeight.Bold, color = IncomeGreen) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    viewModel.onChecklistIncomeInputChanged(localIncomeText)
                                }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ChecklistSurfaceVariant,
                                unfocusedContainerColor = ChecklistSurfaceVariant,
                                focusedBorderColor = IncomeGreen,
                                unfocusedBorderColor = ChecklistBorder,
                                focusedTextColor = TextPrimaryOnDark,
                                unfocusedTextColor = TextPrimaryOnDark,
                                cursorColor = IncomeGreen
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Calculated "REMAINING BALANCE" Summary Box
                        val incomeAmount = CurrencyUtils.parseAmount(localIncomeText) ?: 0.0
                        val remainingAfterUnpaid = incomeAmount - unpaidExpensesTotal

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (remainingAfterUnpaid >= 0) IncomeGreenContainer.copy(alpha = 0.35f)
                                    else ExpenseRoseContainer.copy(alpha = 0.4f)
                                )
                                .border(
                                    1.dp,
                                    if (remainingAfterUnpaid >= 0) IncomeGreen.copy(alpha = 0.6f)
                                    else ExpenseRose.copy(alpha = 0.6f),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "REMAINING BALANCE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (remainingAfterUnpaid >= 0) IncomeGreen else ExpenseRose,
                                        letterSpacing = 0.8.sp
                                    )

                                    Text(
                                        text = "Unticked: -${CurrencyUtils.formatPounds(unpaidExpensesTotal)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryOnDark,
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = CurrencyUtils.formatPounds(remainingAfterUnpaid),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (remainingAfterUnpaid >= 0) IncomeGreen else ExpenseRose
                                )
                            }
                        }
                    }
                }
            }
            // 1. Summary Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ChecklistSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ChecklistBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "BILL CHECKLIST SUMMARY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChecklistPurpleLight.copy(alpha = 0.8f),
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Summary Statistics Cards Grid: Ticked Bills & Left to Tick
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Ticked Bills Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(IncomeGreenContainer.copy(alpha = 0.35f))
                                    .border(1.dp, IncomeGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "TICKED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = IncomeGreen,
                                            fontSize = 11.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(IncomeGreenContainer)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$paidExpensesCount",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = IncomeGreen,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "-${CurrencyUtils.formatPounds(paidExpensesTotal)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = IncomeGreen
                                    )
                                }
                            }

                            // Unticked Bills Left Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ExpenseRoseContainer.copy(alpha = 0.45f))
                                    .border(1.dp, ExpenseRose.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "UNTICKED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExpenseRose,
                                            fontSize = 11.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(ExpenseRoseContainer)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$unpaidExpensesCount",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = ExpenseRose,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "-${CurrencyUtils.formatPounds(unpaidExpensesTotal)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpenseRose
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Filter Selector Chips & Grouping View Segmented Control
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Filter Chips (Unpaid, Paid, All)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChecklistFilter.entries.forEach { filter ->
                            val isSelected = checklistFilter == filter
                            val filterCount = when (filter) {
                                ChecklistFilter.ALL -> totalExpensesCount
                                ChecklistFilter.PAID_ONLY -> paidExpensesCount
                                ChecklistFilter.UNPAID_ONLY -> unpaidExpensesCount
                                ChecklistFilter.STAMPED_ONLY -> stampedExpensesCount
                                ChecklistFilter.UNSTAMPED_ONLY -> unstampedExpensesCount
                            }
                            val chipLabel = "${filter.label} ($filterCount)"

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) ChecklistPurplePrimary else ChecklistSurface)
                                    .border(
                                        1.dp,
                                        if (isSelected) ChecklistPurplePrimary else ChecklistBorder,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { viewModel.onChecklistFilterSelected(filter) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = chipLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimaryOnDark,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Combined View Mode Toggle Button Placed on the Right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ChecklistPurplePrimary)
                                .clickable { viewModel.toggleChecklistGroupedMode() }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isGroupedChecklistMode) Icons.Default.Folder else Icons.AutoMirrored.Filled.FormatListBulleted,
                                    contentDescription = if (isGroupedChecklistMode) "Grouped Folders" else "Flat List",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isGroupedChecklistMode) "Grouped Folders" else "Flat List",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Checklist Items
            if (checklistExpenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = ChecklistSurface),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ChecklistBorder))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(ChecklistPurpleContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ChecklistPurplePrimary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = when (checklistFilter) {
                                    ChecklistFilter.UNPAID_ONLY -> "All bills ticked!"
                                    ChecklistFilter.PAID_ONLY -> "No ticked bills yet."
                                    ChecklistFilter.ALL -> "No bills recorded."
                                    ChecklistFilter.STAMPED_ONLY -> "No stamped bills yet."
                                    ChecklistFilter.UNSTAMPED_ONLY -> "No unstamped bills found."
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryOnDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (checklistFilter) {
                                    ChecklistFilter.UNPAID_ONLY -> "You have ticked all items in Simulate. Excellent job!"
                                    ChecklistFilter.PAID_ONLY -> "Tick off unticked bills below to track them as ticked."
                                    ChecklistFilter.ALL -> "Add expenses on the main screen to track them here."
                                    ChecklistFilter.STAMPED_ONLY -> "Stamp bills on the main page to filter them here."
                                    ChecklistFilter.UNSTAMPED_ONLY -> "All recorded bills have been stamped on the main page."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondaryOnDark,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else if (isGroupedChecklistMode) {
                // GROUPED VIEW: Distinct Master Group Folder Header Cards
                val groupedExpenses = checklistExpenses.groupBy { it.groupId to it.groupName }

                groupedExpenses.forEach { (groupKey, groupBills) ->
                    val (groupId, groupName) = groupKey
                    val isAllGroupPaid = groupBills.all { it.isPaid }
                    val groupTotalAmount = groupBills.sumOf { it.amount }
                    val isGroupExpanded = expandedGroupsMap[groupId] ?: false

                    item(key = "group_header_$groupId") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedGroupsMap[groupId] = !isGroupExpanded
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = ChecklistSurfaceVariant),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ChecklistPurplePrimary.copy(alpha = 0.6f)))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // GROUP MASTER CHECKBOX (Tick entire group together)
                                    Checkbox(
                                        checked = isAllGroupPaid,
                                        onCheckedChange = {
                                            viewModel.toggleGroupExpensesPaidStatus(groupId, !isAllGroupPaid)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = ChecklistPurplePrimary,
                                            uncheckedColor = ExpenseRose,
                                            checkmarkColor = ChecklistDarkBackground
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Distinct Folder Icon Badge
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(ChecklistPurpleContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Folder,
                                            contentDescription = null,
                                            tint = ChecklistPurplePrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    val paidInGroup = groupBills.count { it.isPaid }
                                    val stampedInGroup = groupBills.count { it.isStamped }
                                    val totalInGroup = groupBills.size
                                    val stampedText = if (stampedInGroup > 0) " • $stampedInGroup stamped" else ""
                                    val groupSubtext = if (isAllGroupPaid) {
                                        "All $totalInGroup ticked$stampedText • -${CurrencyUtils.formatPounds(groupTotalAmount)}"
                                    } else {
                                        "$paidInGroup of $totalInGroup ticked$stampedText • -${CurrencyUtils.formatPounds(groupTotalAmount)}"
                                    }

                                    Column {
                                        Text(
                                            text = groupName.ifBlank { "General" },
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ChecklistPurplePrimary
                                        )
                                        Text(
                                            text = groupSubtext,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondaryOnDark,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isAllGroupPaid) ChecklistPurpleContainer else ExpenseRoseContainer)
                                            .clickable {
                                                viewModel.toggleGroupExpensesPaidStatus(groupId, !isAllGroupPaid)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (isAllGroupPaid) "Untick Group" else "Tick Group",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isAllGroupPaid) ChecklistPurpleLight else ExpenseRose,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    IconButton(
                                        onClick = {
                                            expandedGroupsMap[groupId] = !isGroupExpanded
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isGroupExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = if (isGroupExpanded) "Collapse Group" else "Expand Group",
                                            tint = ChecklistPurplePrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (isGroupExpanded) {
                        items(
                            items = groupBills,
                            key = { it.id }
                        ) { expense ->
                            BillChecklistItem(
                                expense = expense,
                                onTogglePaid = { viewModel.toggleExpensePaidStatus(expense.id) },
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            } else {
                // FLAT LIST VIEW
                items(
                    items = checklistExpenses,
                    key = { it.id }
                ) { expense ->
                    BillChecklistItem(
                        expense = expense,
                        onTogglePaid = { viewModel.toggleExpensePaidStatus(expense.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Reset / Refresh All Bills Confirmation Dialog
    if (showResetAllBillsDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissResetAllBillsDialog() },
            containerColor = ChecklistSurface,
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = ChecklistPurplePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset All Bills to Unpaid?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to reset all bills to unpaid? This will unmark all paid bills so you can start a new Simulate list.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryOnDark
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmResetAllBillsToUnpaid() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChecklistPurplePrimary,
                        contentColor = OnChecklistPurple
                    )
                ) {
                    Text("Reset All Bills", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.dismissResetAllBillsDialog() },
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ChecklistBorder))
                ) {
                    Text("Cancel", color = TextSecondaryOnDark)
                }
            }
        )
    }

    // Simulate Category Breakdown Graph Dialog
    if (showSimulateGraphDialog) {
        SimulateGraphDialog(
            categoryBreakdowns = categoryBreakdowns,
            totalExpenses = totalExpenses,
            onDismiss = { viewModel.dismissSimulateGraphDialog() }
        )
    }
}

@Composable
fun BillChecklistItem(
    expense: Expense,
    onTogglePaid: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = if (expense.isPaid) IncomeSurface.copy(alpha = 0.6f) else ChecklistSurface
    val cardBorder = if (expense.isPaid) IncomeBorder.copy(alpha = 0.5f) else ChecklistBorder

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTogglePaid() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(cardBorder))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Interactive Checkbox / Tick Icon
                    Checkbox(
                        checked = expense.isPaid,
                        onCheckedChange = { onTogglePaid() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ChecklistPurplePrimary,
                            uncheckedColor = ExpenseRose,
                            checkmarkColor = ChecklistDarkBackground
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = expense.description,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: Amount
                Text(
                    text = "-${CurrencyUtils.formatPounds(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (expense.isPaid) ChecklistPurplePrimary else ExpenseRose
                )
            }

            // Matching Realistic Circular Rubber Paid Stamp Centered in Middle of Card!
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
