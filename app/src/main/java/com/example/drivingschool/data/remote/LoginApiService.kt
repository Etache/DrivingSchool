package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import com.example.drivingschool.data.models.ProfileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {
    @POST("token/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}