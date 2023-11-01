package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.remote.currentDetail.DetailsApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val apiService: DetailsApiService
){
    suspend fun getLessonDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = apiService.getDetails(id).body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)
}