package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("lastname") var lastname: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("profile_photo") var profilePhoto: ProfilePhoto?=null,
    @SerializedName("group") var group: Group?

)

data class Group(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null

)

data class ProfilePhoto(
    @SerializedName("small") var small : String?=null,
    @SerializedName("medium") var medium : String?=null,
    @SerializedName("big") var big : String?=null,
    @SerializedName("large") var large : String?=null
)