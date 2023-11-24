package com.example.drivingschool.data.models.notification
import com.example.drivingschool.data.models.mainresponse.Instructor
import com.example.drivingschool.data.models.mainresponse.Student

data class NotificationLesson(
    var date: String? = null,
    var id: Int? = null,
    var instructor: Instructor? = null,
    var status: String? = null,
    var student: Student? = null,
    var time: String? = null
)
