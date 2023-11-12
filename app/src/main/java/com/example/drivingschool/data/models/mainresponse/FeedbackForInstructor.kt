package com.example.drivingschool.data.models.mainresponse

data class FeedbackForInstructor(
    var created_at: String? = null,
    var lesson: Int? = null,
    var mark: String? = null,
    var student: Student? = null,
    var text: String? = null
)