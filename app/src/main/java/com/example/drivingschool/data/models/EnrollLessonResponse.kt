package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class EnrollLessonResponse(
    @SerializedName("instructor")
    val instructor : String?,
    @SerializedName("date")
    val date : String?,
    @SerializedName("time")
    val time : String?
)
