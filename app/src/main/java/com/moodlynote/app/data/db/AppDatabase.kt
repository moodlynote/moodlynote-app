package com.moodlynote.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MoodEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodEntryDao(): MoodEntryDao
}
