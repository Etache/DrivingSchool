package com.example.drivingschool.data.models.mainresponse

data class LessonsItem(
    var date: String? = null,
    var feedbackForInstructor: FeedbackForInstructor? = FeedbackForInstructor(),
    var feedbackForStudent: FeedbackForStudent? = FeedbackForStudent(),
    var id: Int? = null,
    var instructor: Instructor? = null,
    var status: String? = null,
    var student: Student? = null,
    var time: String? = null
)