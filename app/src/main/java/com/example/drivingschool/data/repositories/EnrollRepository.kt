package com.example.drivingschool.data.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.refresh.EnrollLessonResponse
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class EnrollRepository @Inject constructor(private val enrollApiService: DrivingApiService) {

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

    @SuppressLint("SuspiciousIndentation")
    suspend fun enrollForLesson(enrollRequest : EnrollLessonRequest): Flow<UiState<EnrollLessonResponse>> = flow {
        emit(UiState.Loading())
        val data = enrollApiService.enrollForLesson(enrollRequest).body()
            emit(UiState.Success(data))
            Log.e("ololo", "repositoryEnrollForLesson: $enrollRequest")
    }.flowOn(Dispatchers.IO)

//    suspend fun enrollForLesson(instructor: String, date: String, time: String): LiveData<String> {
//        val file = File("path/to/your/file.txt")
//        val fileRequestBody =
//            RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
//        val filePart = MultipartBody.Part.createFormData("file", file.name, fileRequestBody)
//        val call = enrollApiService.enrollForLesson(instructor, date, time, filePart)
//        var responseBody = MutableLiveData<String>()
//
//        call.enqueue(object : Callback<EnrollLessonResponse> {
//            override fun onResponse(
//                call: Call<EnrollLessonResponse>,
//                response: Response<EnrollLessonResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // Handle successful response
//                    responseBody.value = response.body()?.success.toString()
//                    Log.e("ololo", "repositoryEnrollForLesson: $responseBody")
//
//                } else {
//                    // Handle error
//                    println("Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<EnrollLessonResponse>, t: Throwable) {
//                // Handle failure
//                println("Request failed: ${t.message}")
//            }
//        })
//        return responseBody
//    }

}