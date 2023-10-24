package com.example.drivingschool.di

import android.content.Context
import com.example.drivingschool.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(@ApplicationContext  context: Context) = RetrofitClient(context)

    @Provides
    @Singleton
    fun provideLoginApiService(retrofitClient: RetrofitClient) = retrofitClient.provideLoginApi()

    @Provides
    @Singleton
    fun provideProfileApiService(retrofitClient: RetrofitClient) = retrofitClient.provideProfileApi()
}