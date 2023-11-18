package com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll

import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.InstructorWorkWindowResponse
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollInstructorViewModel @Inject constructor (
    private val enrollInstructorRepository:DrivingRepository
): BaseViewModel() {

    private val _currentTimetable = MutableStateFlow<UiState<InstructorWorkWindowResponse>>(UiState.Loading())
    val currentTimetable = _currentTimetable.asStateFlow()

    init {
        getWorkWindows()
    }

    fun getWorkWindows() = viewModelScope.launch {
        enrollInstructorRepository.getWorkWindows().collect {
            _currentTimetable.value = it
        }
    }
}