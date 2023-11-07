package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access")
    var accessTokenResponse : String?
)
