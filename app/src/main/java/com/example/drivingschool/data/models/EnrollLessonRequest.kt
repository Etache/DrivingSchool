package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class EnrollLessonRequest(
    @SerializedName("instructor_id")
    val instructor : String,
    @SerializedName("date")
    val date : String,
    @SerializedName("time")
    val time : String
)
