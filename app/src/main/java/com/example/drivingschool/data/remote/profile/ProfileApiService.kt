package com.example.drivingschool.data.remote.profile

import com.example.drivingschool.data.models.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET


interface ProfileApiService {
    @GET("profile/")
    suspend fun getProfile(): Response<ProfileResponse>
}