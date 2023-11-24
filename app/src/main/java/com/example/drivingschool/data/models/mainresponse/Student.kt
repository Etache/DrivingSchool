package com.example.drivingschool.data.models.mainresponse

import com.example.drivingschool.data.models.ProfilePhoto
import com.google.gson.annotations.SerializedName

data class Student(
    var group: Group? = null,
    var id: Int? = null,
    var lastname: String? = null,
    var name: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("profile_photo")
    var profilePhoto: ProfilePhoto? = null,
    var surname: String? = null
)