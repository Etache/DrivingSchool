package com.example.drivingschool.data.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import java.sql.Struct

class PreferencesHelper(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

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




    companion object {
        const val LOGIN_KEY = "login_key"
        const val ACCESS_TOKEN_KEY = "access_key"
        const val REFRESH_TOKEN_KEY = "refresh_key"
        const val ROLE_KEY = "role_key"
    }
}