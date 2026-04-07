package com.libowen.fakecall.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDefaultDelayMs(): Flow<Long>
    fun getRingtoneUri(): Flow<String?>
    fun getFakeAudioEnabled(): Flow<Boolean>
    fun getScheduled(): Flow<Boolean>
    suspend fun setDefaultDelayMs(delayMs: Long)
    suspend fun setRingtoneUri(uri: String?)
    suspend fun setFakeAudioEnabled(enabled: Boolean)
    suspend fun setScheduled(scheduled: Boolean)
}
