package com.libowen.fakecall.data.repository

import com.libowen.fakecall.data.datastore.SettingsDataStore
import com.libowen.fakecall.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : SettingsRepository {

    override fun getDefaultDelayMs(): Flow<Long> = dataStore.defaultDelayMs
    override fun getRingtoneUri(): Flow<String?> = dataStore.ringtoneUri
    override fun getFakeAudioEnabled(): Flow<Boolean> = dataStore.fakeAudioEnabled
    override fun getScheduled(): Flow<Boolean> = dataStore.isScheduled

    override suspend fun setDefaultDelayMs(delayMs: Long) =
        dataStore.setDefaultDelayMs(delayMs)

    override suspend fun setRingtoneUri(uri: String?) =
        dataStore.setRingtoneUri(uri)

    override suspend fun setFakeAudioEnabled(enabled: Boolean) =
        dataStore.setFakeAudioEnabled(enabled)

    override suspend fun setScheduled(scheduled: Boolean) =
        dataStore.setScheduled(scheduled)
}
