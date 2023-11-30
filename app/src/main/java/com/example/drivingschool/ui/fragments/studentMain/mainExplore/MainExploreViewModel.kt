package com.example.drivingschool.ui.fragments.studentMain.mainExplore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.mainresponse.Lessons
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.models.notification.NotificationCheckResponse
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainExploreViewModel @Inject constructor(
    private val repository: DrivingRepository,
) : BaseViewModel() {

    private val _currentState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val currentState = _currentState.asStateFlow()

    private val _previousState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val previousState = _previousState.asStateFlow()


    private val _currentDetailsState = MutableStateFlow<UiState<LessonsItem>>(UiState.Loading())
    val currentDetailsState = _currentDetailsState.asStateFlow()


    private var _notifications = MutableLiveData<UiState<NotificationModel>>()
    val notifications: LiveData<UiState<NotificationModel>> = _notifications

    private var _notificationCheck = MutableLiveData<UiState<NotificationCheckResponse>>()
    val notificationCheck: LiveData<UiState<NotificationCheckResponse>> = _notificationCheck

    fun getCurrent() = viewModelScope.launch {
        repository.getCurrentLessons().collect {
            _currentState.value = it
        }
    }

    fun getPrevious() = viewModelScope.launch {
        repository.getPreviousLessons().collect {
            _previousState.value = it
        }
    }

    fun getCurrentById(id: String) = viewModelScope.launch {
        repository.getCurrentLessonsById(id).collect {
            _currentDetailsState.value = it
        }
    }

    fun checkNotifications() {
        viewModelScope.launch {
            repository.checkNotifications().collect {
                _notificationCheck.postValue(it)
            }
        }
    }

}