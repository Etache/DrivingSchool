package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.data.models.InstructorWorkWindowResponse
import com.example.drivingschool.data.repositories.DrivingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckTimetableViewModel @Inject constructor(
    private val checkTimetableRepository: DrivingRepository
) : BaseViewModel() {

    private val _checkTimetable = MutableLiveData<InstructorWorkWindowResponse>()
    val checkTimetable = _checkTimetable as LiveData<InstructorWorkWindowResponse>

    init {

    }

    fun setWorkWindows(workWindowRequest: InstructorWorkWindowRequest) = viewModelScope.launch {
        checkTimetableRepository.setWorkWindows(workWindowRequest).collect {
            _checkTimetable.value = it
        }
    }
}