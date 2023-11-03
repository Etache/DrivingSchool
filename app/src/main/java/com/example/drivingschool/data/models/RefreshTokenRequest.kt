package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh")
    var refreshTokenDto: String?
)
