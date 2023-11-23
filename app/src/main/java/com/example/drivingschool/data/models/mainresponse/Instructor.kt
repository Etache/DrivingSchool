package com.example.drivingschool.data.models.mainresponse

import com.example.drivingschool.data.models.ProfilePhoto
import com.google.gson.annotations.SerializedName

data class Instructor(
    var car: String? = null,
    var experience: Int? = null,
    var id: Int? = null,
    var lastname: Any? = null,
    var name: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("profile_photo")
    var profilePhoto: ProfilePhoto? = null,
    var rate: Double? = null,
    var surname: String? = null
)