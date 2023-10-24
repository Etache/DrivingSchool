package com.example.drivingschool.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.ProfileResponse
import com.example.drivingschool.data.repositories.ProfileRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profile: MutableStateFlow<UiState<ProfileResponse>> =
        MutableStateFlow(UiState.Loading())
    val profile: StateFlow<UiState<ProfileResponse>> = _profile

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect { resource ->
                when (resource) {
                    is UiState.Loading -> _profile.value = UiState.Loading()
                    is UiState.Success -> {
                        if (resource.data != null) {
                            _profile.value = UiState.Success(data = resource.data)
                            Log.d("madimadi", "profile: ${_profile}")
                        } else {
                            _profile.value = UiState.Empty()
                        }
                    }

                    is UiState.Error -> _profile.value = UiState.Error(msg = resource.msg)
                    else -> {}
                }
            }
        }
    }

}