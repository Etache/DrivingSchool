package com.example.drivingschool.data.repositories

import android.util.Log
import com.example.drivingschool.data.models.CancelRequest
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val apiService: DrivingApiService
) {
    suspend fun getCurrentDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = apiService.getCurrent(id)
        Log.e("ololo", "getLessonDetails: ${response.body()}")
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPreviousDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = apiService.getPrevious(id)
        Log.e("ololo", "getLessonDetails: ${response.body()}")
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun cancelLesson(id: String) = flow {
        emit(apiService.cancelLesson(CancelRequest(lessonId = id)).body())
    }

    suspend fun saveComment(comment: FeedbackForInstructorRequest) = flow {
        val data = apiService.createComment(comment = comment).body()
        emit(data)
        Log.e("ololo", "repositorySaveComment: $data")
    }


}