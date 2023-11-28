package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class PasswordRequest(

    var password: String?,
    @SerializedName("new_password")
    var newPassword: String?
)