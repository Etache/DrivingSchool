package com.example.drivingschool.data.remote.profile

import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part


interface ProfileApiService {
    @GET("profile/")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("profile/")
    suspend fun getInstructorProfile(): Response<InstructorResponse>


}