package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("token/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}