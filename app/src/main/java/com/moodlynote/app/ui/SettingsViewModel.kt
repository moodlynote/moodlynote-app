package com.moodlynote.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moodlynote.app.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: SettingsDataStore) : ViewModel() {
    val language: StateFlow<String> = dataStore.language
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), "system")

    val theme: StateFlow<String> = dataStore.theme
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), "soft")

    fun setLanguage(value: String) {
        viewModelScope.launch {
            dataStore.setLanguage(value)
        }
    }

    fun setTheme(value: String) {
        viewModelScope.launch {
            dataStore.setTheme(value)
        }
    }

    class Factory(private val dataStore: SettingsDataStore) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(dataStore) as T
        }
    }
}
