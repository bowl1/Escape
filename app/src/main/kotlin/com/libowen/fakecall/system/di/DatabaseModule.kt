package com.libowen.fakecall.system.di

import android.content.Context
import androidx.room.Room
import com.libowen.fakecall.data.db.AppDatabase
import com.libowen.fakecall.data.db.dao.CallerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "fakecall.db")
            .fallbackToDestructiveMigration() // 开发阶段，上线前改为正式 Migration
            .build()

    @Provides
    fun provideCallerDao(db: AppDatabase): CallerDao = db.callerDao()
}
