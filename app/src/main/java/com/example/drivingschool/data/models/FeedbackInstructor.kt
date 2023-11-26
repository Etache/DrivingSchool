package com.example.drivingschool.data.models

import com.example.drivingschool.data.models.mainresponse.Student
import com.google.gson.annotations.SerializedName

data class FeedbackInstructor(
    @SerializedName("created_at")
    val createdAt: String? = null,
    val lesson: Int? = null,
    val mark: String? = null,
    val student: Student? = null,
    val text: String? = null
)