package com.example.drivingschool.data.models.start_finish_lesson

import com.google.gson.annotations.SerializedName

data class ChangeLessonStatusResponse (
    @SerializedName("status")
    val status: String? = null
)