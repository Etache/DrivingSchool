package com.example.drivingschool.ui.fragments.studentMain.mainExplore

import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.mainresponse.Lessons
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.repositories.MainRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainExploreViewModel @Inject constructor(
    private val repository: MainRepository,
) : BaseViewModel() {

    private val _currentState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val currentState = _currentState.asStateFlow()

    private val _previousState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val previousState = _previousState.asStateFlow()


    private val _currentDetailsState = MutableStateFlow<UiState<LessonsItem>>(UiState.Loading())
    val currentDetailsState = _currentDetailsState.asStateFlow()


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

}