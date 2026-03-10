package com.alexandr.safespend.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alexandr.safespend.data.model.DayEntry
import com.alexandr.safespend.data.model.ResilienceCheck

@Database(
    entities = [DayEntry::class, ResilienceCheck::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dayEntryDao(): DayEntryDao
    abstract fun resilienceCheckDao(): ResilienceCheckDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `resilience_checks` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `date` TEXT NOT NULL,
                        `score` INTEGER NOT NULL,
                        `note` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_resilience_checks_date` ON `resilience_checks` (`date`)"
                )
            }
        }
    }
}
