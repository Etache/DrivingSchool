package com.example.drivingschool.data.remote.login

import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}