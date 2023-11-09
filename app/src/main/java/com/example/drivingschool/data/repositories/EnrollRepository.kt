package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.remote.enroll.EnrollApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EnrollRepository @Inject constructor(private val enrollApiService: EnrollApiService) {

    suspend fun getInstructors(): Flow<UiState<List<InstructorResponse>>> = flow {
        emit(UiState.Loading())
        val data = enrollApiService.getInstructors().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)
    suspend fun getInstructorById(id: Int): Flow<UiState<InstructorResponse>> = flow {
        emit(UiState.Loading())
        val data = enrollApiService.getInstructorById(id).body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)
}