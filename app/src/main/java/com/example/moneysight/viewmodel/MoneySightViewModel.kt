package com.moneysight.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moneysight.app.data.MoneySightRepository
import com.moneysight.app.model.Expense
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.model.IncomeSource
import com.moneysight.app.ui.theme.ThemeMode
import com.moneysight.app.util.CurrencyUtils
import com.moneysight.app.util.DateUtils
import com.moneysight.app.util.DueDateStatus
import com.moneysight.app.util.ExpenseCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ExpenseSortOrder(val label: String) {
    EARLIEST_DUE("Earliest Bill to Pay"),
    ALPHABETICAL("Alphabetical (A-Z)"),
    AMOUNT_HIGH_LOW("Highest Amount"),
    AMOUNT_LOW_HIGH("Lowest Amount")
}

enum class ChecklistFilter(val label: String) {
    ALL("All Bills"),
    PAID_ONLY("Ticked Bills"),
    UNPAID_ONLY("Unticked Bills"),
    STAMPED_ONLY("Stamped Bills"),
    UNSTAMPED_ONLY("Unstamped Bills")
}

enum class MainExpenseStampFilter {
    SPENT_ALL,
    STAMPED_ONLY,
    UNSTAMPED_ONLY
}

enum class AppScreen {
    MAIN,
    BILL_CHECKLIST
}

class MoneySightViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MoneySightRepository(application)

    // Theme Mode
    val themeMode: StateFlow<ThemeMode> = repository.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.DARK
    )

    fun toggleThemeMode() {
        val nextMode = when (themeMode.value) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.SYSTEM
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        viewModelScope.launch {
            repository.setThemeMode(nextMode)
        }
    }

    // Screen Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.MAIN)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Expense Groups (People / Custom Categories)
    val expenseGroups: StateFlow<List<ExpenseGroup>> = repository.expenseGroupsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
    )

    private val _selectedGroupIdForNewExpense = MutableStateFlow("general")
    val selectedGroupIdForNewExpense: StateFlow<String> = _selectedGroupIdForNewExpense.asStateFlow()

    private val _selectedGroupFilterId = MutableStateFlow<String?>(null)
    val selectedGroupFilterId: StateFlow<String?> = _selectedGroupFilterId.asStateFlow()

    private val _showManageGroupsDialog = MutableStateFlow(false)
    val showManageGroupsDialog: StateFlow<Boolean> = _showManageGroupsDialog.asStateFlow()

    private val _groupNameInput = MutableStateFlow("")
    val groupNameInput: StateFlow<String> = _groupNameInput.asStateFlow()

    private val _groupToDelete = MutableStateFlow<ExpenseGroup?>(null)
    val groupToDelete: StateFlow<ExpenseGroup?> = _groupToDelete.asStateFlow()

    // Presets Management
    val customPresets: StateFlow<List<String>> = repository.customPresetsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val selectedPresetsRaw: StateFlow<Set<String>?> = repository.selectedPresetsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val selectedPresets: StateFlow<Set<String>> = combine(
        customPresets,
        selectedPresetsRaw
    ) { customList, rawSelected ->
        rawSelected ?: (ExpenseCategories.allPresetItems + customList).toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseCategories.allPresetItems.toSet()
    )

    val combinedCategories: StateFlow<List<com.moneysight.app.util.ExpenseCategory>> = customPresets.map { custom ->
        ExpenseCategories.getCombinedCategories(custom)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseCategories.allCategories
    )

    val activeCategories: StateFlow<List<com.moneysight.app.util.ExpenseCategory>> = combine(
        customPresets,
        selectedPresets
    ) { custom, selected ->
        ExpenseCategories.getActiveCategories(custom, selected)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseCategories.allCategories
    )

    private val _showManagePresetsDialog = MutableStateFlow(false)
    val showManagePresetsDialog: StateFlow<Boolean> = _showManagePresetsDialog.asStateFlow()

    private val _customPresetInput = MutableStateFlow("")
    val customPresetInput: StateFlow<String> = _customPresetInput.asStateFlow()

    // MAIN SCREEN Income Sources
    val incomeSources: StateFlow<List<IncomeSource>> = repository.incomeSourcesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalIncome: StateFlow<Double> = incomeSources.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // INDEPENDENT CHECKLIST PAGE Income Sources
    val checklistIncomeSources: StateFlow<List<IncomeSource>> = repository.checklistIncomeSourcesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val checklistTotalIncome: StateFlow<Double> = checklistIncomeSources.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val expenses: StateFlow<List<Expense>> = repository.expensesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalExpenses: StateFlow<Double> = expenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val remainingBalance: StateFlow<Double> = combine(totalIncome, totalExpenses) { income, total ->
        income - total
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Bill Checklist Statistics
    val paidExpensesTotal: StateFlow<Double> = expenses.map { list ->
        list.filter { it.isPaid }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val unpaidExpensesTotal: StateFlow<Double> = expenses.map { list ->
        list.filter { !it.isPaid }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val paidExpensesCount: StateFlow<Int> = expenses.map { list ->
        list.count { it.isPaid }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val unpaidExpensesCount: StateFlow<Int> = expenses.map { list ->
        list.count { !it.isPaid }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val stampedExpensesCount: StateFlow<Int> = expenses.map { list ->
        list.count { it.isStamped }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val unstampedExpensesCount: StateFlow<Int> = expenses.map { list ->
        list.count { !it.isStamped }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalExpensesCount: StateFlow<Int> = expenses.map { list ->
        list.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val availableAfterPaidBills: StateFlow<Double> = combine(checklistTotalIncome, paidExpensesTotal) { income, paid ->
        income - paid
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val checklistProjectedRemaining: StateFlow<Double> = combine(checklistTotalIncome, totalExpenses) { income, total ->
        income - total
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val autoResetStampEnabled: StateFlow<Boolean> = repository.autoResetStampEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val autoResetStampDay: StateFlow<Int> = repository.autoResetStampDayFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 1
    )

    // Manual Income entered on Checklist page
    private val _manualChecklistIncomeInput = MutableStateFlow("")
    val manualChecklistIncomeInput: StateFlow<String> = _manualChecklistIncomeInput.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndPerformAutoStampReset()
        }
        viewModelScope.launch {
            repository.manualChecklistIncomeFlow.collect { saved ->
                if (_manualChecklistIncomeInput.value.isEmpty() && saved.isNotEmpty()) {
                    _manualChecklistIncomeInput.value = saved
                }
            }
        }
    }

    val manualChecklistIncomeAmount: StateFlow<Double> = manualChecklistIncomeInput.map { input ->
        CurrencyUtils.parseAmount(input) ?: 0.0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val remainingAfterUnpaidBills: StateFlow<Double> = combine(
        manualChecklistIncomeAmount,
        unpaidExpensesTotal
    ) { income, unpaid ->
        income - unpaid
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    private val _checklistFilter = MutableStateFlow(ChecklistFilter.ALL)
    val checklistFilter: StateFlow<ChecklistFilter> = _checklistFilter.asStateFlow()

    private val _isGroupedChecklistMode = MutableStateFlow(false)
    val isGroupedChecklistMode: StateFlow<Boolean> = _isGroupedChecklistMode.asStateFlow()

    // Stamp Mode State for Main Page
    private val _isStampModeActive = MutableStateFlow(false)
    val isStampModeActive: StateFlow<Boolean> = _isStampModeActive.asStateFlow()

    val checklistExpenses: StateFlow<List<Expense>> = combine(
        expenses,
        checklistFilter
    ) { list, filter ->
        val filtered = when (filter) {
            ChecklistFilter.ALL -> list
            ChecklistFilter.PAID_ONLY -> list.filter { it.isPaid }
            ChecklistFilter.UNPAID_ONLY -> list.filter { !it.isPaid }
            ChecklistFilter.STAMPED_ONLY -> list.filter { it.isStamped }
            ChecklistFilter.UNSTAMPED_ONLY -> list.filter { !it.isStamped }
        }
        filtered.sortedByDescending { it.timestamp }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Expense Filter & Search State
    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _expenseSearchQuery = MutableStateFlow("")
    val expenseSearchQuery: StateFlow<String> = _expenseSearchQuery.asStateFlow()

    private val _selectedSortOrder = MutableStateFlow(ExpenseSortOrder.EARLIEST_DUE)
    val selectedSortOrder: StateFlow<ExpenseSortOrder> = _selectedSortOrder.asStateFlow()

    private val _mainExpenseStampFilter = MutableStateFlow(MainExpenseStampFilter.SPENT_ALL)
    val mainExpenseStampFilter: StateFlow<MainExpenseStampFilter> = _mainExpenseStampFilter.asStateFlow()

    fun selectMainExpenseStampFilter(filter: MainExpenseStampFilter) {
        _mainExpenseStampFilter.value = filter
    }

    // Base Filtered Expenses for Main Screen (reflects Group, Search, Category & Sort)
    val baseFilteredExpenses: StateFlow<List<Expense>> = combine(
        expenses,
        selectedCategoryFilter,
        selectedGroupFilterId,
        expenseSearchQuery,
        selectedSortOrder
    ) { args: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val list = args[0] as List<Expense>
        val categoryFilter = args[1] as String?
        val groupFilterId = args[2] as String?
        val query = args[3] as String
        val sort = args[4] as ExpenseSortOrder

        var result = list

        // 1. Expense Group Filter (e.g. "Dad", "House", "Alex")
        if (!groupFilterId.isNullOrBlank() && groupFilterId != "ALL") {
            result = result.filter { it.groupId == groupFilterId }
        }

        // 2. Text Search Filter
        if (query.isNotBlank()) {
            val q = query.trim()
            result = result.filter {
                it.description.contains(q, ignoreCase = true) || it.groupName.contains(q, ignoreCase = true)
            }
        }

        // 3. Category Filter
        if (!categoryFilter.isNullBLankOrAll()) {
            result = result.filter { expense ->
                val categoryName = ExpenseCategories.getCategoryForExpense(expense.description)
                categoryName.equals(categoryFilter, ignoreCase = true) ||
                        expense.description.contains(categoryFilter!!, ignoreCase = true)
            }
        }

        fun parseDueDayNumber(dueDateStr: String): Int {
            if (dueDateStr.isBlank()) return Int.MAX_VALUE
            val numericOnly = dueDateStr.replace(Regex("[^0-9]"), "")
            return numericOnly.toIntOrNull() ?: Int.MAX_VALUE
        }

        fun getEarliestDuePriorityRank(expense: Expense): Int {
            if (expense.isStamped) return 3
            if (expense.dueDate.isBlank()) return 2
            val status = DateUtils.calculateDueDateStatus(expense.dueDate)
            return when (status) {
                is DueDateStatus.Today -> 0
                is DueDateStatus.DaysRemaining -> 0
                is DueDateStatus.Overdue -> 1
                is DueDateStatus.Invalid -> 2
            }
        }

        // 4. Sorting & Filter Rules
        when (sort) {
            ExpenseSortOrder.EARLIEST_DUE -> {
                // Priority 0: Unstamped upcoming due dates (on top first)
                // Priority 1: Unstamped past due dates (after upcoming)
                // Priority 2: Bills without due dates
                // Priority 3: Stamped bills
                result.sortedWith(
                    compareBy<Expense> { getEarliestDuePriorityRank(it) }
                        .thenBy { parseDueDayNumber(it.dueDate) }
                        .thenByDescending { it.timestamp }
                )
            }
            ExpenseSortOrder.ALPHABETICAL -> result.sortedBy { it.description.lowercase() }
            ExpenseSortOrder.AMOUNT_HIGH_LOW -> result.sortedByDescending { it.amount }
            ExpenseSortOrder.AMOUNT_LOW_HIGH -> result.sortedBy { it.amount }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered & Sorted Expenses List for Main Screen
    val filteredExpenses: StateFlow<List<Expense>> = combine(
        baseFilteredExpenses,
        mainExpenseStampFilter
    ) { list, stampFilter ->
        when (stampFilter) {
            MainExpenseStampFilter.SPENT_ALL -> list
            MainExpenseStampFilter.STAMPED_ONLY -> list.filter { it.isStamped }
            MainExpenseStampFilter.UNSTAMPED_ONLY -> list.filter { !it.isStamped }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredTotalExpenses: StateFlow<Double> = baseFilteredExpenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Main Page Stamped vs Unstamped Statistics (reflects selected group & search filters without resetting on tab click)
    val stampedExpensesTotal: StateFlow<Double> = baseFilteredExpenses.map { list ->
        list.filter { it.isStamped }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val unstampedExpensesTotal: StateFlow<Double> = baseFilteredExpenses.map { list ->
        list.filter { !it.isStamped }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Expense Form Inputs
    private val _descriptionInput = MutableStateFlow("")
    val descriptionInput: StateFlow<String> = _descriptionInput.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()

    private val _dueDateInput = MutableStateFlow("")
    val dueDateInput: StateFlow<String> = _dueDateInput.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    // Main Page Income Dialog State
    private val _showIncomeDialog = MutableStateFlow(false)
    val showIncomeDialog: StateFlow<Boolean> = _showIncomeDialog.asStateFlow()

    // Checklist Independent Income Dialog State
    private val _showChecklistIncomeDialog = MutableStateFlow(false)
    val showChecklistIncomeDialog: StateFlow<Boolean> = _showChecklistIncomeDialog.asStateFlow()

    private val _incomeNameInput = MutableStateFlow("")
    val incomeNameInput: StateFlow<String> = _incomeNameInput.asStateFlow()

    private val _incomeAmountInput = MutableStateFlow("")
    val incomeAmountInput: StateFlow<String> = _incomeAmountInput.asStateFlow()

    private val _incomeToEdit = MutableStateFlow<IncomeSource?>(null)
    val incomeToEdit: StateFlow<IncomeSource?> = _incomeToEdit.asStateFlow()

    private val _editIncomeNameInput = MutableStateFlow("")
    val editIncomeNameInput: StateFlow<String> = _editIncomeNameInput.asStateFlow()

    private val _editIncomeAmountInput = MutableStateFlow("")
    val editIncomeAmountInput: StateFlow<String> = _editIncomeAmountInput.asStateFlow()

    private val _incomeToDelete = MutableStateFlow<IncomeSource?>(null)
    val incomeToDelete: StateFlow<IncomeSource?> = _incomeToDelete.asStateFlow()

    // Expense Overbudget / Edit / Delete States
    private val _pendingOverbudgetExpense = MutableStateFlow<Expense?>(null)
    val pendingOverbudgetExpense: StateFlow<Expense?> = _pendingOverbudgetExpense.asStateFlow()

    private val _expenseToEdit = MutableStateFlow<Expense?>(null)
    val expenseToEdit: StateFlow<Expense?> = _expenseToEdit.asStateFlow()

    private val _editDescriptionInput = MutableStateFlow("")
    val editDescriptionInput: StateFlow<String> = _editDescriptionInput.asStateFlow()

    private val _editAmountInput = MutableStateFlow("")
    val editAmountInput: StateFlow<String> = _editAmountInput.asStateFlow()

    private val _editGroupIdInput = MutableStateFlow("general")
    val editGroupIdInput: StateFlow<String> = _editGroupIdInput.asStateFlow()

    private val _editNotesInput = MutableStateFlow("")
    val editNotesInput: StateFlow<String> = _editNotesInput.asStateFlow()

    private val _editDueDateInput = MutableStateFlow("")
    val editDueDateInput: StateFlow<String> = _editDueDateInput.asStateFlow()

    private val _pendingOverbudgetEdit = MutableStateFlow<Expense?>(null)
    val pendingOverbudgetEdit: StateFlow<Expense?> = _pendingOverbudgetEdit.asStateFlow()

    private val _expenseToDelete = MutableStateFlow<Expense?>(null)
    val expenseToDelete: StateFlow<Expense?> = _expenseToDelete.asStateFlow()

    // Screen Navigation
    fun navigateTo(screen: AppScreen) {
        _isStampModeActive.value = false
        _currentScreen.value = screen
    }

    // Expense Group Management Actions
    fun selectGroupForNewExpense(groupId: String) {
        _selectedGroupIdForNewExpense.value = groupId
    }

    fun selectGroupFilter(groupId: String?) {
        _selectedGroupFilterId.value = if (groupId == "ALL") null else groupId
    }

    fun openManageGroupsDialog() {
        _groupNameInput.value = ""
        _showManageGroupsDialog.value = true
    }

    fun dismissManageGroupsDialog() {
        _showManageGroupsDialog.value = false
    }

    fun onGroupNameInputChanged(text: String) {
        _groupNameInput.value = text
    }

    fun onAddGroupSubmitted() {
        val name = _groupNameInput.value.trim()
        if (name.isEmpty()) return

        val newGroup = ExpenseGroup(name = name)
        viewModelScope.launch {
            repository.addExpenseGroup(newGroup)
            _selectedGroupIdForNewExpense.value = newGroup.id
            _groupNameInput.value = ""
        }
    }

    fun requestDeleteGroup(group: ExpenseGroup) {
        _groupToDelete.value = group
    }

    fun dismissDeleteGroupDialog() {
        _groupToDelete.value = null
    }

    // Settings & Preset Management Actions
    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _showResetAllStampsDialog = MutableStateFlow(false)
    val showResetAllStampsDialog: StateFlow<Boolean> = _showResetAllStampsDialog.asStateFlow()

    fun openSettingsDialog() {
        _customPresetInput.value = ""
        _showSettingsDialog.value = true
    }

    fun dismissSettingsDialog() {
        _showSettingsDialog.value = false
    }

    fun openResetAllStampsDialog() {
        _showResetAllStampsDialog.value = true
    }

    fun dismissResetAllStampsDialog() {
        _showResetAllStampsDialog.value = false
    }

    fun confirmResetAllStamps() {
        viewModelScope.launch {
            repository.resetAllStampsToUnstamped()
            _showResetAllStampsDialog.value = false
        }
    }

    fun toggleAutoResetStampEnabled() {
        val current = autoResetStampEnabled.value
        viewModelScope.launch {
            repository.setAutoResetStampEnabled(!current)
        }
    }

    fun setAutoResetStampDay(day: Int) {
        viewModelScope.launch {
            repository.setAutoResetStampDay(day)
        }
    }

    fun openManagePresetsDialog() {
        _customPresetInput.value = ""
        _showManagePresetsDialog.value = true
    }

    fun dismissManagePresetsDialog() {
        _showManagePresetsDialog.value = false
    }

    fun onCustomPresetInputChanged(text: String) {
        _customPresetInput.value = text
    }

    fun onAddCustomPresetSubmitted() {
        val preset = _customPresetInput.value.trim()
        if (preset.isEmpty()) return
        viewModelScope.launch {
            repository.addCustomPreset(preset)
            _customPresetInput.value = ""
        }
    }

    fun onRemoveCustomPresetClicked(preset: String) {
        viewModelScope.launch {
            repository.removeCustomPreset(preset)
        }
    }

    fun onTogglePresetSelected(preset: String) {
        val currentSelected = selectedPresets.value
        val newSelected = if (currentSelected.contains(preset)) {
            currentSelected - preset
        } else {
            currentSelected + preset
        }
        viewModelScope.launch {
            repository.setSelectedPresets(newSelected)
        }
    }

    fun onSelectAllPresets() {
        val all = (ExpenseCategories.allPresetItems + customPresets.value).toSet()
        viewModelScope.launch {
            repository.setSelectedPresets(all)
        }
    }

    fun onDeselectAllPresets() {
        viewModelScope.launch {
            repository.setSelectedPresets(emptySet())
        }
    }

    fun confirmDeleteGroup() {
        val group = _groupToDelete.value ?: return
        viewModelScope.launch {
            repository.deleteExpenseGroup(group.id)
            if (_selectedGroupIdForNewExpense.value == group.id) {
                _selectedGroupIdForNewExpense.value = "general"
            }
            if (_selectedGroupFilterId.value == group.id) {
                _selectedGroupFilterId.value = null
            }
            _groupToDelete.value = null
        }
    }

    // Stamp Mode Actions
    fun toggleStampMode() {
        _isStampModeActive.value = !_isStampModeActive.value
    }

    fun setStampModeActive(active: Boolean) {
        _isStampModeActive.value = active
    }

    fun toggleExpenseStampedStatus(expenseId: String) {
        viewModelScope.launch {
            repository.toggleExpenseStampedStatus(expenseId)
        }
    }

    fun onChecklistIncomeInputChanged(input: String) {
        viewModelScope.launch {
            repository.setManualChecklistIncome(input)
        }
    }

    // Select Bill to Edit Dialog State
    private val _showSelectBillToEditDialog = MutableStateFlow(false)
    val showSelectBillToEditDialog: StateFlow<Boolean> = _showSelectBillToEditDialog.asStateFlow()

    fun openSelectBillToEditDialog() {
        _showSelectBillToEditDialog.value = true
    }

    fun dismissSelectBillToEditDialog() {
        _showSelectBillToEditDialog.value = false
    }

    // Reset All Bills Actions
    private val _showResetAllBillsDialog = MutableStateFlow(false)
    val showResetAllBillsDialog: StateFlow<Boolean> = _showResetAllBillsDialog.asStateFlow()

    fun openResetAllBillsDialog() {
        _showResetAllBillsDialog.value = true
    }

    fun dismissResetAllBillsDialog() {
        _showResetAllBillsDialog.value = false
    }

    fun confirmResetAllBillsToUnpaid() {
        viewModelScope.launch {
            repository.resetAllExpensesToUnpaid()
            _showResetAllBillsDialog.value = false
        }
    }

    // Bill Checklist Actions
    fun toggleExpensePaidStatus(expenseId: String) {
        viewModelScope.launch {
            repository.toggleExpensePaidStatus(expenseId)
        }
    }

    fun toggleGroupExpensesPaidStatus(groupId: String, targetPaidState: Boolean) {
        viewModelScope.launch {
            repository.setGroupExpensesPaidStatus(groupId, targetPaidState)
        }
    }

    fun onChecklistFilterSelected(filter: ChecklistFilter) {
        _checklistFilter.value = filter
    }

    fun toggleChecklistGroupedMode() {
        _isGroupedChecklistMode.value = !_isGroupedChecklistMode.value
    }

    // Filter Actions
    fun onCategoryFilterSelected(category: String?) {
        _selectedCategoryFilter.value = if (category == "ALL") null else category
    }

    fun onExpenseSearchQueryChanged(query: String) {
        _expenseSearchQuery.value = query
    }

    fun onSortOrderChanged(order: ExpenseSortOrder) {
        _selectedSortOrder.value = order
    }

    fun clearExpenseFilters() {
        _selectedCategoryFilter.value = null
        _selectedGroupFilterId.value = null
        _expenseSearchQuery.value = ""
        _selectedSortOrder.value = ExpenseSortOrder.EARLIEST_DUE
        _mainExpenseStampFilter.value = MainExpenseStampFilter.SPENT_ALL
    }

    // Expense Actions
    fun onDescriptionChanged(text: String) {
        _descriptionInput.value = text
    }

    fun onAmountChanged(text: String) {
        _amountInput.value = text
    }

    fun onDueDateInputChanged(text: String) {
        _dueDateInput.value = text
    }

    fun onIncomeNameChanged(text: String) {
        _incomeNameInput.value = text
    }

    fun onIncomeAmountChanged(text: String) {
        _incomeAmountInput.value = text
    }

    fun onAddExpenseSubmitted() {
        if (_isSubmitting.value) return

        val description = _descriptionInput.value.trim()
        val amount = CurrencyUtils.parseAmount(_amountInput.value) ?: return

        if (description.isEmpty()) return

        _isSubmitting.value = true

        val currentRemaining = remainingBalance.value
        val groupId = _selectedGroupIdForNewExpense.value
        val group = expenseGroups.value.find { it.id == groupId } ?: ExpenseGroup.DEFAULT_GENERAL
        val dueDate = _dueDateInput.value.trim()

        val newExpense = Expense(
            description = description,
            amount = amount,
            groupId = group.id,
            groupName = group.name,
            dueDate = dueDate
        )

        if (amount > currentRemaining) {
            _pendingOverbudgetExpense.value = newExpense
            _isSubmitting.value = false
        } else {
            viewModelScope.launch {
                repository.addExpense(newExpense)
                clearAddForm()
                _isSubmitting.value = false
            }
        }
    }

    fun confirmPendingOverbudgetExpense() {
        val expense = _pendingOverbudgetExpense.value ?: return
        viewModelScope.launch {
            repository.addExpense(expense)
            _pendingOverbudgetExpense.value = null
            clearAddForm()
        }
    }

    fun dismissPendingOverbudgetExpense() {
        _pendingOverbudgetExpense.value = null
    }

    private fun clearAddForm() {
        _descriptionInput.value = ""
        _amountInput.value = ""
        _dueDateInput.value = ""
    }

    // Main Page Income Actions
    fun openIncomeDialog() {
        _incomeNameInput.value = ""
        _incomeAmountInput.value = ""
        _showIncomeDialog.value = true
    }

    fun dismissIncomeDialog() {
        _showIncomeDialog.value = false
    }

    // Checklist Independent Income Actions
    fun openChecklistIncomeDialog() {
        _incomeNameInput.value = ""
        _incomeAmountInput.value = ""
        _showChecklistIncomeDialog.value = true
    }

    fun dismissChecklistIncomeDialog() {
        _showChecklistIncomeDialog.value = false
    }

    fun onAddIncomeSubmitted() {
        val rawName = _incomeNameInput.value.trim()
        val name = if (rawName.isEmpty()) "Main Income" else rawName
        val amount = CurrencyUtils.parseAmount(_incomeAmountInput.value) ?: return

        viewModelScope.launch {
            if (_showChecklistIncomeDialog.value) {
                repository.addChecklistIncomeSource(IncomeSource(name = name, amount = amount))
            } else {
                repository.addIncomeSource(IncomeSource(name = name, amount = amount))
            }
            _incomeNameInput.value = ""
            _incomeAmountInput.value = ""
        }
    }

    fun startEditingIncome(incomeSource: IncomeSource) {
        _incomeToEdit.value = incomeSource
        _editIncomeNameInput.value = incomeSource.name
        _editIncomeAmountInput.value = incomeSource.amount.toString()
    }

    fun dismissEditIncomeDialog() {
        _incomeToEdit.value = null
    }

    fun onEditIncomeNameChanged(text: String) {
        _editIncomeNameInput.value = text
    }

    fun onEditIncomeAmountChanged(text: String) {
        _editIncomeAmountInput.value = text
    }

    fun saveEditIncomeSubmitted() {
        val original = _incomeToEdit.value ?: return
        val newName = _editIncomeNameInput.value.trim()
        val newAmount = CurrencyUtils.parseAmount(_editIncomeAmountInput.value) ?: return

        if (newName.isEmpty()) return

        viewModelScope.launch {
            if (_showChecklistIncomeDialog.value) {
                repository.updateChecklistIncomeSource(original.copy(name = newName, amount = newAmount))
            } else {
                repository.updateIncomeSource(original.copy(name = newName, amount = newAmount))
            }
            dismissEditIncomeDialog()
        }
    }

    fun requestDeleteIncome(incomeSource: IncomeSource) {
        _incomeToDelete.value = incomeSource
    }

    fun dismissDeleteIncomeDialog() {
        _incomeToDelete.value = null
    }

    fun confirmDeleteIncome() {
        val source = _incomeToDelete.value ?: return
        viewModelScope.launch {
            if (_showChecklistIncomeDialog.value) {
                repository.deleteChecklistIncomeSource(source.id)
            } else {
                repository.deleteIncomeSource(source.id)
            }
            _incomeToDelete.value = null
        }
    }

    // Edit Expense Management
    fun startEditingExpense(expense: Expense) {
        _expenseToEdit.value = expense
        _editDescriptionInput.value = expense.description
        _editAmountInput.value = expense.amount.toString()
        _editGroupIdInput.value = expense.groupId
        _editNotesInput.value = expense.notes
        _editDueDateInput.value = expense.dueDate
    }

    fun dismissEditDialog() {
        _expenseToEdit.value = null
        _pendingOverbudgetEdit.value = null
    }

    fun onEditDescriptionChanged(text: String) {
        _editDescriptionInput.value = text
    }

    fun onEditAmountChanged(text: String) {
        _editAmountInput.value = text
    }

    fun onEditGroupIdChanged(groupId: String) {
        _editGroupIdInput.value = groupId
    }

    fun onEditNotesChanged(text: String) {
        _editNotesInput.value = text
    }

    fun onEditDueDateInputChanged(text: String) {
        _editDueDateInput.value = text
    }

    fun saveEditExpenseSubmitted() {
        val original = _expenseToEdit.value ?: return
        val newDesc = _editDescriptionInput.value.trim()
        val newAmount = CurrencyUtils.parseAmount(_editAmountInput.value) ?: return

        if (newDesc.isEmpty()) return

        val groupId = _editGroupIdInput.value
        val group = expenseGroups.value.find { it.id == groupId } ?: ExpenseGroup.DEFAULT_GENERAL

        val updated = original.copy(
            description = newDesc,
            amount = newAmount,
            groupId = group.id,
            groupName = group.name,
            notes = _editNotesInput.value.trim(),
            dueDate = _editDueDateInput.value.trim()
        )
        val availableForEdit = remainingBalance.value + original.amount

        if (newAmount > availableForEdit) {
            _pendingOverbudgetEdit.value = updated
        } else {
            viewModelScope.launch {
                repository.updateExpense(updated)
                dismissEditDialog()
            }
        }
    }

    fun confirmPendingOverbudgetEdit() {
        val updated = _pendingOverbudgetEdit.value ?: return
        viewModelScope.launch {
            repository.updateExpense(updated)
            dismissEditDialog()
        }
    }

    fun dismissPendingOverbudgetEdit() {
        _pendingOverbudgetEdit.value = null
    }

    // Delete Expense Management
    fun requestDeleteExpense(expense: Expense) {
        _expenseToDelete.value = expense
    }

    fun dismissDeleteDialog() {
        _expenseToDelete.value = null
    }

    fun confirmDeleteExpense() {
        val expense = _expenseToDelete.value ?: return
        viewModelScope.launch {
            repository.deleteExpense(expense.id)
            _expenseToDelete.value = null
        }
    }

    private fun String?.isNullBLankOrAll(): Boolean {
        return this.isNullOrBlank() || this.equals("ALL", ignoreCase = true)
    }
}
