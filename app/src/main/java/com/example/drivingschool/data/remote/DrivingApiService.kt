package com.example.drivingschool.data.remote

import com.example.drivingschool.data.models.CancelRequest
import com.example.drivingschool.data.models.CancelResponse
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.models.FeedbackForInstructorResponse
import com.example.drivingschool.data.models.FeedbackForStudentRequest
import com.example.drivingschool.data.models.FeedbackForStudentResponse
import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.data.models.InstructorWorkWindowResponse
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.models.mainresponse.Lessons
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.models.refresh.EnrollLessonResponse
import com.example.drivingschool.data.models.refresh.RefreshTokenRequest
import com.example.drivingschool.data.models.refresh.RefreshTokenResponse
import com.example.drivingschool.data.models.start_finish_lesson.ChangeLessonStatusResponse
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
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    @PATCH("change_password/")
    suspend fun changePassword(
        @Body requestBody: PasswordRequest
    ): Response<ProfileResponse>

    @GET("lessons/{id}/")
    suspend fun getCurrent(@Path("id") id: String): Response<LessonsItem>
 
    @PATCH("lessons/{id}/start/")
    suspend fun startLesson(@Path("id") id: String): Response<ChangeLessonStatusResponse>

    @PATCH("lessons/{id}/finish/")
    suspend fun finishLesson(@Path("id") id: String): Response<ChangeLessonStatusResponse>

    @PATCH("lessons/{id}/absent/")
    suspend fun studentAbsent(@Path("id") id: String): Response<ChangeLessonStatusResponse>

    @GET("lessons/{id}/")
    suspend fun getPrevious(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/{id}/")
    suspend fun getCurrentDetailsInstructor(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/{id}/")
    suspend fun getPreviousDetailsInstructor(@Path("id") id: String): Response<LessonsItem>

    @GET("lessons/current/")
    suspend fun getCurrent(): Response<Lessons>

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

    @PUT("lessons/cancel/")
    suspend fun cancelLesson(@Body cancelRequest: CancelRequest): Response<CancelResponse>

    @POST("feedbacks/instructor/create/")
    suspend fun createComment(@Body comment: FeedbackForInstructorRequest): Response<FeedbackForInstructorResponse>

    @POST("feedbacks/student/create/")
    suspend fun createInstructorComment(@Body comment: FeedbackForStudentRequest): Response<FeedbackForStudentResponse>

    @POST("lessons/create/")
    suspend fun enrollForLesson(@Body enrollResponse: EnrollLessonRequest): Response<EnrollLessonResponse>

    @GET("workwindows/details/")
    suspend fun getWorkWindows(): Response<InstructorWorkWindowResponse>

    @POST("workwindows/create/")
    suspend fun setWorkWindows(@Body instructorWorkWindowRequest: InstructorWorkWindowRequest): Response<InstructorWorkWindowResponse>
}