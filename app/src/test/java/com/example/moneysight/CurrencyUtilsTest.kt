package com.moneysight.app

import com.moneysight.app.util.CurrencyUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun parseAmount_validNumberString_returnsParsedDouble() {
        assertEquals(2000.0, CurrencyUtils.parseAmount("2000")!!, 0.001)
        assertEquals(500.50, CurrencyUtils.parseAmount("£500.50")!!, 0.001)
        assertEquals(1234.56, CurrencyUtils.parseAmount("1,234.56")!!, 0.001)
    }

    @Test
    fun parseAmount_invalidOrNegativeString_returnsNull() {
        assertNull(CurrencyUtils.parseAmount("invalid"))
        assertNull(CurrencyUtils.parseAmount("-500"))
    }

    @Test
    fun remainingBalanceCalculation_returnsCorrectBalance() {
        val startingAmount = 2000.0
        val expenses = listOf(500.0, 150.0, 50.0)
        val totalExpenses = expenses.sum()
        val remainingBalance = startingAmount - totalExpenses

        assertEquals(1300.0, remainingBalance, 0.001)
    }
}
