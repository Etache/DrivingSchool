package com.example.drivingschool.ui.fragments.enroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.refresh.EnrollLessonResponse
import com.example.drivingschool.data.repositories.EnrollRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollViewModel @Inject constructor(private val enrollRepository: EnrollRepository) :
    BaseViewModel() {

    private val _instructors = MutableLiveData<UiState<List<InstructorResponse>>>()
    val instructors: LiveData<UiState<List<InstructorResponse>>> = _instructors

    private val _instructorDetails = MutableLiveData<UiState<InstructorResponse>>()
    val instructorDetails: LiveData<UiState<InstructorResponse>> = _instructorDetails

    private val _enrollResponse = MutableLiveData<UiState<EnrollLessonResponse>>()
    val enrollResponse: LiveData<UiState<EnrollLessonResponse>> = _enrollResponse

    init {
        getInstructors()
        //getInstructorsId()
    }

    fun getInstructors() = viewModelScope.launch {
        enrollRepository.getInstructors().collect {
            _instructors.postValue(it)
            Log.d("ahahaha", "getInstructors view model: $_instructors")
        }
    }

    fun getInstructorById(id: Int) = viewModelScope.launch {
        enrollRepository.getInstructorById(id).collect {
            _instructorDetails.postValue(it)
            Log.d("ahahaha", "getInstructorDetails view model: $_instructorDetails")
        }
    }

    fun enrollForLesson(enrollRequest : EnrollLessonRequest) = viewModelScope.launch {
        enrollRepository.enrollForLesson(enrollRequest).collect {
            _enrollResponse.postValue(it)
            Log.d("madimadi", "enrollResponse in viewModel: $_enrollResponse")
        }
    }

//    suspend fun enrollForLesson(instructor: String, date : String, time : String) : LiveData<String>{
//        return enrollRepository.enrollForLesson(instructor, date, time)
//    }
}