package com.moodlynote.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "mood") val mood: Double,
    @ColumnInfo(name = "energy") val energy: Int?,
    @ColumnInfo(name = "note") val note: String?
)
