package com.alexandr.safespend.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val displayInput = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayOutput = SimpleDateFormat("dd MMM yyyy", Locale.US)

    fun LocalDate.formatDisplay(): String {
        return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US))
    }

    fun formatIsoDateForDisplay(isoDate: String): String {
        return runCatching {
            val parsed = displayInput.parse(isoDate) ?: return@runCatching isoDate
            displayOutput.format(parsed)
        }.getOrDefault(isoDate)
    }

    fun formatIsoDateShort(isoDate: String): String {
        return runCatching {
            val parsed = displayInput.parse(isoDate) ?: return@runCatching isoDate
            SimpleDateFormat("dd MMM", Locale.US).format(parsed)
        }.getOrDefault(isoDate)
    }

    fun String.parseToLocalDate(): LocalDate {
        return LocalDate.parse(this, isoFormatter)
    }

    fun isIsoOnOrAfter(isoDate: String, startIsoDate: String): Boolean {
        // ISO yyyy-MM-dd can be safely compared lexicographically.
        return isoDate >= startIsoDate
    }

    fun getWeekStartDate(): LocalDate {
        return LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong() - 1)
    }

    fun getMonthStartDate(): LocalDate {
        return LocalDate.now().withDayOfMonth(1)
    }

    fun getYearStartDate(): LocalDate {
        return LocalDate.now().withDayOfYear(1)
    }
}
