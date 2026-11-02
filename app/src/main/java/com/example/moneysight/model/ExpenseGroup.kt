package com.moneysight.app.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExpenseGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String
) {
    companion object {
        val DEFAULT_GENERAL = ExpenseGroup(id = "general", name = "General")
        val DEFAULT_HOUSE = ExpenseGroup(id = "house", name = "House Expenses")
    }
}
