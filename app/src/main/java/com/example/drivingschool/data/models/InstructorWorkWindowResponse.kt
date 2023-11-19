package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorWorkWindowResponse(
    @SerializedName("dates") var dates: ArrayList<String>? = null,
    @SerializedName("times") var times: ArrayList<String>? = null,
    @SerializedName("success") val success: String? = null,

    )
