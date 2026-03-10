package com.alexandr.safespend.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "resilience_checks",
    indices = [Index(value = ["date"], unique = true)]
)
data class ResilienceCheck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val score: Int,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class ResilienceChartPoint(
    val date: String,
    val score: Int
)

