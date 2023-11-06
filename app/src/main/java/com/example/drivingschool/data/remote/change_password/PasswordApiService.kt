package com.example.drivingschool.data.remote.change_password

import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface PasswordApiService {
    @PATCH("change_password/")
    suspend fun changePassword(
        @Body requestBody : PasswordRequest
    ) : Response<ProfileResponse>

}