package com.example.drivingschool.ui.fragments.enroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.refresh.EnrollLessonResponse
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollViewModel @Inject constructor(private val enrollRepository: DrivingRepository) :
    BaseViewModel() {

    private val _instructors = MutableLiveData<UiState<List<InstructorResponse>>>()
    val instructors: LiveData<UiState<List<InstructorResponse>>> = _instructors

    private val _instructorDetails = MutableLiveData<UiState<InstructorResponse>>()
    val instructorDetails: LiveData<UiState<InstructorResponse>> = _instructorDetails

    private val _enrollResponse = MutableLiveData<UiState<EnrollLessonResponse>>()
    val enrollResponse: LiveData<UiState<EnrollLessonResponse>> = _enrollResponse

    fun getInstructors() = viewModelScope.launch {
        enrollRepository.getInstructors().collect {
            _instructors.postValue(it)
        }
    }

    fun getInstructorById(id: Int) = viewModelScope.launch {
        enrollRepository.getInstructorById(id).collect {
            _instructorDetails.postValue(it)
        }
    }

    fun enrollForLesson(enrollRequest : EnrollLessonRequest) = viewModelScope.launch {
        enrollRepository.enrollForLesson(enrollRequest).collect {
            _enrollResponse.postValue(it)
        }
    }

}