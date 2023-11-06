package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.remote.change_password.PasswordApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PasswordRepository @Inject constructor(
    private val passwordApiService: PasswordApiService
) {

//    suspend fun changePassword(passwordRequest: PasswordRequest): Flow<UiState<ProfileResponse>> = flow {
//        emit(UiState.Loading())
//        val data = passwordApiService.changePassword(passwordRequest).body()
//        if (data != null) {
//            emit(UiState.Success(data))
//        }
//    }.flowOn(Dispatchers.IO)

    suspend fun changePassword(passwordRequest: PasswordRequest) {
        passwordApiService.changePassword(passwordRequest)
    }

}