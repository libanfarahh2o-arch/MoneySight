package com.moneysight.app.util

import java.util.Calendar

sealed class DueDateStatus {
    object Today : DueDateStatus()
    data class DaysRemaining(val days: Int) : DueDateStatus()
    data class Overdue(val days: Int) : DueDateStatus()
    object Invalid : DueDateStatus()
}

object DateUtils {
    /**
     * Calculates days left to make payment based on due date string (e.g. "15th", "1st", "28").
     */
    fun calculateDueDateStatus(dueDateStr: String, today: Calendar = Calendar.getInstance()): DueDateStatus {
        if (dueDateStr.isBlank()) return DueDateStatus.Invalid

        val numericOnly = dueDateStr.replace(Regex("[^0-9]"), "")
        val dayOfMonth = numericOnly.toIntOrNull()

        if (dayOfMonth != null && dayOfMonth in 1..31) {
            val currentDay = today.get(Calendar.DAY_OF_MONTH)
            val maxDaysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH)
            val targetDay = dayOfMonth.coerceAtMost(maxDaysInMonth)

            return when {
                targetDay == currentDay -> DueDateStatus.Today
                targetDay > currentDay -> DueDateStatus.DaysRemaining(targetDay - currentDay)
                else -> DueDateStatus.Overdue(currentDay - targetDay)
            }
        }

        return DueDateStatus.Invalid
    }

    /**
     * Checks if a recurring expense has reached or passed its recurrence end date
     * relative to the selected calendar year and month (0-11).
     */
    fun hasRecurrenceStopped(recurrenceEnd: String, recurrenceEndDate: String, selectedYear: Int, selectedMonth: Int): Boolean {
        if (recurrenceEnd != "Stops on Date" || recurrenceEndDate.isBlank()) return false
        val cleaned = recurrenceEndDate.trim()
        val parts = cleaned.split(Regex("[/\\-]"))
        if (parts.size == 2) {
            val p1 = parts[0].toIntOrNull() ?: return false
            val p2 = parts[1].toIntOrNull() ?: return false
            val stopYear: Int
            val stopMonth: Int // 0-11
            if (p1 > 12 || p2 > 31) {
                stopYear = p1
                stopMonth = p2 - 1
            } else if (p2 > 12) {
                stopMonth = p1 - 1
                stopYear = p2
            } else {
                stopMonth = p1 - 1
                stopYear = if (p2 < 100) 2000 + p2 else p2
            }
            val currentTotalMonths = selectedYear * 12 + selectedMonth
            val stopTotalMonths = stopYear * 12 + stopMonth
            return currentTotalMonths > stopTotalMonths
        }
        return false
    }

    /**
     * Validates DD MM YYYY stop date format.
     */
    fun isValidStopDate(dateStr: String): Boolean {
        if (dateStr.isBlank()) return false
        val parts = dateStr.split("/", " ", "-")
        if (parts.size < 3) return false
        val day = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val year = parts[2].toIntOrNull() ?: return false
        return day in 1..31 && month in 1..12 && year in 2020..2100
    }
}
