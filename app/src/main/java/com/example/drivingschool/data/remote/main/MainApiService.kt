package com.example.drivingschool.data.remote.main

import com.example.drivingschool.data.models.mainresponse.Lessons
import retrofit2.Response
import retrofit2.http.GET

interface MainApiService {
    @GET("lessons/current/")
    suspend fun getCurrent(): Response<Lessons>

    @GET("lessons/previous/")
    suspend fun getPrevious(): Response<Lessons>

}