package com.example.drivingschool.data.repositories

import android.annotation.SuppressLint
import android.util.Log
import com.example.drivingschool.R
import com.example.drivingschool.data.models.CancelRequest
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.models.FeedbackForStudentRequest
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.StudentProfileResponse
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.models.notification.Notification
import com.example.drivingschool.data.models.notification.NotificationCheckResponse
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.data.models.refresh.EnrollLessonResponse
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class DrivingRepository @Inject constructor(
    private val drivingApiService: DrivingApiService
) {

    suspend fun login(loginRequest: LoginRequest): Flow<UiState<LoginResponse>> = flow {
        emit(UiState.Loading())
        val data = drivingApiService.login(loginRequest).body()
        if (data != null) {
            emit(UiState.Success(data))
        } else {
            emit(UiState.Error(R.string.login_error_text))
        }
    }.flowOn(Dispatchers.IO)

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
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPreviousDetails(id: String) = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getPrevious(id)
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
    }

    suspend fun getInstructors(): Flow<UiState<List<InstructorResponse>>> = flow {
        emit(UiState.Loading())
        val data = drivingApiService.getInstructors().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getInstructorById(id: Int): Flow<UiState<InstructorResponse>> = flow {
        emit(UiState.Loading())
        val data = drivingApiService.getInstructorById(id).body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    @SuppressLint("SuspiciousIndentation")
    suspend fun enrollForLesson(enrollRequest: EnrollLessonRequest): Flow<UiState<EnrollLessonResponse>> =
        flow {
            emit(UiState.Loading())
            val data = drivingApiService.enrollForLesson(enrollRequest).body()
            emit(UiState.Success(data))
        }.flowOn(Dispatchers.IO)

    suspend fun getPreviousDetailsInstructor(id: String) = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getPreviousDetailsInstructor(id)
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveInstructorComment(comment: FeedbackForStudentRequest) = flow {
        val data = drivingApiService.createInstructorComment(comment = comment).body()
        emit(data)
    }

    suspend fun getCurrentLessons() = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getCurrent().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPreviousLessons() = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getPrevious().body()
        if (response != null) {
            emit(UiState.Success(response))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    //iman
    suspend fun getCurrentLessonsById(id: String) = flow {
        emit(UiState.Loading())
        val response = drivingApiService.getCurrentDetailsInstructor(id)
        if (response.isSuccessful) {
            emit(UiState.Success(response.body()))
        } else {
            emit(UiState.Empty())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun startLesson(id: String) = flow {
        val response = drivingApiService.startLesson(id)
        emit(response.body())
    }

    suspend fun finishLesson(id: String) = flow {
        val response = drivingApiService.finishLesson(id)
        emit(response.body())
    }

    suspend fun studentAbsent(id: String) = flow {
        val response = drivingApiService.studentAbsent(id)
        emit(response.body())
    }

    suspend fun changePassword(passwordRequest: PasswordRequest) {
        drivingApiService.changePassword(passwordRequest)
    }

    suspend fun getProfile(): Flow<UiState<StudentProfileResponse>> = flow {
        emit(UiState.Loading())
        val data = drivingApiService.getProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getInstructorProfile(): Flow<UiState<InstructorResponse>> = flow {
        emit(UiState.Loading())
        val data = drivingApiService.getInstructorProfile().body()
        if (data != null) {
            emit(UiState.Success(data))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun deleteProfilePhoto() {
        drivingApiService.deleteStudentProfilePhoto()
    }

    suspend fun updateProfilePhoto(image: MultipartBody.Part) =
        drivingApiService.updateStudentProfilePhoto(image)

    suspend fun getNotifications(): Flow<UiState<List<Notification>>> = flow {
        emit(UiState.Loading())
        try {
            val response = drivingApiService.getNotifications()
            if (response.isSuccessful) {
                emit(UiState.Success(response.body()))
            } else {
                emit(UiState.Error(R.string.notification_error))
            }
        } catch (e: Exception) {
            emit(UiState.Error(R.string.notification_error))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun checkNotifications(): Flow<UiState<NotificationCheckResponse>> = flow {
        try {
            val response = drivingApiService.checkNotifications()
            if (response.isSuccessful) {
                emit(UiState.Success(response.body()))
            } else {
                emit(UiState.Error(R.string.notification_error))
            }
        } catch (e: Exception) {
            emit(UiState.Error(R.string.notification_error))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun readNotifications() = drivingApiService.readNotifications()

}