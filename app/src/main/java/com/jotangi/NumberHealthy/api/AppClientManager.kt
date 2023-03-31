package com.jotangi.NumberHealthy.api

import android.util.Log
import com.jotangi.NumberHealthy.api.book.BookApiService
import com.jotangi.NumberHealthy.api.watch.WatchApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

class AppClientManager private constructor() {

    lateinit var watchService: WatchApiService
    lateinit var bookService: BookApiService

    companion object {
        val instance by lazy { AppClientManager() }
    }

    private var logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            try {
                Log.i("API msg", "${URLDecoder.decode(message, Charsets.UTF_8.toString())}")
            } catch (e: Exception) {
                Log.i("API msg", message)
            }
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var okHttpClient = OkHttpClient().newBuilder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(logging).build()

    fun init() {

        val watchRetrofit = Retrofit.Builder()
            .baseUrl(ApiConstant.WATCH_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        watchService = watchRetrofit.create(WatchApiService::class.java)

        val bookRetrofit = Retrofit.Builder()
            .baseUrl(ApiConstant.BOOK_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        bookService = bookRetrofit.create(BookApiService::class.java)
    }
}