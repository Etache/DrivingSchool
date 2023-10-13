package com.example.drivingschool.data.local.sharedpreferences

import android.content.Context

class PreferencesHelper(context: Context) {

    val sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    // Образец
    var name: String?
        set(value) = sharedPreferences.edit().putString("name", value).apply()
        get() = sharedPreferences.getString("name", "Default")
}