package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.remote.change_password.PasswordApiService
import com.example.drivingschool.data.remote.login.LoginApiService
import javax.inject.Inject

class PasswordRepository @Inject constructor(
    private val passwordApiService: LoginApiService
) {
    suspend fun changePassword(passwordRequest: PasswordRequest) {
        passwordApiService.changePassword(passwordRequest)
    }
}