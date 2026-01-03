package com.moodlynote.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT 50")
    fun observeRecentEntries(): Flow<List<MoodEntry>>

    @Insert
    suspend fun insert(entry: MoodEntry)
}
