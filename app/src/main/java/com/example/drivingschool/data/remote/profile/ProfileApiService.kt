package com.example.drivingschool.data.remote.profile

import com.example.drivingschool.data.models.InstructorProfileResponse
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
    suspend fun getInstructorProfile(): Response<InstructorProfileResponse>

    @Multipart
    @PUT("change_pp/")
    suspend fun updateProfilePhoto(@Part photo: MultipartBody.Part): Response<ProfileResponse>

    @DELETE("delete_pp/")
    suspend fun deleteProfilePhoto(): Response<ProfileResponse>

}