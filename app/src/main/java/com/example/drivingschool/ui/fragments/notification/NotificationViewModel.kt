package com.example.drivingschool.ui.fragments.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.notification.NotificationCheckResponse
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: DrivingRepository
) : BaseViewModel() {
    private var _notifications = MutableLiveData<UiState<NotificationModel>>()
    val notifications: LiveData<UiState<NotificationModel>> = _notifications

    private var _notificationCheck = MutableLiveData<UiState<NotificationCheckResponse>>()
    val notificationCheck: LiveData<UiState<NotificationCheckResponse>> = _notificationCheck

    fun getNotifications() {
        viewModelScope.launch {
            repository.getNotifications().collect {
                _notifications.postValue(it)
                Log.d("ololo", "getNotifications: $_notifications")
            }
        }
    }

    fun checkNotifications() {
        viewModelScope.launch {
            repository.checkNotifications().collect {
                _notificationCheck.postValue(it)
                Log.d("ololo", "getNotifications: $_notifications")
            }
        }
    }

    fun readNotifications() {
        viewModelScope.launch {
            repository.readNotifications()
        }
    }

}