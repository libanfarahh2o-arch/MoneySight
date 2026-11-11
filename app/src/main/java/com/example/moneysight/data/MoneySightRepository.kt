package com.moneysight.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.moneysight.app.model.Expense
import com.moneysight.app.model.ExpenseListInfo
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.model.IncomeSource
import com.moneysight.app.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "moneysight_prefs")

class MoneySightRepository(private val context: Context) {

    private object PreferencesKeys {
        val STARTING_AMOUNT = doublePreferencesKey("starting_amount")
        val INCOME_SOURCES_JSON = stringPreferencesKey("income_sources_json")
        val CHECKLIST_INCOME_SOURCES_JSON = stringPreferencesKey("checklist_income_sources_json")
        val EXPENSE_GROUPS_JSON = stringPreferencesKey("expense_groups_json")
        val EXPENSES_JSON = stringPreferencesKey("expenses_json")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CUSTOM_PRESETS_JSON = stringPreferencesKey("custom_presets_json")
        val SELECTED_PRESETS_JSON = stringPreferencesKey("selected_presets_json")
        val MANUAL_CHECKLIST_INCOME = stringPreferencesKey("manual_checklist_income")
        val AUTO_RESET_STAMP_ENABLED = booleanPreferencesKey("auto_reset_stamp_enabled")
        val AUTO_RESET_STAMP_DAY = intPreferencesKey("auto_reset_stamp_day")
        val LAST_STAMP_RESET_MONTH = stringPreferencesKey("last_stamp_reset_month")
        val ACTIVE_LIST_ID = stringPreferencesKey("active_list_id")
        val EXPENSE_LISTS_JSON = stringPreferencesKey("expense_lists_json")
    }

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Flow of Saved Theme Mode (DARK, LIGHT, SYSTEM).
     */
    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val saved = preferences[PreferencesKeys.THEME_MODE]
        if (saved != null) {
            try {
                ThemeMode.valueOf(saved)
            } catch (e: Exception) {
                ThemeMode.DARK
            }
        } else {
            ThemeMode.DARK
        }
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    /**
     * Flow of Expense Groups (People / Custom Categories).
     */
    val expenseGroupsFlow: Flow<List<ExpenseGroup>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.EXPENSE_GROUPS_JSON]
        if (jsonString != null) {
            try {
                val list = json.decodeFromString<List<ExpenseGroup>>(jsonString)
                if (list.isEmpty()) listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE) else list
            } catch (e: Exception) {
                listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
            }
        } else {
            listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
        }
    }

    /**
     * Flow of Main Income Sources.
     */
    val incomeSourcesFlow: Flow<List<IncomeSource>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
        val legacyAmount = preferences[PreferencesKeys.STARTING_AMOUNT] ?: 0.0

        if (jsonString != null) {
            try {
                json.decodeFromString<List<IncomeSource>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } else if (legacyAmount > 0) {
            listOf(IncomeSource(name = "Main Payday", amount = legacyAmount))
        } else {
            emptyList()
        }
    }

    /**
     * Flow of Independent Checklist Income Sources.
     */
    val checklistIncomeSourcesFlow: Flow<List<IncomeSource>> = context.dataStore.data.map { preferences ->
        val checklistJson = preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON]
        val mainJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
        val legacyAmount = preferences[PreferencesKeys.STARTING_AMOUNT] ?: 0.0

        if (checklistJson != null) {
            try {
                json.decodeFromString<List<IncomeSource>>(checklistJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else if (mainJson != null) {
            try {
                json.decodeFromString<List<IncomeSource>>(mainJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else if (legacyAmount > 0) {
            listOf(IncomeSource(name = "Main Payday", amount = legacyAmount))
        } else {
            emptyList()
        }
    }

    val expensesFlow: Flow<List<Expense>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
        try {
            json.decodeFromString<List<Expense>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    val activeListIdFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACTIVE_LIST_ID] ?: "current"
    }

    val expenseListsFlow: Flow<List<ExpenseListInfo>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.EXPENSE_LISTS_JSON]
        if (jsonString != null) {
            try {
                val list = json.decodeFromString<List<ExpenseListInfo>>(jsonString)
                if (list.isEmpty()) listOf(ExpenseListInfo("current", "This Month")) else list
            } catch (e: Exception) {
                listOf(ExpenseListInfo("current", "This Month"))
            }
        } else {
            listOf(ExpenseListInfo("current", "This Month"))
        }
    }

    suspend fun setActiveListId(listId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_LIST_ID] = listId
        }
    }

    suspend fun createNextMonthList(listName: String, sourceListId: String = "current") {
        context.dataStore.edit { preferences ->
            val newListId = "month_list_${System.currentTimeMillis()}"
            val newListInfo = ExpenseListInfo(id = newListId, name = listName)

            val listsJson = preferences[PreferencesKeys.EXPENSE_LISTS_JSON]
            val currentLists = if (listsJson != null) {
                try {
                    json.decodeFromString<List<ExpenseListInfo>>(listsJson)
                } catch (e: Exception) {
                    listOf(ExpenseListInfo("current", "This Month"))
                }
            } else {
                listOf(ExpenseListInfo("current", "This Month"))
            }
            val updatedLists = (currentLists + newListInfo).distinctBy { it.id }
            preferences[PreferencesKeys.EXPENSE_LISTS_JSON] = json.encodeToString(updatedLists)

            val expensesJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val allExpenses = try {
                json.decodeFromString<List<Expense>>(expensesJson)
            } catch (e: Exception) {
                emptyList()
            }

            val sourceExpenses = allExpenses.filter { it.listId == sourceListId || (sourceListId == "current" && (it.listId == "current" || it.listId.isBlank())) }
            val nextMonthCopies = sourceExpenses.map { expense ->
                expense.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    listId = newListId,
                    isStamped = false,
                    isPaid = false,
                    timestamp = System.currentTimeMillis()
                )
            }

            val updatedExpenses = allExpenses + nextMonthCopies
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedExpenses)
            preferences[PreferencesKeys.ACTIVE_LIST_ID] = newListId
        }
    }

    suspend fun deleteExpenseList(listId: String) {
        if (listId == "current") return
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSE_LISTS_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<ExpenseListInfo>>(currentJson)
                } catch (e: Exception) {
                    listOf(ExpenseListInfo("current", "This Month"))
                }
            } else {
                listOf(ExpenseListInfo("current", "This Month"))
            }
            val updated = currentList.filter { it.id != listId }
            preferences[PreferencesKeys.EXPENSE_LISTS_JSON] = json.encodeToString(updated)

            val expensesJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            try {
                val allExpenses = json.decodeFromString<List<Expense>>(expensesJson)
                val remainingExpenses = allExpenses.filter { it.listId != listId }
                preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(remainingExpenses)
            } catch (e: Exception) { }

            if (preferences[PreferencesKeys.ACTIVE_LIST_ID] == listId) {
                preferences[PreferencesKeys.ACTIVE_LIST_ID] = "current"
            }
        }
    }

    // Expense Groups Actions
    suspend fun addExpenseGroup(group: ExpenseGroup) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSE_GROUPS_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<ExpenseGroup>>(currentJson)
                } catch (e: Exception) {
                    listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
                }
            } else {
                listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
            }

            val updatedList = currentList + group
            preferences[PreferencesKeys.EXPENSE_GROUPS_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun updateExpenseGroup(updatedGroup: ExpenseGroup) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSE_GROUPS_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<ExpenseGroup>>(currentJson)
                } catch (e: Exception) {
                    listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
                }
            } else {
                listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
            }

            val updatedList = currentList.map {
                if (it.id == updatedGroup.id) updatedGroup else it
            }
            preferences[PreferencesKeys.EXPENSE_GROUPS_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun deleteExpenseGroup(groupId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSE_GROUPS_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<ExpenseGroup>>(currentJson)
                } catch (e: Exception) {
                    listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
                }
            } else {
                listOf(ExpenseGroup.DEFAULT_GENERAL, ExpenseGroup.DEFAULT_HOUSE)
            }

            val updatedList = currentList.filterNot { it.id == groupId }
            preferences[PreferencesKeys.EXPENSE_GROUPS_JSON] = json.encodeToString(updatedList)
        }
    }

    // Main Income Actions
    suspend fun addIncomeSource(incomeSource: IncomeSource) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
            val legacyAmount = preferences[PreferencesKeys.STARTING_AMOUNT] ?: 0.0
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(currentJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else if (legacyAmount > 0) {
                listOf(IncomeSource(name = "Main Payday", amount = legacyAmount))
            } else {
                emptyList()
            }

            val updatedList = currentList + incomeSource
            preferences[PreferencesKeys.INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun updateIncomeSource(updatedIncomeSource: IncomeSource) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<IncomeSource>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map {
                if (it.id == updatedIncomeSource.id) updatedIncomeSource else it
            }
            preferences[PreferencesKeys.INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun deleteIncomeSource(incomeSourceId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<IncomeSource>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.filterNot { it.id == incomeSourceId }
            preferences[PreferencesKeys.INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    // Independent Checklist Income Actions
    suspend fun addChecklistIncomeSource(incomeSource: IncomeSource) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON]
            val mainJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
            val legacyAmount = preferences[PreferencesKeys.STARTING_AMOUNT] ?: 0.0

            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(currentJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else if (mainJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(mainJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else if (legacyAmount > 0) {
                listOf(IncomeSource(name = "Main Payday", amount = legacyAmount))
            } else {
                emptyList()
            }

            val updatedList = currentList + incomeSource
            preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun updateChecklistIncomeSource(updatedIncomeSource: IncomeSource) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON]
            val mainJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(currentJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else if (mainJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(mainJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            val updatedList = currentList.map {
                if (it.id == updatedIncomeSource.id) updatedIncomeSource else it
            }
            preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun deleteChecklistIncomeSource(incomeSourceId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON]
            val mainJson = preferences[PreferencesKeys.INCOME_SOURCES_JSON]
            val currentList = if (currentJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(currentJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else if (mainJson != null) {
                try {
                    json.decodeFromString<List<IncomeSource>>(mainJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            val updatedList = currentList.filterNot { it.id == incomeSourceId }
            preferences[PreferencesKeys.CHECKLIST_INCOME_SOURCES_JSON] = json.encodeToString(updatedList)
        }
    }

    // Expense Actions
    suspend fun addExpense(expense: Expense) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList + expense
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun updateExpense(updatedExpense: Expense) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map {
                if (it.id == updatedExpense.id) updatedExpense else it
            }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun toggleExpensePaidStatus(expenseId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map {
                if (it.id == expenseId) it.copy(isPaid = !it.isPaid) else it
            }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun setGroupExpensesPaidStatus(groupId: String, targetPaidState: Boolean) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map {
                if (it.groupId == groupId) it.copy(isPaid = targetPaidState) else it
            }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun deleteExpense(expenseId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.filterNot { it.id == expenseId }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    /**
     * Flow of Custom Extra Presets created by the user.
     */
    val customPresetsFlow: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.CUSTOM_PRESETS_JSON]
        if (jsonString != null) {
            try {
                json.decodeFromString<List<String>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    /**
     * Flow of Selected Preset Names.
     * Returns null if user has never customized selections (meaning default to ALL available presets).
     */
    val selectedPresetsFlow: Flow<Set<String>?> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.SELECTED_PRESETS_JSON]
        if (jsonString != null) {
            try {
                json.decodeFromString<List<String>>(jsonString).toSet()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    suspend fun addCustomPreset(preset: String) {
        val clean = preset.trim()
        if (clean.isEmpty()) return
        context.dataStore.edit { preferences ->
            val currentCustomJson = preferences[PreferencesKeys.CUSTOM_PRESETS_JSON]
            val currentCustomList = if (currentCustomJson != null) {
                try {
                    json.decodeFromString<List<String>>(currentCustomJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            if (currentCustomList.none { it.equals(clean, ignoreCase = true) }) {
                val updatedCustom = currentCustomList + clean
                preferences[PreferencesKeys.CUSTOM_PRESETS_JSON] = json.encodeToString(updatedCustom)
            }

            // Also ensure it is added to selected presets if selections exist
            val currentSelectedJson = preferences[PreferencesKeys.SELECTED_PRESETS_JSON]
            if (currentSelectedJson != null) {
                val currentSelectedSet = try {
                    json.decodeFromString<List<String>>(currentSelectedJson).toSet()
                } catch (e: Exception) {
                    emptySet()
                }
                val updatedSelected = currentSelectedSet + clean
                preferences[PreferencesKeys.SELECTED_PRESETS_JSON] = json.encodeToString(updatedSelected.toList())
            }
        }
    }

    suspend fun removeCustomPreset(preset: String) {
        context.dataStore.edit { preferences ->
            val currentCustomJson = preferences[PreferencesKeys.CUSTOM_PRESETS_JSON]
            val currentCustomList = if (currentCustomJson != null) {
                try {
                    json.decodeFromString<List<String>>(currentCustomJson)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            val updatedCustom = currentCustomList.filterNot { it.equals(preset, ignoreCase = true) }
            preferences[PreferencesKeys.CUSTOM_PRESETS_JSON] = json.encodeToString(updatedCustom)

            val currentSelectedJson = preferences[PreferencesKeys.SELECTED_PRESETS_JSON]
            if (currentSelectedJson != null) {
                val currentSelectedSet = try {
                    json.decodeFromString<List<String>>(currentSelectedJson).toSet()
                } catch (e: Exception) {
                    emptySet()
                }
                val updatedSelected = currentSelectedSet.filterNot { it.equals(preset, ignoreCase = true) }
                preferences[PreferencesKeys.SELECTED_PRESETS_JSON] = json.encodeToString(updatedSelected.toList())
            }
        }
    }

    suspend fun setSelectedPresets(selectedPresets: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_PRESETS_JSON] = json.encodeToString(selectedPresets.toList())
        }
    }

    /**
     * Flow of Manual Income amount entered on the Bill Checklist page.
     */
    val manualChecklistIncomeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.MANUAL_CHECKLIST_INCOME] ?: ""
    }

    suspend fun setManualChecklistIncome(amountStr: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MANUAL_CHECKLIST_INCOME] = amountStr
        }
    }

    suspend fun resetAllExpensesToUnpaid() {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map { it.copy(isPaid = false) }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun toggleExpenseStampedStatus(expenseId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map {
                if (it.id == expenseId) it.copy(isStamped = !it.isStamped) else it
            }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    val autoResetStampEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTO_RESET_STAMP_ENABLED] ?: false
    }

    val autoResetStampDayFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTO_RESET_STAMP_DAY] ?: 1
    }

    suspend fun setAutoResetStampEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_RESET_STAMP_ENABLED] = enabled
        }
    }

    suspend fun setAutoResetStampDay(day: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_RESET_STAMP_DAY] = day.coerceIn(1, 31)
        }
    }

    suspend fun resetAllStampsToUnstamped() {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<Expense>>(currentJson)
            } catch (e: Exception) {
                emptyList()
            }
            val updatedList = currentList.map { it.copy(isStamped = false) }
            preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
        }
    }

    suspend fun checkAndPerformAutoStampReset() {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val currentMonthKey = "$year-${String.format(java.util.Locale.UK, "%02d", month)}"

        context.dataStore.edit { preferences ->
            val enabled = preferences[PreferencesKeys.AUTO_RESET_STAMP_ENABLED] ?: false
            val resetDay = preferences[PreferencesKeys.AUTO_RESET_STAMP_DAY] ?: 1
            val lastResetMonth = preferences[PreferencesKeys.LAST_STAMP_RESET_MONTH] ?: ""

            if (enabled && dayOfMonth >= resetDay && lastResetMonth != currentMonthKey) {
                val currentJson = preferences[PreferencesKeys.EXPENSES_JSON] ?: "[]"
                val currentList = try {
                    json.decodeFromString<List<Expense>>(currentJson)
                } catch (e: Exception) {
                    emptyList()
                }
                val updatedList = currentList.map { it.copy(isStamped = false) }
                preferences[PreferencesKeys.EXPENSES_JSON] = json.encodeToString(updatedList)
                preferences[PreferencesKeys.LAST_STAMP_RESET_MONTH] = currentMonthKey
            }
        }
    }
}
