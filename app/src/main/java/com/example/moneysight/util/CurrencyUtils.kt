package com.moneysight.app.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val ukLocale = Locale.UK
    private val currencyFormat = NumberFormat.getCurrencyInstance(ukLocale)

    /**
     * Formats a double value to UK British Pounds string (£1,300.00 or -£500.00).
     */
    fun formatPounds(amount: Double): String {
        return currencyFormat.format(amount)
    }

    /**
     * Parses string input to Double safely. Returns null if invalid or negative.
     */
    fun parseAmount(input: String): Double? {
        val cleanInput = input.trim().replace("£", "").replace(",", "")
        val value = cleanInput.toDoubleOrNull() ?: return null
        return if (value >= 0) value else null
    }
}
