package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: DrivingApiService
) {

    suspend fun getCurrentLessons() = flow {
        emit(UiState.Loading())
        val response = mainApiService.getCurrent().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPreviousLessons() = flow {
        emit(UiState.Loading())
        val response = mainApiService.getPrevious().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getCurrentLessonsById(id: Int) = flow {
        emit(UiState.Loading())
        val response = mainApiService.getCurrentById(id).body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)
}