package com.example.drivingschool.data.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.drivingschool.ui.fragments.Constants.ACCESS_TOKEN_KEY
import com.example.drivingschool.ui.fragments.Constants.LOGIN_KEY
import com.example.drivingschool.ui.fragments.Constants.PASSWORD_KEY
import com.example.drivingschool.ui.fragments.Constants.REFRESH_TOKEN_KEY
import com.example.drivingschool.ui.fragments.Constants.ROLE_KEY
import javax.inject.Inject

class PreferencesHelper @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    var isLoginSuccess: Boolean
        set(value) = sharedPreferences.edit().putBoolean(LOGIN_KEY, value).apply()
        get() = sharedPreferences.getBoolean(LOGIN_KEY, false)

    var accessToken : String?
        set(value) = sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, value).apply()
        get() = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    var refreshToken : String?
        set(value) = sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, value).apply()
        get() = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)

    var role : String?
        set(value) = sharedPreferences.edit().putString(ROLE_KEY, value).apply()
        get() = sharedPreferences.getString(ROLE_KEY, null)

    var password : String?
        set(value) = sharedPreferences.edit().putString(PASSWORD_KEY, value).apply()
        get() = sharedPreferences.getString(PASSWORD_KEY, null)

}