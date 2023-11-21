package com.example.drivingschool.data.models.mainresponse

data class FeedbackForStudent(
    var created_at: String? = null,
    var instructor: InstructorX? = null,
    var lesson: Int? = null,
    var mark: String? = null,
    var text: String? = null
)