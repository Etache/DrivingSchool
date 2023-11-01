package com.example.drivingschool.data.remote.currentDetail

import com.example.drivingschool.data.models.mainresponse.LessonsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface DetailsApiService {

    @GET("lessons/details/")
    suspend fun getDetails(
        @Query("lesson_id") lessonId: String
    ): Response<LessonsItem>
}