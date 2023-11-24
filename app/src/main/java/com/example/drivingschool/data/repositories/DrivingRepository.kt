package com.example.drivingschool.data.repositories

import android.util.Log
import com.example.drivingschool.data.models.CancelRequest
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DrivingRepository @Inject constructor(
    private val drivingApiService : DrivingApiService
) {

    suspend fun getWorkWindows() = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getWorkWindows().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun setWorkWindows(instructorWorkWindowRequest: InstructorWorkWindowRequest) = flow {
        val data = drivingApiService.setWorkWindows(
            instructorWorkWindowRequest = instructorWorkWindowRequest
        ).body()
        emit(data)
    }.flowOn(Dispatchers.IO)

    suspend fun getCurrentDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getCurrent(id)
        Log.e("ololo", "getLessonDetails: ${response.body()}")
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPreviousDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getPrevious(id)
        Log.e("ololo", "getLessonDetails: ${response.body()}")
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun cancelLesson(id: String) = flow {
        emit(drivingApiService.cancelLesson(lessonId = id, CancelRequest(lessonId = id)).body())
    }

    suspend fun saveComment(comment: FeedbackForInstructorRequest) = flow {
        val data = drivingApiService.createComment(comment = comment).body()
        emit(data)
        Log.e("ololo", "repositorySaveComment: $data")
    }

}