package com.example.drivingschool.data.models.mainresponse

data class FeedbackForStudent(
    var created_at: String? = "",
    var instructor: Instructor? = Instructor(),
    var lesson: Int? = 0,
    var mark: String? = "",
    var text: String? = ""
)