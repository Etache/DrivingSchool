package com.example.drivingschool.data.remote

import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.refresh.RefreshTokenRequest
import com.example.drivingschool.ui.fragments.Constants.AUTHORIZATION
import com.example.drivingschool.ui.fragments.Constants.BEARER
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LoginInterceptor @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : Interceptor {

    @Inject
    lateinit var loginApiService: DrivingApiService

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentAccessToken = preferencesHelper.accessToken
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER $currentAccessToken")
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            val refreshedAccessToken = runBlocking {
                refreshToken()
            }
            if (refreshedAccessToken != null) {
                val newRequest = request.newBuilder()
                    .header(AUTHORIZATION, "$BEARER $refreshedAccessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return response
    }

    private suspend fun refreshToken(): String? {
        try {
            val refreshToken = RefreshTokenRequest(preferencesHelper.refreshToken!!)
            val refreshedTokenResponse = loginApiService.refreshToken(refreshToken)

            return refreshedTokenResponse.body()?.accessTokenResponse
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

