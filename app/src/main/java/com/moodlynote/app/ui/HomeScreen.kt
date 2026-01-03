package com.moodlynote.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moodlynote.app.R
import com.moodlynote.app.data.db.MoodEntry
import com.moodlynote.app.ui.theme.ThemeMode
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val entries by homeViewModel.recentEntries.collectAsState()

    var showSettings by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.home_title)) },
            actions = {
                IconButton(onClick = { showSettings = true }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MoodSlider(
                mood = uiState.mood,
                onMoodChange = homeViewModel::updateMood
            )

            EnergySlider(
                energy = uiState.energy,
                onEnergyChange = homeViewModel::updateEnergy
            )

            OutlinedTextField(
                value = uiState.note,
                onValueChange = homeViewModel::updateNote,
                label = { Text(text = stringResource(R.string.note_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = homeViewModel::saveEntry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.save_entry))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        RecentEntries(entries = entries)
    }

    if (showSettings) {
        SettingsDialog(
            settingsViewModel = settingsViewModel,
            onDismiss = { showSettings = false }
        )
    }
}

@Composable
private fun MoodSlider(
    mood: Double,
    onMoodChange: (Double) -> Unit
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }
    formatter.minimumFractionDigits = 1
    formatter.maximumFractionDigits = 1

    Column {
        Text(
            text = stringResource(R.string.mood_label),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.mood_value, formatter.format(mood)),
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = mood.toFloat(),
            onValueChange = { value ->
                val rounded = kotlin.math.round(value * 10) / 10.0
                onMoodChange(rounded)
            },
            valueRange = 1f..10f,
            steps = 89
        )
    }
}

@Composable
private fun EnergySlider(
    energy: Int,
    onEnergyChange: (Int) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.energy_label),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.energy_value, energy),
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = energy.toFloat(),
            onValueChange = { value -> onEnergyChange(value.toInt()) },
            valueRange = 0f..100f,
            steps = 99
        )
    }
}

@Composable
private fun RecentEntries(entries: List<MoodEntry>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.recent_entries),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (entries.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_entries),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entries) { entry ->
                EntryCard(entry = entry)
            }
        }
    }
}

@Composable
private fun EntryCard(entry: MoodEntry) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }
    formatter.minimumFractionDigits = 1
    formatter.maximumFractionDigits = 1

    val timestamp = remember(entry.timestamp) {
        val instant = Instant.ofEpochMilli(entry.timestamp)
        val formatterDate = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm", Locale.getDefault())
        formatterDate.format(instant.atZone(ZoneId.systemDefault()))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = timestamp, fontWeight = FontWeight.SemiBold)
            Text(text = stringResource(R.string.mood_value, formatter.format(entry.mood)))
            entry.energy?.let { energy ->
                Text(text = stringResource(R.string.energy_value, energy))
            }
            entry.note?.let { note ->
                Text(text = note)
            }
        }
    }
}

@Composable
private fun SettingsDialog(
    settingsViewModel: SettingsViewModel,
    onDismiss: () -> Unit
) {
    val language by settingsViewModel.language.collectAsState()
    val theme by settingsViewModel.theme.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.done))
            }
        },
        title = { Text(text = stringResource(R.string.settings_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = stringResource(R.string.language_label), fontWeight = FontWeight.SemiBold)
                LanguageOption(
                    selected = language == "system",
                    onSelected = { settingsViewModel.setLanguage("system") },
                    label = stringResource(R.string.language_system)
                )
                LanguageOption(
                    selected = language == "de",
                    onSelected = { settingsViewModel.setLanguage("de") },
                    label = stringResource(R.string.language_german)
                )
                LanguageOption(
                    selected = language == "en",
                    onSelected = { settingsViewModel.setLanguage("en") },
                    label = stringResource(R.string.language_english)
                )

                Text(text = stringResource(R.string.theme_label), fontWeight = FontWeight.SemiBold)
                ThemeOption(
                    selected = theme == ThemeMode.Soft.value,
                    onSelected = { settingsViewModel.setTheme(ThemeMode.Soft.value) },
                    label = stringResource(R.string.theme_soft)
                )
                ThemeOption(
                    selected = theme == ThemeMode.Normal.value,
                    onSelected = { settingsViewModel.setTheme(ThemeMode.Normal.value) },
                    label = stringResource(R.string.theme_normal)
                )
                ThemeOption(
                    selected = theme == ThemeMode.Dark.value,
                    onSelected = { settingsViewModel.setTheme(ThemeMode.Dark.value) },
                    label = stringResource(R.string.theme_dark)
                )
            }
        }
    )
}

@Composable
private fun LanguageOption(selected: Boolean, onSelected: () -> Unit, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onSelected)
        Text(text = label)
    }
}

@Composable
private fun ThemeOption(selected: Boolean, onSelected: () -> Unit, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onSelected)
        Text(text = label)
    }
}
