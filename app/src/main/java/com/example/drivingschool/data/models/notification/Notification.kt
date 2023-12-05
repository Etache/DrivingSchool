package com.example.drivingschool.data.models.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("created_at")
    val createdAt: String? = null,
    val id: Int? = null,
    @SerializedName("is_read")
    val isRead: Boolean? = null,
    val lesson: NotificationLesson? = null,
    val status: String? = null
)