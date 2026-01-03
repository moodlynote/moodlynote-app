package com.moodlynote.app.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    private val languageKey = stringPreferencesKey("language")
    private val themeKey = stringPreferencesKey("theme")

    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[languageKey] ?: "system"
    }

    val theme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[themeKey] ?: "soft"
    }

    suspend fun setLanguage(value: String) {
        context.dataStore.edit { prefs ->
            prefs[languageKey] = value
        }
    }

    suspend fun setTheme(value: String) {
        context.dataStore.edit { prefs ->
            prefs[themeKey] = value
        }
    }
}
