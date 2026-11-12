package com.moneysight.app.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExpenseListInfo(
    val id: String = "current",
    val name: String = "This Month",
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val amount: Double,
    val category: String = "General",
    val groupId: String = "general",
    val groupName: String = "General",
    val category: String = "Expenses",
    val isPaid: Boolean = false,
    val isStamped: Boolean = false,
    val notes: String = "",
    val dueDate: String = "",
    val listId: String = "current",
    val timestamp: Long = System.currentTimeMillis()
)
