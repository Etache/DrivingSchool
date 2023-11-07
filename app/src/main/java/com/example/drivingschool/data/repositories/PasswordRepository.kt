package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.remote.DrivingApiService
import javax.inject.Inject

class PasswordRepository @Inject constructor(
    private val passwordApiService: DrivingApiService
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