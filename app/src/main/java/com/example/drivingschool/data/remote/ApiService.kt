package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.InstructorRequest
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("token/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("instructors/")
    fun getInstructors() : Call<List<InstructorRequest>>
}