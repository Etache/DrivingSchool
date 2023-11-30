package com.example.drivingschool.data.models.notification

import com.google.gson.annotations.SerializedName

data class NotificationCheckResponse(
    @SerializedName("is_notification")
    val isNotification: Boolean
)
