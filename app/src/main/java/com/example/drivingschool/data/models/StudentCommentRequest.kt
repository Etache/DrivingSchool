package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class StudentCommentRequest(
    @SerializedName("student")
    val student: Int,
    @SerializedName("lesson")
    val lesson: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("mark")
    val mark: Int,
)
