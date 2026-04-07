package com.libowen.fakecall.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fakecall_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    object Keys {
        val DEFAULT_DELAY_MS = longPreferencesKey("default_delay_ms")
        val RINGTONE_URI = stringPreferencesKey("ringtone_uri")
        val FAKE_AUDIO_ON = booleanPreferencesKey("fake_audio_on")
        val IS_SCHEDULED = booleanPreferencesKey("is_scheduled")
    }

    val defaultDelayMs: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[Keys.DEFAULT_DELAY_MS] ?: (5 * 60 * 1000L) // 默认 5 分钟
    }

    val ringtoneUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[Keys.RINGTONE_URI]
    }

    val fakeAudioEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.FAKE_AUDIO_ON] ?: false
    }

    val isScheduled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_SCHEDULED] ?: false
    }

    suspend fun setDefaultDelayMs(delayMs: Long) {
        context.dataStore.edit { it[Keys.DEFAULT_DELAY_MS] = delayMs }
    }

    suspend fun setRingtoneUri(uri: String?) {
        context.dataStore.edit { prefs ->
            if (uri != null) prefs[Keys.RINGTONE_URI] = uri
            else prefs.remove(Keys.RINGTONE_URI)
        }
    }

    suspend fun setFakeAudioEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.FAKE_AUDIO_ON] = enabled }
    }

    suspend fun setScheduled(scheduled: Boolean) {
        context.dataStore.edit { it[Keys.IS_SCHEDULED] = scheduled }
    }
}
