package com.example.drivingschool.data.models.refresh

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh")
    var refreshTokenDto: String?
)
