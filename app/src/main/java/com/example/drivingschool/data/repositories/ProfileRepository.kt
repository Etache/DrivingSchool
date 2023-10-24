package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.remote.ProfileApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApiService: ProfileApiService
) {
    fun getProfile(): Flow<UiState<ProfileResponse>> {
        return flow {
            val data = profileApiService.getProfile()
            emit(UiState.Success(data))
        }.flowOn(Dispatchers.IO)
    }
}