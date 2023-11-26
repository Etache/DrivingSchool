package com.example.drivingschool.data.models.notification

data class Notification(
    val created_at: String? = null,
    val id: Int? = null,
    val is_read: Boolean? = null,
    val lesson: NotificationLesson? = null,
    val status: String? = null
)