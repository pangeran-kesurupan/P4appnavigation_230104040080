package id.antasari.p4appnavigation_230104040079.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Deklarasikan DataStore di level top (cukup sekali per aplikasi)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    // Definisikan key untuk setiap data yang akan disimpan
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_NIM = stringPreferencesKey("user_nim")
    }

    // Flow untuk membaca data nama secara real-time
    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: ""
        }

    // Flow untuk membaca data NIM secara real-time
    val userNim: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_NIM] ?: ""
        }

    // Fungsi untuk menyimpan data
    suspend fun save(name: String, nim: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.USER_NIM] = nim
        }
    }
}