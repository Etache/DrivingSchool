package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.models.mainresponse.Lessons
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.models.refresh.RefreshTokenRequest
import com.example.drivingschool.data.models.refresh.RefreshTokenResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface DrivingApiService {

    @POST("token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("token/refresh/")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest) : Response<RefreshTokenResponse>

    @PATCH("change_password/")
    suspend fun changePassword(
        @Body requestBody : PasswordRequest
    ) : Response<ProfileResponse>

    @GET("lessons/details/{id}")
    suspend fun getCurrent(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/details/{id}")
    suspend fun getPrevious(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/current/")
    suspend fun getCurrent(): Response<Lessons>

    @GET("lessons/details/{id}")
    suspend fun getCurrentById(@Path("id") id: Int): Response<LessonsItem>

    @GET("lessons/previous/")
    suspend fun getPrevious(): Response<Lessons>

    @GET("instructors/")
    suspend fun getInstructors(): Response<List<InstructorResponse>>

    @GET("instructors/{id}")
    suspend fun getInstructorById(@Path("id") instructorId: Int): Response<InstructorResponse>

    @GET("profile/")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("profile/")
    suspend fun getInstructorProfile(): Response<InstructorResponse>

    @Multipart
    @PUT("change_pp/")
    suspend fun updateStudentProfilePhoto(@Part photo: MultipartBody.Part): Response<ProfileResponse>

    @DELETE("delete_pp/")
    suspend fun deleteStudentProfilePhoto(): Response<ProfileResponse>

}