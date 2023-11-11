package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WorkWindows(

    @SerializedName("dates") var dates: ArrayList<String> = arrayListOf(),
    @SerializedName("times") var times: ArrayList<String> = arrayListOf()

) : Serializable
