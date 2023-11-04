package com.example.drivingschool.ui.fragments.enroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.repositories.EnrollRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollViewModel @Inject constructor(private val enrollRepository: EnrollRepository) :
    ViewModel() {

    private val _instructors = MutableLiveData<UiState<List<InstructorResponse>>>()
    val instructors: LiveData<UiState<List<InstructorResponse>>> = _instructors

    private val _instructorDetails = MutableLiveData<UiState<InstructorResponse>>()
    val instructorDetails: LiveData<UiState<InstructorResponse>> = _instructorDetails

    init {
        getInstructors()
        //getInstructorsId()
    }

    fun getInstructors() = viewModelScope.launch {
        enrollRepository.getInstructors().collect {
            _instructors.postValue(it)
            Log.d("madimadi", "getInstructors view model: $_instructors")
        }
    }

    fun getInstructorById(id: Int) = viewModelScope.launch {
        enrollRepository.getInstructorById(id).collect {
            _instructorDetails.postValue(it)
            Log.d("madimadi", "getInstructorDetails view model: $_instructorDetails")
        }
    }

}