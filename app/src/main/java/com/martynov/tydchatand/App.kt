package com.martynov.tydchatand

import android.app.Application
import android.content.Context
import android.util.Log
import com.martynov.tydchatand.api.API
import com.martynov.tydchatand.api.interceptor.InjectAuthTokenInterceptor
import com.martynov.tydchatand.repository.NetworkRepository
import com.martynov.tydchatand.repository.Repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    companion object {
        lateinit var repository: Repository
            private set

    }
    override fun onCreate() {
        super.onCreate()
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor {
                getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(
                    AUTHENTICATED_SHARED_KEY, null
                )
            })
            .addInterceptor(httpLoggerInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("$BASE_URL/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(API::class.java)
        repository = NetworkRepository(api)
        Log.d("MY", "Отработало")
    }


}