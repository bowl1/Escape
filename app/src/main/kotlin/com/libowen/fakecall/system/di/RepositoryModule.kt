package com.libowen.fakecall.system.di

import com.libowen.fakecall.data.repository.CallerRepositoryImpl
import com.libowen.fakecall.data.repository.SettingsRepositoryImpl
import com.libowen.fakecall.domain.repository.CallerRepository
import com.libowen.fakecall.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCallerRepository(
        impl: CallerRepositoryImpl
    ): CallerRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
