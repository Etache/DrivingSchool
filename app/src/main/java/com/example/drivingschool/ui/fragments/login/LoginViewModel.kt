package com.example.drivingschool.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.repository.Repository

class LoginViewModel : ViewModel(){

    private val repository = Repository()
    fun getToken(loginRequest: LoginRequest): LiveData<String?> {
        return repository.login(loginRequest)
    }
}