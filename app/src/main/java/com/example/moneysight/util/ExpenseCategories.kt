package com.moneysight.app.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Work
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
        PresetCategoryItem("appliances", "Appliances", "kitchen", Color(0xFF4A6572)),
        PresetCategoryItem("bank_charges", "Bank Charges", "credit_card", Color(0xFFD32F2F)),
        PresetCategoryItem("bills", "Bills", "lightbulb", Color(0xFF0288D1)),
        PresetCategoryItem("business", "Business", "work", Color(0xFF37474F)),
        PresetCategoryItem("car_expenses", "Car Expenses", "bus", Color(0xFF455A64)),
        PresetCategoryItem("charity", "Charity", "volunteer", Color(0xFF556B2F)),
        PresetCategoryItem("childcare", "Childcare", "face", Color(0xFFFFB74D)),
        PresetCategoryItem("clothing", "Clothing", "checkroom", Color(0xFFBA68C8)),
        PresetCategoryItem("debt", "Debt", "credit_card", Color(0xFFE53935)),
        PresetCategoryItem("eating_out", "Eating Out", "restaurant", Color(0xFF8BC34A)),
        PresetCategoryItem("education", "Education", "school", Color(0xFF3F51B5)),
        PresetCategoryItem("entertainment", "Entertainment", "movie", Color(0xFF26A69A)),
        PresetCategoryItem("family", "Family", "groups", Color(0xFF00ACC1)),
        PresetCategoryItem("finance", "Finance", "account_balance", Color(0xFF2E7D32)),
        PresetCategoryItem("fines", "Fines", "gavel", Color(0xFFD32F2F)),
        PresetCategoryItem("fitness", "Fitness", "fitness", Color(0xFF66BB6A)),
        PresetCategoryItem("furniture", "Furniture", "weekend", Color(0xFF795548)),
        PresetCategoryItem("general", "General", "category", Color(0xFF78909C)),
        PresetCategoryItem("gifts", "Gifts", "gift", Color(0xFF33691E)),
        PresetCategoryItem("groceries", "Groceries", "cart", Color(0xFFFF9800)),
        PresetCategoryItem("healthcare", "Healthcare", "medical", Color(0xFFE53935)),
        PresetCategoryItem("hobbies", "Hobbies", "palette", Color(0xFF7E57C2)),
        PresetCategoryItem("holidays", "Holidays", "beach", Color(0xFFF57C00)),
        PresetCategoryItem("home_maintenance", "Home Maintenance", "build", Color(0xFFFF9800)),
        PresetCategoryItem("household", "Household", "home", Color(0xFF795548)),
        PresetCategoryItem("housing", "Housing", "house", Color(0xFF6D4C41)),
        PresetCategoryItem("income", "Income", "trending_up", Color(0xFF43A047)),
        PresetCategoryItem("insurance", "Insurance", "shield", Color(0xFF1E88E5)),
        PresetCategoryItem("investments", "Investments", "show_chart", Color(0xFF1B5E20)),
        PresetCategoryItem("legal", "Legal", "gavel", Color(0xFF4E342E)),
        PresetCategoryItem("personal_care", "Personal Care", "favorite", Color(0xFFEF5350)),
        PresetCategoryItem("pets", "Pets", "pets", Color(0xFFA1887F)),
        PresetCategoryItem("savings", "Savings", "savings", Color(0xFF00695C)),
        PresetCategoryItem("shopping", "Shopping", "bag", Color(0xFFEC407A)),
        PresetCategoryItem("sports", "Sports", "sports", Color(0xFF4CAF50)),
        PresetCategoryItem("subscriptions", "Subscriptions", "subscriptions", Color(0xFFE91E63)),
        PresetCategoryItem("takeaways", "Takeaways", "fastfood", Color(0xFFFF7043)),
        PresetCategoryItem("taxes", "Taxes", "taxes", Color(0xFF6D4C41)),
        PresetCategoryItem("transfers", "Transfers", "transfers", Color(0xFF00838F)),
        PresetCategoryItem("transport", "Transport", "bus", Color(0xFF0097A7)),
        PresetCategoryItem("travel", "Travel", "flight", Color(0xFF039BE5)),
        PresetCategoryItem("tuition", "Tuition", "book", Color(0xFF3F51B5))
    )

    fun getCategoryIcon(iconName: String): ImageVector {
        return when (iconName) {
            "kitchen" -> Icons.Default.Kitchen
            "credit_card" -> Icons.Default.CreditCard
            "lightbulb" -> Icons.Default.Lightbulb
            "work" -> Icons.Default.Work
            "bus" -> Icons.Default.DirectionsBus
            "volunteer" -> Icons.Default.VolunteerActivism
            "face" -> Icons.Default.Face
            "checkroom" -> Icons.Default.Checkroom
            "restaurant" -> Icons.Default.Restaurant
            "school" -> Icons.Default.School
            "movie" -> Icons.Default.Movie
            "groups" -> Icons.Default.Groups
            "account_balance" -> Icons.Default.AccountBalance
            "gavel" -> Icons.Default.Gavel
            "fitness" -> Icons.Default.FitnessCenter
            "weekend" -> Icons.Default.Weekend
            "category" -> Icons.Default.Category
            "gift" -> Icons.Default.CardGiftcard
            "cart" -> Icons.Default.ShoppingCart
            "medical" -> Icons.Default.MedicalServices
            "palette" -> Icons.Default.Palette
            "beach" -> Icons.Default.BeachAccess
            "build" -> Icons.Default.Build
            "home" -> Icons.Default.Home
            "house" -> Icons.Default.House
            "trending_up" -> Icons.AutoMirrored.Filled.TrendingUp
            "shield" -> Icons.Default.Shield
            "show_chart" -> Icons.AutoMirrored.Filled.ShowChart
            "favorite" -> Icons.Default.Favorite
            "pets" -> Icons.Default.Pets
            "savings" -> Icons.Default.Savings
            "bag" -> Icons.Default.ShoppingBag
            "sports" -> Icons.Default.SportsBasketball
            "subscriptions" -> Icons.Default.Subscriptions
            "fastfood" -> Icons.Default.Fastfood
            "taxes" -> Icons.AutoMirrored.Filled.ReceiptLong
            "transfers" -> Icons.AutoMirrored.Filled.CompareArrows
            "flight" -> Icons.Default.Flight
            "book" -> Icons.AutoMirrored.Filled.MenuBook
            else -> Icons.Default.Category
        }
    }

    fun getPresetForCategory(name: String): PresetCategoryItem {
        return categoryGridPresets.firstOrNull { it.title.equals(name, ignoreCase = true) }
            ?: PresetCategoryItem(name.lowercase(), name, "category", Color(0xFF78909C))
    }

    val allCategories = listOf(
        ExpenseCategory(
            name = "ALL PRESETS",
            items = categoryGridPresets.map { it.title }
        )
    )

    val allPresetItems: List<String> = categoryGridPresets.map { it.title }

    fun getCombinedCategories(customPresets: List<String>): List<ExpenseCategory> {
        val combinedItems = (categoryGridPresets.map { it.title } + customPresets).distinct()
        return listOf(
            ExpenseCategory(
                name = "ALL PRESETS",
                items = combinedItems
            )
        )
    }

    fun getActiveCategories(
        customPresets: List<String>,
        selectedPresets: Set<String>
    ): List<ExpenseCategory> {
        val allCombined = (categoryGridPresets.map { it.title } + customPresets).distinct()
        val activeItems = allCombined.filter { selectedPresets.contains(it) }
        return if (activeItems.isNotEmpty()) {
            listOf(ExpenseCategory("ACTIVE PRESETS", activeItems))
        } else {
            emptyList()
        }
    }

    fun getCategoryForExpense(description: String): String {
        for (preset in categoryGridPresets) {
            if (preset.title.equals(description, ignoreCase = true) || description.contains(preset.title, ignoreCase = true)) {
                return preset.title
            }
        }
        return "Expenses"
    }
}
