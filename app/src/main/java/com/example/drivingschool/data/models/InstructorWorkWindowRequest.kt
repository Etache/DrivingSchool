package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorWorkWindowRequest(
    @SerializedName("date") var date: List<String>? = null,
    @SerializedName("time") var time: List<String>? = null
)