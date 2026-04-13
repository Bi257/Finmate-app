package com.example.baitapdidongcuoiki.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {



    private const val BASE_URL = "https://api.exchangerate.host/"

    private val userAgent = Interceptor { chain ->
        val req = chain.request().newBuilder()
            .header("User-Agent", "Baitapdidongcuoiki/1.0 (Android)")
            .build()
        chain.proceed(req)
    }


    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(userAgent)
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ExchangeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeApi::class.java)
    }

}
