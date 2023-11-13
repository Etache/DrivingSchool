package com.example.drivingschool.data.models

import com.example.drivingschool.data.models.mainresponse.Student

data class FeedbackInstructor(
    val created_at: String,
    val lesson: Int,
    val mark: String ,
    val student: Student ,
    val text: String
)