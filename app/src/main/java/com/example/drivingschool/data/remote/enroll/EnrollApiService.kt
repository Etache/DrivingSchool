package com.example.drivingschool.data.remote.enroll

import com.example.drivingschool.data.models.InstructorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EnrollApiService {

    @GET("instructors/")
    suspend fun getInstructors(): Response<List<InstructorResponse>>

    // запрос getInstructorById
    @GET("instructors/{id}")
    suspend fun getInstructorById(@Path("id") instructorId: Int): Response<InstructorResponse>

}