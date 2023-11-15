package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InstructorResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("lastname") var lastname: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("profile_photo") var profilePhoto: ProfilePhoto? = null,
    @SerializedName("experience") var experience: Int? = null,
    @SerializedName("car") var car: String? = null,
    @SerializedName("rate") var rate: Float? = null,
    @SerializedName("feedbacks") var feedbacks: List<FeedbackInstructor>? = null,
    @SerializedName("workwindows") var workwindows: ArrayList<Date> = arrayListOf(),

)

data class WorkWindows(

    @SerializedName("2023-11-13") var date1 : ArrayList<String> = arrayListOf(),
    @SerializedName("2023-11-14") var date2 : ArrayList<String> = arrayListOf()
)