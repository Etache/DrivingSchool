package com.example.drivingschool.data.remote.currentDetail

import com.example.drivingschool.data.models.mainresponse.LessonsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface DetailsApiService {

    @GET("lessons/details/{id}")
    suspend fun getCurrent(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/details/{id}")
    suspend fun getPrevious(@Path("id") id: String): Response<LessonsItem>

//    @PUT("lessons/cancel/")
//    suspend fun cancelLesson(@Query("lesson_id") lessonId: String)


}