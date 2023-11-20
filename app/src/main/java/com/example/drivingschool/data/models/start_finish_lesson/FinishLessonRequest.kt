package com.example.drivingschool.data.models.start_finish_lesson

import com.google.gson.annotations.SerializedName

data class FinishLessonRequest (
    @SerializedName("lesson_id")
    val id: String
)