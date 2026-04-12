package com.example.baitapdidongcuoiki.di

import com.example.baitapdidongcuoiki.service.SmsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSmsRepository(): SmsRepository {
        return SmsRepository()
    }
}