package com.example.drivingschool.data.models.start_finish_lesson

import com.google.gson.annotations.SerializedName

data class StartLessonResponse (
    @SerializedName("success")
    val startSuccess: String? = null,
    @SerializedName("error")
    val startError: String? = null
)