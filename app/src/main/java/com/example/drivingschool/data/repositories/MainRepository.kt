package com.example.drivingschool.data.repositories

import android.util.Log
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
        Log.e("ahahaha", "getCurrentLessons: ${response}", )
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

    //iman
    suspend fun getCurrentLessonsById(id: String) = flow {
        emit(UiState.Loading())
        val response = mainApiService.getCurrentDetailsInstructor(id)
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun startLesson(id: String) = flow {
        val response = mainApiService.startLesson(id)
        emit(response.body())
    }

    suspend fun finishLesson(id : String) = flow {
        val response = mainApiService.finishLesson(id)
        emit(response.body())
    }

    suspend fun studentAbsent(id: String) = flow {
        val response = mainApiService.studentAbsent(id)
        emit(response.body())
    }
}