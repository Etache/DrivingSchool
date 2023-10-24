package com.example.drivingschool.data.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    var isLoginSuccess: Boolean
        set(value) = sharedPreferences.edit().putBoolean(LOGIN_KEY, value).apply()
        get() = sharedPreferences.getBoolean(LOGIN_KEY, false)

    var accessToken : String?
        set(value) = sharedPreferences.edit().putString(TOKEN_KEY, value).apply()
        get() = sharedPreferences.getString(TOKEN_KEY, null)

    companion object {
        const val LOGIN_KEY = "login_key"
        const val TOKEN_KEY = "token_key"
    }
}