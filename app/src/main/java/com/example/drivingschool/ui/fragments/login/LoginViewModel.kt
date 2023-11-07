package com.example.drivingschool.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.data.models.login.LoginResponse
import com.example.drivingschool.data.repositories.LoginRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel(){

    private var _token = MutableLiveData<UiState<LoginResponse>>()
    val token: LiveData<UiState<LoginResponse>> = _token

//    init {
//        getToken(loginRequest)
//    }

    fun getToken(loginRequest: LoginRequest) = viewModelScope.launch {
        loginRepository.login(loginRequest).collect {
            _token.postValue(it)
//            Log.d("madimadi", "getProfile: $_token")
        }
    }
}