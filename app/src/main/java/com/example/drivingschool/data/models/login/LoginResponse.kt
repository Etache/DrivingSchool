package com.example.drivingschool.data.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("refresh")
    var refreshToken: String?,

    @SerializedName("access")
    var accessToken: String?,

    @SerializedName("role")
    var role: String?
)