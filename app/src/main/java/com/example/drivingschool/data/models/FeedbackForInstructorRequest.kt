package com.example.drivingschool.data.models

data class FeedbackForInstructorRequest(
    val instructor: Int,
    val lesson: Int,
    val text: String,
    val mark: Int,
)
