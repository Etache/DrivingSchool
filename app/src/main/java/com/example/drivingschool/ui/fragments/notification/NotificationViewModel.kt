package com.example.drivingschool.ui.fragments.notification

import android.app.Notification
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.data.repositories.NotificationRepository
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
):ViewModel() {
    private var _notifications = MutableLiveData<UiState<NotificationModel>>()
    val notifications: LiveData<UiState<NotificationModel>> = _notifications

    fun getNotifications() {
        viewModelScope.launch {
            repository.getNotifications().collect{
                _notifications.postValue(it)
                Log.d("ololo", "getProfile: $_notifications")
            }
        }
    }

}