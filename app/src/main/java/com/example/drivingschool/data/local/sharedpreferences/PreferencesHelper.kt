package com.example.drivingschool.data.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    var isLoginSuccess: Boolean
        set(value) = sharedPreferences.edit().putBoolean(LOGIN_KEY, value).apply()
        get() = sharedPreferences.getBoolean(LOGIN_KEY, false)

    companion object {
        const val LOGIN_KEY = "login_key"
    }
}