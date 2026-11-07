package com.moneysight.app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class PresetCategoryItem(
    val id: String,
    val title: String,
    val iconName: String,
    val color: Color
)

data class ExpenseCategory(
    val name: String,
    val items: List<String>
)

object ExpenseCategories {

    val categoryGridPresets = listOf(
        PresetCategoryItem("bills", "Bills", "lightbulb", Color(0xFF0288D1)),
        PresetCategoryItem("charity", "Charity", "ribbon", Color(0xFF556B2F)),
        PresetCategoryItem("eating_out", "Eating out", "restaurant", Color(0xFF8BC34A)),
        PresetCategoryItem("entertainment", "Entertainment", "smiley", Color(0xFF26A69A)),
        PresetCategoryItem("expenses", "Expenses", "receipt", Color(0xFF455A64)),
        PresetCategoryItem("family", "Family", "home", Color(0xFF00ACC1)),
        PresetCategoryItem("finances", "Finances", "wallet", Color(0xFF2E7D32)),
        PresetCategoryItem("general", "General", "category", Color(0xFF78909C)),
        PresetCategoryItem("gifts", "Gifts", "gift", Color(0xFF33691E)),
        PresetCategoryItem("groceries", "Groceries", "basket", Color(0xFFFF9800)),
        PresetCategoryItem("holidays", "Holidays", "luggage", Color(0xFFF57C00)),
        PresetCategoryItem("income", "Income", "income_arrow", Color(0xFF43A047)),
        PresetCategoryItem("personal_care", "Personal Care", "heart", Color(0xFFEF5350)),
        PresetCategoryItem("savings", "Savings", "sprout", Color(0xFF00695C)),
        PresetCategoryItem("shopping", "Shopping", "bag", Color(0xFFEC407A)),
        PresetCategoryItem("transfers", "Transfers", "sync", Color(0xFF00838F)),
        PresetCategoryItem("transport", "Transport", "bus", Color(0xFF0097A7))
    )

    fun getCategoryIcon(iconName: String): ImageVector {
        return when (iconName) {
            "lightbulb" -> Icons.Default.Lightbulb
            "ribbon" -> Icons.Default.VolunteerActivism
            "restaurant" -> Icons.Default.Restaurant
            "smiley" -> Icons.Default.Movie
            "receipt" -> Icons.AutoMirrored.Filled.ReceiptLong
            "home" -> Icons.Default.Home
            "wallet" -> Icons.Default.AccountBalance
            "category" -> Icons.Default.Category
            "gift" -> Icons.Default.CardGiftcard
            "basket" -> Icons.Default.ShoppingCart
            "luggage" -> Icons.Default.BeachAccess
            "income_arrow" -> Icons.Default.SouthWest
            "heart" -> Icons.Default.Favorite
            "sprout" -> Icons.Default.Savings
            "bag" -> Icons.Default.ShoppingBag
            "sync" -> Icons.AutoMirrored.Filled.CompareArrows
            "bus" -> Icons.Default.DirectionsBus
            else -> Icons.Default.Category
        }
    }

    val popularSuggestions = listOf(
        "Bills",
        "Groceries",
        "Eating out",
        "Transport",
        "Shopping",
        "Expenses",
        "Family",
        "Personal Care",
        "Subscriptions",
        "Entertainment"
    )

    val allCategories = listOf(
        ExpenseCategory(
            name = "PRIMARY CATEGORIES",
            items = categoryGridPresets.map { it.title }
        )
    )

    val allPresetItems: List<String> = categoryGridPresets.map { it.title }

    /**
     * Combines default built-in categories with user-added custom extras.
     */
    fun getCombinedCategories(customPresets: List<String>): List<ExpenseCategory> {
        if (customPresets.isEmpty()) return allCategories
        val customCategory = ExpenseCategory(
            name = "MY EXTRA PRESETS",
            items = customPresets
        )
        return allCategories + customCategory
    }

    /**
     * Returns active categories based on selection.
     */
    fun getActiveCategories(
        customPresets: List<String>,
        selectedPresets: Set<String>
    ): List<ExpenseCategory> {
        val combined = getCombinedCategories(customPresets)
        return combined.mapNotNull { category ->
            val activeItems = category.items.filter { selectedPresets.contains(it) }
            if (activeItems.isNotEmpty()) {
                ExpenseCategory(category.name, activeItems)
            } else {
                null
            }
        }
    }

    /**
     * Determines which category an expense description belongs to.
     */
    fun getCategoryForExpense(description: String): String {
        for (preset in categoryGridPresets) {
            if (preset.title.equals(description, ignoreCase = true) || description.contains(preset.title, ignoreCase = true)) {
                return preset.title
            }
        }
        return "Expenses"
    }
}
