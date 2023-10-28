package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorProfileResponse(

    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("profile_photo") var profilePhoto: String? = null,
    @SerializedName("experience") var experience: Int? = null,
    @SerializedName("car") var car: String? = null,
    @SerializedName("rate") var rate: Double? = null

)