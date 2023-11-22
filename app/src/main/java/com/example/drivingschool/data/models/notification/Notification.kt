package com.example.drivingschool.data.models.notification

import com.example.drivingschool.data.models.mainresponse.LessonsItem

data class Notification(
    val created_at: String,
    val id: Int,
    val is_read: Boolean,
    val lesson: LessonsItem,
    val status: String
)