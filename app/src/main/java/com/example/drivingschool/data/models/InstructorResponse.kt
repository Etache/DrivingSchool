package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorResponse(

    var id: Int? = null,
    var name: String? = null,
    var surname: String? = null,
    var lastname: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("profile_photo") var profilePhoto: ProfilePhoto? = null,
    var experience: Int? = null,
    var car: String? = null,
    var rate: Float? = null,
    var feedbacks: List<FeedbackInstructor>? = null,
    var workwindows: ArrayList<Date> ?= null

)