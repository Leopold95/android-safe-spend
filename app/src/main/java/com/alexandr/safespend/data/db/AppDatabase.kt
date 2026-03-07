package com.alexandr.safespend.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexandr.safespend.data.model.DayEntry

@Database(
    entities = [DayEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dayEntryDao(): DayEntryDao
}

