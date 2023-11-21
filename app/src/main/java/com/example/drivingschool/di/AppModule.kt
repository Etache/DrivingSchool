package com.example.drivingschool.di

import android.content.Context
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.data.remote.LoginInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): DrivingApiService {
        return Retrofit.Builder()
            .baseUrl(" https://ebec-94-143-197-63.ngrok-free.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrivingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(helper: PreferencesHelper): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoginInterceptor(helper))
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesPreferencesHelper(@ApplicationContext context: Context) = PreferencesHelper(context)
}
