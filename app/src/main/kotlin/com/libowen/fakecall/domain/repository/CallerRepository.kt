package com.libowen.fakecall.domain.repository

import com.libowen.fakecall.domain.model.CallerInfo
import kotlinx.coroutines.flow.Flow

interface CallerRepository {
    fun getAll(): Flow<List<CallerInfo>>
    fun getDefault(): Flow<CallerInfo?>
    suspend fun save(caller: CallerInfo)
    suspend fun delete(caller: CallerInfo)
    suspend fun setDefault(id: String)
    suspend fun clearDefault()
}
