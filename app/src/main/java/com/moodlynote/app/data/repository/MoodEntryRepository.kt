package com.moodlynote.app.data.repository

import com.moodlynote.app.data.db.MoodEntry
import com.moodlynote.app.data.db.MoodEntryDao
import kotlinx.coroutines.flow.Flow

class MoodEntryRepository(private val dao: MoodEntryDao) {
    fun observeRecentEntries(): Flow<List<MoodEntry>> = dao.observeRecentEntries()

    suspend fun addEntry(entry: MoodEntry) {
        dao.insert(entry)
    }
}
