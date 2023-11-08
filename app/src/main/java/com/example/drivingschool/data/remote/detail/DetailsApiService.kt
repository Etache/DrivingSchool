package com.example.drivingschool.data.remote.detail

import com.example.drivingschool.data.models.CancelRequest
import com.example.drivingschool.data.models.CancelResponse
import com.example.drivingschool.data.models.StudentCommentRequest
import com.example.drivingschool.data.models.StudentCommentResponse
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface DetailsApiService {

    @GET("lessons/details/{id}")
    suspend fun getCurrent(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/details/{id}")
    suspend fun getPrevious(@Path("id") id: String): Response<LessonsItem>

    @PUT("lessons/cancel/")
    suspend fun cancelLesson(@Body cancelRequest: CancelRequest): Response<CancelResponse>

    @POST("feedbacks/student/create")
    suspend fun createComment(@Body comment: StudentCommentRequest): Response<StudentCommentResponse>

}