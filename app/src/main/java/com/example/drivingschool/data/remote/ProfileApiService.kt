package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.ProfileResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET


interface ProfileApiService {
    @GET("profile/")
    fun getProfile() : ProfileResponse
}