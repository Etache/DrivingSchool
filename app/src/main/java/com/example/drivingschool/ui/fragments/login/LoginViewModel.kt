package com.example.drivingschool.ui.fragments.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import com.example.drivingschool.data.repositories.LoginRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel(){

//    fun getToken(loginRequest: LoginRequest): LiveData<List<String?>> {
//        return loginRepository.login(loginRequest)
//    }

    private var _token = MutableLiveData<UiState<LoginResponse>>()
    val token: LiveData<UiState<LoginResponse>> = _token

//    init {
//        getToken(loginRequest)
//    }

    fun getToken(loginRequest: LoginRequest) = viewModelScope.launch {
        loginRepository.login(loginRequest).collect {
            _token.postValue(it)
            Log.d("madimadi", "getProfile: $_token")
        }
    }
}