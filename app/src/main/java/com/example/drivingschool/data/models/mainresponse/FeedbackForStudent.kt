package com.example.drivingschool.data.models.mainresponse

import com.google.gson.annotations.SerializedName

data class FeedbackForStudent(
    @SerializedName("created_at")
    var createdAt: String? = "",
    var instructor: Instructor? = Instructor(),
    var lesson: Int? = 0,
    var mark: String? = "",
    var text: String? = ""
)