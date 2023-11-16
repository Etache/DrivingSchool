package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class CancelRequest(
    @SerializedName("lesson_id")
    var lessonId: String?
)
