package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("profile") var profile: Profile?,
    @SerializedName("group") var group: Group?

)

data class Profile(

    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("profile_photo") var profilePhoto: String? = null,
    @SerializedName("user") var user: Int? = null

)

data class Group(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null

)