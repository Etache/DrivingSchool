package com.example.drivingschool.data.models.mainresponse

import com.google.gson.annotations.SerializedName

data class FeedbackForInstructor(
    @SerializedName("created_at")
    var createdAt: String? = null,
    var lesson: Int? = null,
    var mark: String? = null,
    var student: Student? = null,
    var text: String? = null
)