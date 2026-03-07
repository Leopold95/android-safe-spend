package com.alexandr.safespend.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "days")
data class DayEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String, // ISO 8601 format: YYYY-MM-DD
    val status: DayStatus,
    val note: String = "",
    val resilienceScore: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
enum class DayStatus {
    SAFE,
    OVERSPEND
}

//@Serializable
//data class DayUiModel(
//    val id: Int,
//    val date: LocalDate,
//    val status: DayStatus,
//    val note: String = "",
//    val resilienceScore: Int? = null
//)

@Serializable
data class AnalyticsData(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalSafeDays: Int = 0,
    val totalOverspendDays: Int = 0,
    val allDays: List<DayEntry> = emptyList()
)

