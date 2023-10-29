package com.example.drivingschool.ui.fragments.enroll.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.InstructorRequest
import com.example.drivingschool.data.remote.ApiService
import kotlinx.coroutines.launch

class InstructorViewModel(private val apiService: ApiService): ViewModel() {
    private val _instructors = MutableLiveData<List<InstructorRequest>>()
    val instructors: LiveData<List<InstructorRequest>> = _instructors

    init {
        loadInstructors()
    }

    private fun loadInstructors() {
        viewModelScope.launch {
            try {
                val call = apiService.getInstructors()
                val response = call.execute()
                if (response.isSuccessful) {
                    _instructors.value = response.body()
                } else {
                    Log.e("yyy", "Failed to load instructors: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("yyy", "Failed to load instructors: $e")
            }
        }
    }
}