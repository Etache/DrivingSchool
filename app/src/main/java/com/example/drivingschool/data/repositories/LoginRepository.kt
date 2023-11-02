package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import com.example.drivingschool.data.remote.login.LoginApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApiService: LoginApiService
){

    suspend fun login(loginRequest: LoginRequest) : Flow<UiState<LoginResponse>> = flow {
        emit(UiState.Loading())
        val data = loginApiService.login(loginRequest).body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

}