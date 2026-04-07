package com.libowen.fakecall.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.libowen.fakecall.data.db.dao.CallerDao
import com.libowen.fakecall.data.db.entity.CallerEntity

@Database(
    entities = [CallerEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun callerDao(): CallerDao
}
