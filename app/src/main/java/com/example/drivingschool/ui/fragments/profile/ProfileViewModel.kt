package com.example.drivingschool.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.StudentProfileResponse
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: DrivingRepository
) : BaseViewModel() {

    private var _profile = MutableLiveData<UiState<StudentProfileResponse>>()
    val profile: LiveData<UiState<StudentProfileResponse>> = _profile

    private var _instructorProfile = MutableLiveData<UiState<InstructorResponse>>()
    val instructorProfile: LiveData<UiState<InstructorResponse>> = _instructorProfile

    init {
        getProfile()
        getInstructorProfile()
    }

    fun getProfile() = viewModelScope.launch {
        profileRepository.getProfile().collect {
            _profile.postValue(it)
        }
    }

    fun getInstructorProfile() = viewModelScope.launch {
        profileRepository.getInstructorProfile().collect {
            _instructorProfile.postValue(it)
        }
    }

    suspend fun changePassword(passwordRequest: PasswordRequest) {
        profileRepository.changePassword(passwordRequest)
    }

    fun deleteProfilePhoto() {
        viewModelScope.launch {
            profileRepository.deleteProfilePhoto()
        }
    }

    fun updateProfilePhoto(image: MultipartBody.Part) = viewModelScope.launch {
        profileRepository.updateProfilePhoto(image)
    }
}

