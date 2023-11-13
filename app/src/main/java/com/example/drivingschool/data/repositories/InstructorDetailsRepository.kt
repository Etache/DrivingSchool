package com.example.drivingschool.data.repositories

import android.util.Log
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.models.FeedbackForStudentRequest
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InstructorDetailsRepository @Inject constructor(
    private val apiService: DrivingApiService
) {
    suspend fun getPreviousDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = apiService.getPreviousDetailsInstructor(id)
        Log.e("ololo", "getLessonDetails: ${response.body()}")
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveComment(comment: FeedbackForStudentRequest) = flow {
        val data = apiService.createInstructorComment(comment = comment).body()
        emit(data)
        Log.e("ololo", "repositorySaveComment: $data")
    }
}