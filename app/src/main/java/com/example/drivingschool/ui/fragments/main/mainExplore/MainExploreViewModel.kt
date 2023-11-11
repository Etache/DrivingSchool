package com.example.drivingschool.ui.fragments.main.mainExplore

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.mainresponse.Lessons
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.repositories.MainRepository
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.instructorMain.fragments.InstructorCurrentLessonFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainExploreViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel() {

    private val _currentState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val currentState = _currentState.asStateFlow()

    private val _previousState = MutableStateFlow<UiState<Lessons>>(UiState.Loading())
    val previousState = _previousState.asStateFlow()

    init {
        getCurrent()
        getPrevious()
    }

    private fun getCurrent() = viewModelScope.launch {
        repository.getCurrentLessons().collect {
            _currentState.value = it
        }
    }

    private fun getPrevious() = viewModelScope.launch {
        repository.getPreviousLessons().collect {
            _previousState.value = it
        }
        Log.d("ahahaha", "данные на MainExploreViewModel")
    }
}