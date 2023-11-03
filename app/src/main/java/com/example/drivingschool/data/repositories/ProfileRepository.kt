package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.InstructorProfileResponse
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.remote.login.LoginApiService
import com.example.drivingschool.data.remote.profile.ProfileApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApiService: LoginApiService
) {
    suspend fun getProfile() : Flow<UiState<ProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getInstructorProfile() : Flow<UiState<InstructorProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getInstructorProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)
}