package com.example.drivingschool.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel(){

    fun getToken(loginRequest: LoginRequest): LiveData<String?> {
        return loginRepository.login(loginRequest)
    }
}