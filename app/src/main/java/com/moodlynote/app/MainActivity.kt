package com.moodlynote.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moodlynote.app.data.db.DatabaseProvider
import com.moodlynote.app.data.preferences.SettingsDataStore
import com.moodlynote.app.data.repository.MoodEntryRepository
import com.moodlynote.app.ui.HomeScreen
import com.moodlynote.app.ui.HomeViewModel
import com.moodlynote.app.ui.SettingsViewModel
import com.moodlynote.app.ui.theme.MoodlyNoteTheme
import com.moodlynote.app.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MoodEntryRepository(DatabaseProvider.getDatabase(this).moodEntryDao())
        val settingsDataStore = SettingsDataStore(this)

        setContent {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repository))
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(settingsDataStore)
            )

            val themeSetting by settingsViewModel.theme.collectAsState()
            val languageSetting by settingsViewModel.language.collectAsState()

            LaunchedEffect(languageSetting) {
                val locales = when (languageSetting) {
                    "de" -> LocaleListCompat.forLanguageTags("de")
                    "en" -> LocaleListCompat.forLanguageTags("en")
                    else -> LocaleListCompat.getEmptyLocaleList()
                }
                AppCompatDelegate.setApplicationLocales(locales)
            }

            val themeMode = remember(themeSetting) { ThemeMode.fromSetting(themeSetting) }

            MoodlyNoteTheme(themeMode = themeMode) {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
