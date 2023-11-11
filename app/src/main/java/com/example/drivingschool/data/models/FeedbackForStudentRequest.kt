package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class FeedbackForStudentRequest(
    @SerializedName("instructor")
    val instructor: Int,
    @SerializedName("lesson")
    val lesson: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("mark")
    val mark: Int,
)
