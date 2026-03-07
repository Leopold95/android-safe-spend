package com.alexandr.safespend.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexandr.safespend.data.model.DayEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DayEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: DayEntry): Long

    @Update
    suspend fun updateDay(day: DayEntry)

    @Delete
    suspend fun deleteDay(day: DayEntry)

    @Query("SELECT * FROM days WHERE date = :date")
    suspend fun getDayByDate(date: String): DayEntry?

    @Query("SELECT * FROM days WHERE id = :id")
    suspend fun getDayById(id: Int): DayEntry?

    @Query("SELECT * FROM days WHERE date = :date")
    fun getDayByDateFlow(date: String): Flow<DayEntry?>

    @Query("SELECT * FROM days ORDER BY date DESC")
    fun getAllDays(): Flow<List<DayEntry>>

    @Query("SELECT * FROM days WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getDaysByDateRange(startDate: String, endDate: String): Flow<List<DayEntry>>

    @Query("SELECT * FROM days WHERE note LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchDaysByNote(query: String): Flow<List<DayEntry>>

    @Query("DELETE FROM days")
    suspend fun deleteAllDays()

    @Query("SELECT COUNT(*) FROM days")
    fun getDayCount(): Flow<Int>
}

