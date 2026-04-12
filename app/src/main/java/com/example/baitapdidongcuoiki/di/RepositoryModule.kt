package com.example.baitapdidongcuoiki.di

import com.example.baitapdidongcuoiki.data.repository.AuthRepository // Nhớ kiểm tra đúng đường dẫn này
import com.example.baitapdidongcuoiki.service.SmsRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepository(auth)
    }

    @Provides
    @Singleton
    fun provideSmsRepository(): SmsRepository {
        return SmsRepository()
    }
}