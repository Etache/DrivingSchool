package com.example.drivingschool.data.remote.login

import android.content.Context
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class LoginInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val preferencesHelper = PreferencesHelper(context)
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "Bearer ${preferencesHelper.accessToken!!}").build()
        return chain.proceed(request)
    }
}