package com.example.drivingschool.data.remote.login

import com.example.drivingschool.data.models.InstructorProfileResponse
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.models.refresh.RefreshTokenRequest
import com.example.drivingschool.data.models.refresh.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface LoginApiService {
    @POST("token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("token/refresh/")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : Response<RefreshTokenResponse>

    @GET("profile/")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("profile/")
    suspend fun getInstructorProfile(): Response<InstructorProfileResponse>

    @PATCH("change_password/")
    suspend fun changePassword(
        @Body requestBody : PasswordRequest
    ) : Response<ProfileResponse>
}