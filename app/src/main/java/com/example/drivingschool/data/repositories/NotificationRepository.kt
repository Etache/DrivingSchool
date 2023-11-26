package com.example.drivingschool.data.repositories

import com.example.drivingschool.R
import com.example.drivingschool.data.models.notification.NotificationCheckResponse
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.data.remote.DrivingApiService
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApi : DrivingApiService
) {

    suspend fun getNotifications() : Flow<UiState<NotificationModel>> = flow {
        emit(UiState.Loading())
        try {
            val response = notificationApi.getNotifications()
            if (response.isSuccessful) {
                emit(UiState.Success(response.body()))
            } else {
                emit(UiState.Error(R.string.notification_error))
            }
        } catch (e: Exception) {
            emit(UiState.Error(R.string.notification_error))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun checkNotifications() : Flow<UiState<NotificationCheckResponse>> = flow {
        try {
            val response = notificationApi.checkNotifications()
            if (response.isSuccessful) {
                emit(UiState.Success(response.body()))
            } else {
                emit(UiState.Error(R.string.notification_error))
            }
        } catch (e: Exception) {
            emit(UiState.Error(R.string.notification_error))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun readNotifications() = notificationApi.readNotifications()

}