package com.example.drivingschool.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.data.models.LoginResponse
import com.example.drivingschool.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    fun login(loginRequest: LoginRequest): MutableLiveData<String?> {
        val tokenLiveData = MutableLiveData<String?>()
        RetrofitClient().provideApi().login(loginRequest).enqueue(object : Callback<LoginResponse> {
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