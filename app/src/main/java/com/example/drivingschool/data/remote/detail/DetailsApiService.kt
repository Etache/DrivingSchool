package com.example.drivingschool.data.remote.detail

import com.example.drivingschool.data.models.mainresponse.LessonsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface DetailsApiService {

    @GET("lessons/details/{id}")
    suspend fun getCurrent(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/details/{id}")
    suspend fun getPrevious(@Path("id") id: String): Response<LessonsItem>

}