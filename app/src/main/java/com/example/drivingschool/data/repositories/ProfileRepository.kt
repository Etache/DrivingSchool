package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.StudentProfileResponse
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApiService: DrivingApiService
) {
    suspend fun getProfile(): Flow<UiState<StudentProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getInstructorProfile(): Flow<UiState<InstructorResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getInstructorProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun deleteProfilePhoto() {
        profileApiService.deleteStudentProfilePhoto()
    }

    suspend fun updateProfilePhoto(image: MultipartBody.Part) =
        profileApiService.updateStudentProfilePhoto(image)
}