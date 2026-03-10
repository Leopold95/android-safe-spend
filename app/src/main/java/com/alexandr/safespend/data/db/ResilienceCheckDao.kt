package com.alexandr.safespend.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexandr.safespend.data.model.ResilienceCheck
import kotlinx.coroutines.flow.Flow

@Dao
interface ResilienceCheckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(check: ResilienceCheck): Long

    @Update
    suspend fun update(check: ResilienceCheck)

    @Query("SELECT * FROM resilience_checks WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): ResilienceCheck?

    @Query("SELECT * FROM resilience_checks ORDER BY date ASC")
    fun getAll(): Flow<List<ResilienceCheck>>

    @Query("SELECT * FROM resilience_checks WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<ResilienceCheck>>

    @Query("DELETE FROM resilience_checks")
    suspend fun deleteAll()
}
