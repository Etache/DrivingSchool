package com.example.drivingschool.data.remote

import android.content.Context
import com.example.drivingschool.data.remote.change_password.PasswordApiService
import com.example.drivingschool.data.remote.detail.DetailsApiService
import com.example.drivingschool.data.remote.login.LoginApiService
import com.example.drivingschool.data.remote.login.LoginInterceptor
import com.example.drivingschool.data.remote.main.MainApiService
import com.example.drivingschool.data.remote.profile.ProfileApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(val context: Context) {

    val loginRetrofit = Retrofit.Builder()
        .baseUrl("https://80c6-94-143-197-187.ngrok-free.app/")
        .client(provideLoginOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://80c6-94-143-197-187.ngrok-free.app/")
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun provideLoginOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(LoginInterceptor(context))
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun provideLoginApi(): LoginApiService {
        return loginRetrofit.create(LoginApiService::class.java)
    }

    fun provideProfileApi(): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    fun providePasswordApi(): PasswordApiService {
        return retrofit.create(PasswordApiService::class.java)
    }

    fun provideMainApi(): MainApiService {
        return retrofit.create(MainApiService::class.java)
    }

    fun provideDetailsApi(): DetailsApiService {
        return retrofit.create(DetailsApiService::class.java)
    }

}