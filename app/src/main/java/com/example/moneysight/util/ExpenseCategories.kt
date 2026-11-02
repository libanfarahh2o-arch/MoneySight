package com.moneysight.app.util

data class ExpenseCategory(
    val name: String,
    val items: List<String>
)

object ExpenseCategories {

    val popularSuggestions = listOf(
        "Rent",
        "Food shopping",
        "Fuel",
        "Electricity",
        "Gas",
        "Mobile Phone Contract",
        "Home Broadband / Wi-Fi",
        "Council Tax",
        "Car Insurance",
        "Netflix"
    )

    val allCategories = listOf(
        ExpenseCategory(
            name = "HOUSEHOLD BILLS",
            items = listOf(
                "Rent",
                "Mortgage",
                "Council Tax",
                "Electricity",
                "Gas",
                "Water",
                "Waste Collection"
            )
        ),
        ExpenseCategory(
            name = "PHONE & INTERNET",
            items = listOf(
                "Mobile Phone Contract",
                "Home Broadband / Wi-Fi",
                "TV Licence"
            )
        ),
        ExpenseCategory(
            name = "TRANSPORT & VEHICLE",
            items = listOf(
                "Car Finance",
                "Car Insurance",
                "Vehicle Tax (Road Tax)",
                "MOT",
                "Car Servicing",
                "Fuel",
                "Travel Pass",
                "Public Transport"
            )
        ),
        ExpenseCategory(
            name = "INSURANCE",
            items = listOf(
                "Life Insurance",
                "Home Contents Insurance",
                "Buildings Insurance",
                "Health Insurance",
                "Pet Insurance",
                "Income Protection Insurance"
            )
        ),
        ExpenseCategory(
            name = "SUBSCRIPTIONS & MEMBERSHIPS",
            items = listOf(
                "Netflix",
                "Disney+",
                "Amazon Prime",
                "Spotify",
                "Apple Music",
                "Cloud Storage (Google One / iCloud)",
                "App Subscriptions",
                "Gym Membership"
            )
        ),
        ExpenseCategory(
            name = "LOANS, CREDIT & REPAYMENTS",
            items = listOf(
                "Personal Loan",
                "Credit Card Repayment",
                "Buy Now, Pay Later",
                "Overdraft Repayment",
                "Student Loan",
                "Family or Friends Loan Repayment"
            )
        ),
        ExpenseCategory(
            name = "FAMILY & EDUCATION",
            items = listOf(
                "School Meals",
                "School Clubs",
                "Tuition Fees",
                "Madrasah Fees",
                "Nursery Fees",
                "Childcare Fees",
                "Child Maintenance"
            )
        ),
        ExpenseCategory(
            name = "OTHER REGULAR PAYMENTS",
            items = listOf(
                "Charity Donations",
                "Mosque Donations",
                "Pension Contributions",
                "Savings Standing Order",
                "Accountant Fees",
                "Professional Memberships"
            )
        )
    )

    val allPresetItems: List<String> = allCategories.flatMap { it.items }.distinct()

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
     * Returns only the categories that contain at least one item present in [selectedPresets].
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
        for (category in allCategories) {
            if (category.items.any { it.equals(description, ignoreCase = true) || description.contains(it, ignoreCase = true) }) {
                return category.name
            }
        }
        return "OTHER"
    }
}
