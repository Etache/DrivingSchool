package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DrivingRepository @Inject constructor(
    private val enrollInstructorApi : DrivingApiService
) {

    suspend fun getWorkWindows() = flow {
        emit(UiState.Loading())
        val response = enrollInstructorApi.getWorkWindows().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun setWorkWindows(instructorWorkWindowRequest: InstructorWorkWindowRequest) = flow {
        val data = enrollInstructorApi.setWorkWindows(
            instructorWorkWindowRequest = instructorWorkWindowRequest
        ).body()
        emit(data)
    }.flowOn(Dispatchers.IO)

}