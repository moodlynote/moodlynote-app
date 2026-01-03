# MoodlyNote

MoodlyNote ist ein sanfter, offline-first Mood- und Energie-Tracker für Android. Dieses Repository enthält ein lauffähiges Kotlin/Jetpack-Compose-Grundgerüst, das lokale, verschlüsselte Datenspeicherung und eine extrem schnelle Mood-Eingabe priorisiert.

## MVP-Status (Grundgerüst)
- ✅ **Offline first**: Alle Daten werden lokal gespeichert, keine Netzwerkanforderungen.
- ✅ **Sicherheit**: Room + SQLCipher, Passphrase in `EncryptedSharedPreferences` via Android Keystore.
- ✅ **Schnelle Mood-Eingabe**: Mood-Slider 1.0–10.0 (0.1-Schritte) und optionaler Energie-Slider.
- ✅ **Lokalisierung**: Deutsch + Englisch, in der App umschaltbar.
- ✅ **Ruhiges UI**: Soft/Normal/Dark Theme mit sanften Farben.

## Architekturentscheidungen
- **Kotlin + Jetpack Compose** (UI), **MVVM** (State + ViewModel), **Room** (Storage).
- **SQLCipher** für verschlüsselte SQLite-Datenbank (lokal, offline). Passphrase wird sicher im Android Keystore abgelegt.
- **DataStore** für Settings (Sprache/Theme).

## Projektstruktur (Auszug)
- `app/src/main/java/com/moodlynote/app/MainActivity.kt`
- `app/src/main/java/com/moodlynote/app/ui/HomeScreen.kt`
- `app/src/main/java/com/moodlynote/app/data/db/*`
- `app/src/main/java/com/moodlynote/app/data/preferences/SettingsDataStore.kt`

## Lokalisierung
Alle UI-Texte liegen in `strings.xml` (EN) und `values-de/strings.xml` (DE). Die Sprache kann in den Einstellungen in der App gewechselt werden.

## Sicherheit & Datenschutz
- **Keine Tracker, keine Werbung, kein Analytics SDK**.
- **Lokale, verschlüsselte Datenbank** via SQLCipher.
- **Keine Cloud** / kein Netzwerk notwendig.

## Build & Run
Da in dieser Umgebung kein Gradle Wrapper hinterlegt ist, bitte Gradle lokal verwenden:

```bash
gradle :app:assembleDebug
```

Öffne das Projekt in Android Studio (Giraffe/Koala oder neuer) und starte die App auf einem Emulator oder Gerät.

## Nächste MVP-Schritte (geplant)
- PIN-Sperre (3–6 Stellen) + optional Biometrie.
- Schlafzeiten & Reminder-Logik (WorkManager/AlarmManager).
- Verlauf/Analyse-Screens + PDF-Export.
- Backup/Restore (verschlüsselt, lokal/Datei-Share).

## Kontakt & Spenden
- Kontakt: moodlynote@gmail.com
- Spenden: https://ko-fi.com/moodlynote (externer Link, kein In-App-Kauf)
