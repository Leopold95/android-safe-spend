package com.alexandr.safespend.data.repository

import com.alexandr.safespend.data.db.DayEntryDao
import com.alexandr.safespend.data.model.AnalyticsData
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.model.DayStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class DayRepository(private val dayEntryDao: DayEntryDao) {

    suspend fun addDay(date: LocalDate, status: DayStatus, note: String = "", resilienceScore: Int? = null) {
        val day = DayEntry(
            date = date.toString(),
            status = status,
            note = note,
            resilienceScore = resilienceScore
        )
        dayEntryDao.insertDay(day)
    }

    suspend fun updateDay(
        id: Int,
        date: LocalDate,
        status: DayStatus,
        note: String = "",
        resilienceScore: Int? = null
    ) {
        val entry = dayEntryDao.getDayById(id) ?: return
        val updated = entry.copy(
            date = date.toString(),
            status = status,
            note = note,
            resilienceScore = resilienceScore
        )
        dayEntryDao.updateDay(updated)
    }

    suspend fun deleteDay(dayEntry: DayEntry) {
        dayEntryDao.deleteDay(dayEntry)
    }

    fun getAllDaysFlow(): Flow<List<DayEntry>> {
        return dayEntryDao.getAllDays()
    }

    fun getDaysByDateRangeFlow(startDate: LocalDate, endDate: LocalDate): Flow<List<DayEntry>> {
        return dayEntryDao.getDaysByDateRange(startDate.toString(), endDate.toString())
    }

    fun searchDaysFlow(query: String): Flow<List<DayEntry>> {
        return dayEntryDao.searchDaysByNote(query)
    }

    suspend fun getDayByDate(date: LocalDate): DayEntry? {
        return dayEntryDao.getDayByDate(date.toString())
    }

    suspend fun getDayById(id: Int): DayEntry? {
        return dayEntryDao.getDayById(id)
    }

    fun getDayByDateFlow(date: LocalDate): Flow<DayEntry?> {
        return dayEntryDao.getDayByDateFlow(date.toString())
    }

    suspend fun deleteAllDays() {
        dayEntryDao.deleteAllDays()
    }

    fun getAnalyticsFlow(): Flow<AnalyticsData> {
        return dayEntryDao.getAllDays().map { days ->
            computeAnalytics(days)
        }
    }

    private fun computeAnalytics(days: List<DayEntry>): AnalyticsData {
        if (days.isEmpty()) return AnalyticsData()

        val sortedByDateDesc = days.sortedByDescending { it.date }
        val sortedByDateAsc = days.sortedBy { it.date }

        val currentStreak = sortedByDateDesc
            .takeWhile { it.status == DayStatus.SAFE }
            .size

        var longestStreak = 0
        var tempStreak = 0
        sortedByDateAsc.forEach { day ->
            if (day.status == DayStatus.SAFE) {
                tempStreak++
                longestStreak = maxOf(longestStreak, tempStreak)
            } else {
                tempStreak = 0
            }
        }

        val safeDays = days.count { it.status == DayStatus.SAFE }
        val overspendDays = days.count { it.status == DayStatus.OVERSPEND }

        return AnalyticsData(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalSafeDays = safeDays,
            totalOverspendDays = overspendDays,
            allDays = sortedByDateDesc
        )
    }
}
