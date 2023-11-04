package com.example.drivingschool.data.repositories

import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.remote.profile.ProfileApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ProfileRepository @Inject constructor(
    private val profileApiService: ProfileApiService
) {
    suspend fun getProfile(): Flow<UiState<ProfileResponse>> = flow {
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


//    suspend fun updateProfilePhoto(image: MultipartBody.Part) : Flow<UiState<ProfileResponse>> = flow {
//        emit(UiState.Loading())
//        val data = profileApiService.updateProfilePhoto(image).body()
//        if (data != null) {
//            emit(UiState.Success(data))
//        }
//    }.flowOn(Dispatchers.IO)


//    suspend fun updateProfilePhoto(file: File) {
//        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val body = MultipartBody.Part.createFormData("upload", file.name, reqFile)
//        profileApiService.updateProfilePhoto(body)
//    }
}