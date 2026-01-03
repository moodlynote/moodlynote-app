package com.moodlynote.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moodlynote.app.data.db.MoodEntry
import com.moodlynote.app.data.repository.MoodEntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val mood: Double = 5.0,
    val energy: Int = 50,
    val note: String = ""
)

class HomeViewModel(private val repository: MoodEntryRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val recentEntries = repository.observeRecentEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun updateMood(value: Double) {
        _uiState.value = _uiState.value.copy(mood = value)
    }

    fun updateEnergy(value: Int) {
        _uiState.value = _uiState.value.copy(energy = value)
    }

    fun updateNote(value: String) {
        _uiState.value = _uiState.value.copy(note = value)
    }

    fun saveEntry() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.addEntry(
                MoodEntry(
                    timestamp = System.currentTimeMillis(),
                    mood = state.mood,
                    energy = state.energy,
                    note = state.note.takeIf { it.isNotBlank() }
                )
            )
        }
    }

    class Factory(private val repository: MoodEntryRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
