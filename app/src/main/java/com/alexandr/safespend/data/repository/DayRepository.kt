package com.alexandr.safespend.data.repository

import com.alexandr.safespend.data.db.DayEntryDao
import com.alexandr.safespend.data.db.ResilienceCheckDao
import com.alexandr.safespend.data.model.AnalyticsData
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.model.DayStatus
import com.alexandr.safespend.data.model.ResilienceChartPoint
import com.alexandr.safespend.data.model.ResilienceCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import kotlin.math.roundToInt

class DayRepository(
    private val dayEntryDao: DayEntryDao,
    private val resilienceCheckDao: ResilienceCheckDao
) {

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
        resilienceCheckDao.deleteAll()
    }

    suspend fun saveResilienceCheck(date: LocalDate, score: Int, note: String = "") {
        val existing = resilienceCheckDao.getByDate(date.toString())
        val normalized = score.coerceIn(0, 100)
        if (existing == null) {
            resilienceCheckDao.insert(
                ResilienceCheck(
                    date = date.toString(),
                    score = normalized,
                    note = note
                )
            )
        } else {
            resilienceCheckDao.update(
                existing.copy(
                    score = normalized,
                    note = note
                )
            )
        }
    }

    fun getAnalyticsFlow(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): Flow<AnalyticsData> {
        val dayFlow = if (startDate != null && endDate != null) {
            dayEntryDao.getDaysByDateRange(startDate.toString(), endDate.toString())
        } else {
            dayEntryDao.getAllDays()
        }

        val resilienceFlow = if (startDate != null && endDate != null) {
            resilienceCheckDao.getByDateRange(startDate.toString(), endDate.toString())
        } else {
            resilienceCheckDao.getAll()
        }

        return combine(dayFlow, resilienceFlow) { days, checks ->
            computeAnalytics(days = days, checks = checks)
        }
    }

    private fun computeAnalytics(days: List<DayEntry>, checks: List<ResilienceCheck>): AnalyticsData {
        if (days.isEmpty() && checks.isEmpty()) return AnalyticsData()

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

        val resilienceByDate = linkedMapOf<String, Int>()
        days
            .asSequence()
            .filter { it.resilienceScore != null }
            .sortedBy { it.date }
            .forEach { day ->
                resilienceByDate[day.date] = day.resilienceScore ?: return@forEach
            }
        checks
            .sortedBy { it.date }
            .forEach { check ->
                resilienceByDate[check.date] = check.score
            }

        val resiliencePoints = resilienceByDate
            .map { (date, score) -> ResilienceChartPoint(date = date, score = score) }
        val averageResilience = resiliencePoints
            .map { it.score }
            .takeIf { it.isNotEmpty() }
            ?.average()
            ?.roundToInt()
        val latestResilience = resiliencePoints.lastOrNull()?.score

        return AnalyticsData(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalSafeDays = safeDays,
            totalOverspendDays = overspendDays,
            allDays = sortedByDateDesc,
            resiliencePoints = resiliencePoints,
            averageResilienceScore = averageResilience,
            latestResilienceScore = latestResilience
        )
    }
}
