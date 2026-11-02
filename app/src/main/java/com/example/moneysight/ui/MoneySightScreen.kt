package com.moneysight.app.ui

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneysight.app.ui.components.AddExpenseSection
import com.moneysight.app.ui.components.BalanceHeader
import com.moneysight.app.ui.components.DeleteConfirmationDialog
import com.moneysight.app.ui.components.DeleteGroupConfirmationDialog
import com.moneysight.app.ui.components.DeleteIncomeConfirmationDialog
import com.moneysight.app.ui.components.EditExpenseDialog
import com.moneysight.app.ui.components.EditIncomeSourceDialog
import com.moneysight.app.ui.components.ExpenseFilterBar
import com.moneysight.app.ui.components.ExpenseItem
import com.moneysight.app.ui.components.IncomeSourcesDialog
import com.moneysight.app.ui.components.ManageGroupsDialog
import com.moneysight.app.ui.components.ManagePresetsDialog
import com.moneysight.app.ui.components.OverbudgetWarningDialog
import com.moneysight.app.ui.components.SelectBillToEditDialog
import com.moneysight.app.ui.theme.ChecklistPurpleContainer
import com.moneysight.app.ui.theme.ChecklistPurpleLight
import com.moneysight.app.ui.theme.ChecklistPurplePrimary
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.EntryTealContainer
import com.moneysight.app.ui.theme.ExpenseBorder
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.ExpenseRoseContainer
import com.moneysight.app.ui.theme.ExpenseSurface
import com.moneysight.app.ui.theme.ForestDarkBackground
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.IncomeGreenContainer
import com.moneysight.app.ui.theme.OnChecklistPurple
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.util.CurrencyUtils
import com.moneysight.app.viewmodel.AppScreen
import com.moneysight.app.viewmodel.MainExpenseStampFilter
import com.moneysight.app.viewmodel.MoneySightViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneySightScreen(
    viewModel: MoneySightViewModel,
    modifier: Modifier = Modifier
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val incomeSources by viewModel.incomeSources.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val totalExpenses by viewModel.totalExpenses.collectAsStateWithLifecycle()
    val remainingBalance by viewModel.remainingBalance.collectAsStateWithLifecycle()

    // Expense Groups (People / Custom Categories)
    val expenseGroups by viewModel.expenseGroups.collectAsStateWithLifecycle()
    val selectedGroupIdForNewExpense by viewModel.selectedGroupIdForNewExpense.collectAsStateWithLifecycle()
    val selectedGroupFilterId by viewModel.selectedGroupFilterId.collectAsStateWithLifecycle()
    val showManageGroupsDialog by viewModel.showManageGroupsDialog.collectAsStateWithLifecycle()
    val groupNameInput by viewModel.groupNameInput.collectAsStateWithLifecycle()
    val groupToDelete by viewModel.groupToDelete.collectAsStateWithLifecycle()

    // Filter & Search State
    val filteredExpenses by viewModel.filteredExpenses.collectAsStateWithLifecycle()
    val filteredTotalExpenses by viewModel.filteredTotalExpenses.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val expenseSearchQuery by viewModel.expenseSearchQuery.collectAsStateWithLifecycle()
    val selectedSortOrder by viewModel.selectedSortOrder.collectAsStateWithLifecycle()

    val descriptionInput by viewModel.descriptionInput.collectAsStateWithLifecycle()
    val amountInput by viewModel.amountInput.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()

    // Group Collapse / Expand States
    var isIncomeExpanded by remember { mutableStateOf(false) }
    var isEntryExpanded by remember { mutableStateOf(false) }
    var isExpensesExpanded by remember { mutableStateOf(true) }

    // Income Source Dialog States
    val showIncomeDialog by viewModel.showIncomeDialog.collectAsStateWithLifecycle()
    val incomeNameInput by viewModel.incomeNameInput.collectAsStateWithLifecycle()
    val incomeAmountInput by viewModel.incomeAmountInput.collectAsStateWithLifecycle()
    val incomeToEdit by viewModel.incomeToEdit.collectAsStateWithLifecycle()
    val editIncomeNameInput by viewModel.editIncomeNameInput.collectAsStateWithLifecycle()
    val editIncomeAmountInput by viewModel.editIncomeAmountInput.collectAsStateWithLifecycle()
    val incomeToDelete by viewModel.incomeToDelete.collectAsStateWithLifecycle()

    // Expense States
    val pendingOverbudgetExpense by viewModel.pendingOverbudgetExpense.collectAsStateWithLifecycle()
    val expenseToEdit by viewModel.expenseToEdit.collectAsStateWithLifecycle()
    val editDescriptionInput by viewModel.editDescriptionInput.collectAsStateWithLifecycle()
    val editAmountInput by viewModel.editAmountInput.collectAsStateWithLifecycle()
    val editGroupIdInput by viewModel.editGroupIdInput.collectAsStateWithLifecycle()
    val editNotesInput by viewModel.editNotesInput.collectAsStateWithLifecycle()
    val pendingOverbudgetEdit by viewModel.pendingOverbudgetEdit.collectAsStateWithLifecycle()
    val expenseToDelete by viewModel.expenseToDelete.collectAsStateWithLifecycle()

    // Presets Management States
    val activeCategories by viewModel.activeCategories.collectAsStateWithLifecycle()
    val combinedCategories by viewModel.combinedCategories.collectAsStateWithLifecycle()
    val customPresets by viewModel.customPresets.collectAsStateWithLifecycle()
    val selectedPresets by viewModel.selectedPresets.collectAsStateWithLifecycle()
    val showManagePresetsDialog by viewModel.showManagePresetsDialog.collectAsStateWithLifecycle()
    val customPresetInput by viewModel.customPresetInput.collectAsStateWithLifecycle()

    // Stamp Mode State for Main Screen
    val isStampModeActive by viewModel.isStampModeActive.collectAsStateWithLifecycle()

    // Main Page Stamped / Unstamped Totals & Active Stamp Filter
    val stampedExpensesTotal by viewModel.stampedExpensesTotal.collectAsStateWithLifecycle()
    val unstampedExpensesTotal by viewModel.unstampedExpensesTotal.collectAsStateWithLifecycle()
    val mainExpenseStampFilter by viewModel.mainExpenseStampFilter.collectAsStateWithLifecycle()

    // Select Bill to Edit/Delete State
    val showSelectBillToEditDialog by viewModel.showSelectBillToEditDialog.collectAsStateWithLifecycle()

    // Reset Stamp Mode to OFF when leaving main screen or when app is paused/backgrounded
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                viewModel.setStampModeActive(false)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            viewModel.setStampModeActive(false)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ForestDarkBackground,
        floatingActionButton = {
            // Floating Flex Button on bottom right
            ExtendedFloatingActionButton(
                onClick = { viewModel.navigateTo(AppScreen.BILL_CHECKLIST) },
                containerColor = ChecklistPurplePrimary,
                contentColor = OnChecklistPurple,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
                    .offset(y = 38.dp)
                    .padding(end = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ChecklistPurpleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FactCheck,
                        contentDescription = null,
                        tint = ChecklistPurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preview",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OnChecklistPurple
                )
            }
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
            // 1. GROUP 1: Balance Header & Income Section (Includes Theme Switch Button)
            item(key = "balance_header") {
                BalanceHeader(
                    remainingBalance = remainingBalance,
                    totalIncome = totalIncome,
                    incomeSourcesCount = incomeSources.size,
                    isIncomeExpanded = isIncomeExpanded,
                    currentThemeMode = themeMode,
                    onToggleIncomeExpand = { isIncomeExpanded = !isIncomeExpanded },
                    onManageIncomeClicked = { viewModel.openIncomeDialog() },
                    onToggleThemeClicked = { viewModel.toggleThemeMode() }
                )
            }

            // 2. GROUP 2: Add Expense Section with Group Selector
            item(key = "add_expense_section") {
                AddExpenseSection(
                    descriptionInput = descriptionInput,
                    amountInput = amountInput,
                    selectedGroupId = selectedGroupIdForNewExpense,
                    groups = expenseGroups,
                    activeCategories = activeCategories,
                    isSubmitting = isSubmitting,
                    isExpanded = isEntryExpanded,
                    onToggleExpand = { isEntryExpanded = !isEntryExpanded },
                    onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
                    onAmountChanged = { viewModel.onAmountChanged(it) },
                    onSelectGroup = { viewModel.selectGroupForNewExpense(it) },
                    onOpenManageGroupsClicked = { viewModel.openManageGroupsDialog() },
                    onOpenManagePresetsClicked = { viewModel.openManagePresetsDialog() },
                    onAddExpenseClicked = { viewModel.onAddExpenseSubmitted() }
                )
            }

            // 3. GROUP 3: Your Expenses Header with Spent, Stamped, Unstamped Summary
            item(key = "your_expenses_header") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ExpenseSurface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ExpenseBorder))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isExpensesExpanded = !isExpensesExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ExpenseRoseContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ListAlt,
                                        contentDescription = null,
                                        tint = ExpenseRose,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Your Expenses",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark
                                )
                                if (expenses.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ExpenseRoseContainer)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${filteredExpenses.size}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExpenseRose
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = { isExpensesExpanded = !isExpensesExpanded },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isExpensesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (isExpensesExpanded) "Collapse Expenses" else "Expand Expenses",
                                    tint = ExpenseRose
                                )
                            }
                        }

                        if (isExpensesExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Interactive Spent, Stamped, Unstamped Summary Filter Tabs
                            val spentDisplay = if (selectedCategoryFilter != null || !selectedGroupFilterId.isNullOrBlank() || expenseSearchQuery.isNotBlank()) filteredTotalExpenses else totalExpenses

                            val isSpentActive = mainExpenseStampFilter == MainExpenseStampFilter.SPENT_ALL
                            val isStampedActive = mainExpenseStampFilter == MainExpenseStampFilter.STAMPED_ONLY
                            val isUnstampedActive = mainExpenseStampFilter == MainExpenseStampFilter.UNSTAMPED_ONLY

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 1. Spent Box (Mix/All Bills by Default)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSpentActive) ExpenseRoseContainer else ExpenseSurface)
                                        .border(
                                            if (isSpentActive) 1.5.dp else 1.dp,
                                            if (isSpentActive) ExpenseRose else ExpenseBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.selectMainExpenseStampFilter(MainExpenseStampFilter.SPENT_ALL) }
                                        .padding(horizontal = 6.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "SPENT",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ExpenseRose,
                                            fontSize = 10.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = CurrencyUtils.formatPounds(spentDisplay),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ExpenseRose,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                // 2. Stamped Box (Stamped Only)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isStampedActive) IncomeGreenContainer else ExpenseSurface)
                                        .border(
                                            if (isStampedActive) 1.5.dp else 1.dp,
                                            if (isStampedActive) IncomeGreen else ExpenseBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.selectMainExpenseStampFilter(MainExpenseStampFilter.STAMPED_ONLY) }
                                        .padding(horizontal = 6.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "STAMPED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = IncomeGreen,
                                            fontSize = 10.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = CurrencyUtils.formatPounds(stampedExpensesTotal),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = IncomeGreen,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                // 3. Unstamped Box (Unstamped Only)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isUnstampedActive) ForestSurfaceVariant else ExpenseSurface)
                                        .border(
                                            if (isUnstampedActive) 1.5.dp else 1.dp,
                                            if (isUnstampedActive) TextPrimaryOnDark.copy(alpha = 0.6f) else ExpenseBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.selectMainExpenseStampFilter(MainExpenseStampFilter.UNSTAMPED_ONLY) }
                                        .padding(horizontal = 6.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "UNSTAMPED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isUnstampedActive) TextPrimaryOnDark else TextSecondaryOnDark,
                                            fontSize = 10.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = CurrencyUtils.formatPounds(unstampedExpensesTotal),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TextPrimaryOnDark,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Expenses Content (Collapsible with Filter Bar & Search)
            if (isExpensesExpanded) {
                // Filter Bar
                if (expenses.isNotEmpty()) {
                    item(key = "expense_filter_bar") {
                        ExpenseFilterBar(
                            searchQuery = expenseSearchQuery,
                            selectedCategory = selectedCategoryFilter,
                            selectedGroupId = selectedGroupFilterId,
                            groups = expenseGroups,
                            selectedSortOrder = selectedSortOrder,
                            onSearchQueryChanged = { viewModel.onExpenseSearchQueryChanged(it) },
                            onCategorySelected = { viewModel.onCategoryFilterSelected(it) },
                            onGroupSelected = { viewModel.selectGroupFilter(it) },
                            onSortOrderSelected = { viewModel.onSortOrderChanged(it) },
                            onClearFiltersClicked = { viewModel.clearExpenseFilters() },
                            onEditBillClicked = { viewModel.openSelectBillToEditDialog() }
                        )
                    }
                }

                if (expenses.isEmpty()) {
                    item(key = "empty_expenses") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ExpenseSurface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ExpenseBorder))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(ExpenseRoseContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Savings,
                                        contentDescription = null,
                                        tint = ExpenseRose,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No expenses recorded yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Add your income sources above, then record expenses. You can organize expenses into groups/people (e.g. Dad, House, Alex).",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryOnDark,
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                } else if (filteredExpenses.isEmpty()) {
                    item(key = "filtered_empty_expenses") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ExpenseSurface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ExpenseBorder))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    tint = ExpenseRose,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No matching expenses found",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try adjusting or clearing your group/search filter.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryOnDark,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.clearExpenseFilters() },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ExpenseRose,
                                        contentColor = TextPrimaryOnDark
                                    )
                                ) {
                                    Text("Clear Search / Filters", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(
                        items = filteredExpenses,
                        key = { it.id }
                    ) { expense ->
                        ExpenseItem(
                            expense = expense,
                            isStampModeActive = isStampModeActive,
                            onToggleStampedClicked = { viewModel.toggleExpenseStampedStatus(expense.id) }
                        )
                    }
                }
            }

            // 5. Scrollable Action Buttons: Presets (circular) & Stamp (pill) attached to background
            item(key = "presets_stamp_actions") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Circular Presets Button
                    FloatingActionButton(
                        onClick = { viewModel.openManagePresetsDialog() },
                        containerColor = EntryTealContainer,
                        contentColor = EntryTeal,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Presets",
                            tint = EntryTeal,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 2. Stamp Pill Button
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.toggleStampMode() },
                        containerColor = if (isStampModeActive) IncomeGreen else ForestSurfaceVariant,
                        contentColor = if (isStampModeActive) TextPrimaryOnDark else IncomeGreen,
                        shape = CircleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(IncomeGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Stamp Mode",
                                tint = IncomeGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isStampModeActive) "Stamp ON" else "Stamp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isStampModeActive) TextPrimaryOnDark else IncomeGreen
                        )
                    }
                }
            }
        }
    }

    // DIALOGS

    // Manage Expense Groups Dialog
    if (showManageGroupsDialog) {
        ManageGroupsDialog(
            groups = expenseGroups,
            groupNameInput = groupNameInput,
            onNameChanged = { viewModel.onGroupNameInputChanged(it) },
            onAddGroupClicked = { viewModel.onAddGroupSubmitted() },
            onDeleteGroupClicked = { viewModel.requestDeleteGroup(it) },
            onDismiss = { viewModel.dismissManageGroupsDialog() }
        )
    }

    // Delete Expense Group Confirmation Dialog
    groupToDelete?.let { group ->
        DeleteGroupConfirmationDialog(
            group = group,
            onConfirmDelete = { viewModel.confirmDeleteGroup() },
            onDismiss = { viewModel.dismissDeleteGroupDialog() }
        )
    }

    // Manage Income Sources Dialog
    if (showIncomeDialog) {
        IncomeSourcesDialog(
            incomeSources = incomeSources,
            totalIncome = totalIncome,
            incomeNameInput = incomeNameInput,
            incomeAmountInput = incomeAmountInput,
            onNameChanged = { viewModel.onIncomeNameChanged(it) },
            onAmountChanged = { viewModel.onIncomeAmountChanged(it) },
            onAddIncomeClicked = { viewModel.onAddIncomeSubmitted() },
            onEditClicked = { viewModel.startEditingIncome(it) },
            onDeleteClicked = { viewModel.requestDeleteIncome(it) },
            onDismiss = { viewModel.dismissIncomeDialog() }
        )
    }

    // Edit Income Source Dialog
    incomeToEdit?.let {
        EditIncomeSourceDialog(
            nameInput = editIncomeNameInput,
            amountInput = editIncomeAmountInput,
            onNameChanged = { viewModel.onEditIncomeNameChanged(it) },
            onAmountChanged = { viewModel.onEditIncomeAmountChanged(it) },
            onConfirm = { viewModel.saveEditIncomeSubmitted() },
            onDismiss = { viewModel.dismissEditIncomeDialog() }
        )
    }

    // Delete Income Source Confirmation Dialog
    incomeToDelete?.let { incomeSource ->
        DeleteIncomeConfirmationDialog(
            incomeSource = incomeSource,
            onConfirmDelete = { viewModel.confirmDeleteIncome() },
            onDismiss = { viewModel.dismissDeleteIncomeDialog() }
        )
    }

    // Overbudget Warning for Add Expense
    pendingOverbudgetExpense?.let { pending ->
        OverbudgetWarningDialog(
            expenseAmount = pending.amount,
            remainingBalance = remainingBalance,
            onConfirmProceed = { viewModel.confirmPendingOverbudgetExpense() },
            onDismiss = { viewModel.dismissPendingOverbudgetExpense() }
        )
    }

    // Edit Expense Dialog with Group Selector, Notes, and Delete Option
    expenseToEdit?.let {
        EditExpenseDialog(
            descriptionInput = editDescriptionInput,
            amountInput = editAmountInput,
            notesInput = editNotesInput,
            selectedGroupId = editGroupIdInput,
            groups = expenseGroups,
            onDescriptionChanged = { viewModel.onEditDescriptionChanged(it) },
            onAmountChanged = { viewModel.onEditAmountChanged(it) },
            onNotesChanged = { viewModel.onEditNotesChanged(it) },
            onGroupIdChanged = { viewModel.onEditGroupIdChanged(it) },
            onConfirm = { viewModel.saveEditExpenseSubmitted() },
            onDelete = {
                val currentExpense = expenseToEdit
                viewModel.dismissEditDialog()
                if (currentExpense != null) {
                    viewModel.requestDeleteExpense(currentExpense)
                }
            },
            onDismiss = { viewModel.dismissEditDialog() }
        )
    }

    // Overbudget Warning for Edit Expense
    pendingOverbudgetEdit?.let { pendingEdit ->
        OverbudgetWarningDialog(
            expenseAmount = pendingEdit.amount,
            remainingBalance = remainingBalance + (expenseToEdit?.amount ?: 0.0),
            onConfirmProceed = { viewModel.confirmPendingOverbudgetEdit() },
            onDismiss = { viewModel.dismissPendingOverbudgetEdit() }
        )
    }

    // Delete Expense Dialog
    expenseToDelete?.let { expense ->
        DeleteConfirmationDialog(
            expense = expense,
            onConfirmDelete = { viewModel.confirmDeleteExpense() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    // Manage Presets Dialog
    if (showManagePresetsDialog) {
        ManagePresetsDialog(
            combinedCategories = combinedCategories,
            customPresets = customPresets,
            selectedPresets = selectedPresets,
            customPresetInput = customPresetInput,
            onCustomPresetInputChanged = { viewModel.onCustomPresetInputChanged(it) },
            onAddCustomPresetClicked = { viewModel.onAddCustomPresetSubmitted() },
            onRemoveCustomPresetClicked = { viewModel.onRemoveCustomPresetClicked(it) },
            onTogglePresetSelected = { viewModel.onTogglePresetSelected(it) },
            onSelectAllClicked = { viewModel.onSelectAllPresets() },
            onDeselectAllClicked = { viewModel.onDeselectAllPresets() },
            onDismiss = { viewModel.dismissManagePresetsDialog() }
        )
    }

    // Select Bill to Edit / Delete Dialog
    if (showSelectBillToEditDialog) {
        SelectBillToEditDialog(
            expenses = filteredExpenses,
            onExpenseSelected = { expense ->
                viewModel.dismissSelectBillToEditDialog()
                viewModel.startEditingExpense(expense)
            },
            onDismiss = { viewModel.dismissSelectBillToEditDialog() }
        )
    }
}
