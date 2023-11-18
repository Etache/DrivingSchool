package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorWorkWindowRequest(
@SerializedName("dates") val dates: ArrayList<String>? = null,
@SerializedName("times") val times: ArrayList<String>? = null

)