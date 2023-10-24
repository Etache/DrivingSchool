package com.example.drivingschool.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import com.example.drivingschool.data.remote.LoginApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApiService: LoginApiService
){
    fun login(loginRequest: LoginRequest): MutableLiveData<String?> {
        val tokenLiveData = MutableLiveData<String?>()
        loginApiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                tokenLiveData.postValue(response.body()?.accessToken)
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("madimadi", "onFailure: ${t.message}")
            }
        })
        return tokenLiveData
    }
}