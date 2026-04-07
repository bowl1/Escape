package com.libowen.fakecall.data.repository

import com.libowen.fakecall.data.db.dao.CallerDao
import com.libowen.fakecall.data.mapper.toDomain
import com.libowen.fakecall.data.mapper.toEntity
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.domain.repository.CallerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallerRepositoryImpl @Inject constructor(
    private val dao: CallerDao
) : CallerRepository {

    override fun getAll(): Flow<List<CallerInfo>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getDefault(): Flow<CallerInfo?> =
        dao.getDefault().map { it?.toDomain() }

    override suspend fun save(caller: CallerInfo) {
        dao.upsert(caller.toEntity())
    }

    override suspend fun delete(caller: CallerInfo) {
        dao.delete(caller.toEntity())
    }

    override suspend fun setDefault(id: String) {
        dao.clearDefault()
        dao.setDefault(id)
    }

    override suspend fun clearDefault() {
        dao.clearDefault()
    }
}
