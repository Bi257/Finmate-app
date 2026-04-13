package com.example.baitapdidongcuoiki.di

import android.app.Application
import androidx.room.Room
import com.example.baitapdidongcuoiki.api.BinanceApi
import com.example.baitapdidongcuoiki.api.ExchangeApi
import com.example.baitapdidongcuoiki.api.RetrofitInstance
import com.example.baitapdidongcuoiki.data.local.AppDatabase
import com.example.baitapdidongcuoiki.data.local.ExchangeRateDao
import com.example.baitapdidongcuoiki.data.local.SmsMessageDao
import com.example.baitapdidongcuoiki.data.local.TransactionDao
import com.example.baitapdidongcuoiki.data.repository.ExchangeRepository
import com.example.baitapdidongcuoiki.data.repository.SmsRepository
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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ROOM DATABASE
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "finance_db"
        ).fallbackToDestructiveMigration().build()
    }

    // DAOs
    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideExchangeRateDao(db: AppDatabase): ExchangeRateDao = db.exchangeRateDao()

    @Provides
    @Singleton
    fun provideSmsMessageDao(db: AppDatabase): SmsMessageDao = db.smsMessageDao()

    // API
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

    // FIREBASE
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // REPOSITORIES
    @Provides
    @Singleton
    fun provideTransactionRepository(
        dao: TransactionDao,
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): TransactionRepository {
        return TransactionRepositoryImpl(dao, firestore, auth)
    }

    @Provides
    @Singleton
    fun provideExchangeRepository(api: ExchangeApi, dao: ExchangeRateDao): ExchangeRepository {
        return ExchangeRepository(api, dao)
    }

    @Provides
    @Singleton
    fun provideSmsRepository(dao: SmsMessageDao): SmsRepository {
        return SmsRepository(dao)
    }

    // USE CASES
    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(repository: TransactionRepository): GetTransactionsUseCase {
        return GetTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(repository: TransactionRepository): AddTransactionUseCase {
        return AddTransactionUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateTaxUseCase(): CalculateTaxUseCase {
        return CalculateTaxUseCase()
    }

    @Provides
    @Singleton
    fun provideRefreshTransactionsUseCase(repository: TransactionRepository): RefreshTransactionsUseCase {
        return RefreshTransactionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        getTransactionsUseCase: GetTransactionsUseCase,
        addTransactionUseCase: AddTransactionUseCase,
        calculateTaxUseCase: CalculateTaxUseCase,
        refreshTransactionsUseCase: RefreshTransactionsUseCase
    ): UseCases {
        return UseCases(
            getTransactionsUseCase = getTransactionsUseCase,
            addTransactionUseCase = addTransactionUseCase,
            calculateTaxUseCase = calculateTaxUseCase,
            refreshTransactionsUseCase = refreshTransactionsUseCase
        )
    }
}