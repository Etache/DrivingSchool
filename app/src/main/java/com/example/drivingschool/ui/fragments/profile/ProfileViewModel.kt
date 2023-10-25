package com.example.drivingschool.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.repositories.PasswordRepository
import com.example.drivingschool.data.repositories.ProfileRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private var _profile = MutableLiveData<UiState<ProfileResponse>>()
    val profile: LiveData<UiState<ProfileResponse>> = _profile

    init {
        getProfile()
    }

    fun getProfile() = viewModelScope.launch {
        profileRepository.getProfile().collect {
            _profile.postValue(it)
            Log.d("madimadi", "getProfile: $_profile")
        }
    }

    fun changePassword(passwordRequest: PasswordRequest) {
        passwordRepository.changePassword(passwordRequest)
    }

}