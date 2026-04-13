package com.example.baitapdidongcuoiki.di

import android.app.Application
import androidx.room.Room
import com.example.baitapdidongcuoiki.api.BinanceApi
import com.example.baitapdidongcuoiki.api.ExchangeApi
import com.example.baitapdidongcuoiki.api.RetrofitInstance
import com.example.baitapdidongcuoiki.data.local.AppDatabase
import com.example.baitapdidongcuoiki.data.local.ExchangeRateDao
import com.example.baitapdidongcuoiki.data.local.TransactionDao
import com.example.baitapdidongcuoiki.data.repository.TransactionRepositoryImpl
import com.example.baitapdidongcuoiki.domain.repository.TransactionRepository
import com.example.baitapdidongcuoiki.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.example.baitapdidongcuoiki.data.repository.ExchangeRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //ROOM DATABASE
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "finance_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    //DAO
    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()

    @Provides
    @Singleton
    fun provideExchangeRateDao(db: AppDatabase): ExchangeRateDao = db.exchangeRateDao()

    @Provides
    @Singleton
    fun provideExchangeApi(): ExchangeApi = RetrofitInstance.api

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val userAgent = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .header("User-Agent", "Baitapdidongcuoiki/1.0 (Android)")
                .build()
            chain.proceed(req)
        }
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(userAgent)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideBinanceApi(client: OkHttpClient): BinanceApi {
        return Retrofit.Builder()
            .baseUrl("https://api.binance.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceApi::class.java)
    }

    //FIREBASE
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    //REPOSITORY
    @Provides
    @Singleton
    fun provideRepository(
        dao: TransactionDao,
        firestore: FirebaseFirestore, // Hilt sẽ lấy từ provideFirestore ở trên
        auth: FirebaseAuth            // Hilt sẽ lấy từ provideAuth ở trên
    ): TransactionRepository {
        return TransactionRepositoryImpl(dao, firestore, auth)
    }

    //USE CASES
    @Provides
    @Singleton
    fun provideUseCases(
        repository: TransactionRepository
    ): UseCases {
        return UseCases(
            getTransactionsUseCase = GetTransactionsUseCase(repository),
            addTransactionUseCase = AddTransactionUseCase(repository),
            calculateTaxUseCase = CalculateTaxUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideExchangeRepository(api: ExchangeApi, dao: ExchangeRateDao): ExchangeRepository {
        return ExchangeRepository(api, dao) 
    }
}