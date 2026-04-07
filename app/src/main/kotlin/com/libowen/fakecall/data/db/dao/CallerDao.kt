package com.libowen.fakecall.data.db.dao

import androidx.room.*
import com.libowen.fakecall.data.db.entity.CallerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallerDao {

    @Query("SELECT * FROM callers ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CallerEntity>>

    @Query("SELECT * FROM callers WHERE isDefault = 1 LIMIT 1")
    fun getDefault(): Flow<CallerEntity?>

    @Upsert
    suspend fun upsert(caller: CallerEntity)

    @Delete
    suspend fun delete(caller: CallerEntity)

    @Query("UPDATE callers SET isDefault = 0")
    suspend fun clearDefault()

    @Query("UPDATE callers SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: String)
}
