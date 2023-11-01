package com.example.drivingschool.data.repositories

import android.net.Uri
import com.example.drivingschool.data.models.InstructorProfileResponse
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.remote.profile.ProfileApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ProfileRepository @Inject constructor(
    private val profileApiService: ProfileApiService
) {
    suspend fun getProfile() : Flow<UiState<ProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getInstructorProfile() : Flow<UiState<InstructorProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = profileApiService.getInstructorProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun deleteProfilePhoto() {
        profileApiService.deleteProfilePhoto()
    }

    suspend fun updateProfilePhoto(image: MultipartBody.Part) = profileApiService.updateProfilePhoto(image)


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