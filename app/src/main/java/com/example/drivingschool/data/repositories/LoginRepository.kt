package com.example.drivingschool.data.repositories

import com.example.drivingschool.R
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApiService: DrivingApiService
){

    suspend fun login(loginRequest: LoginRequest) : Flow<UiState<LoginResponse>> = flow {
        emit(UiState.Loading())
        val data = loginApiService.login(loginRequest).body()
        if (data != null) {
            emit(UiState.Success(data))
        } else {
            emit(UiState.Error(R.string.login_error_text))
        }
    }.flowOn(Dispatchers.IO)

}