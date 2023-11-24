package com.example.drivingschool.data.models

import com.example.drivingschool.data.models.mainresponse.Student
import com.google.gson.annotations.SerializedName

data class FeedbackInstructor(
    @SerializedName("created_at")
    val createdAt: String,
    val lesson: Int,
    val mark: String,
    val student: Student,
    val text: String
)