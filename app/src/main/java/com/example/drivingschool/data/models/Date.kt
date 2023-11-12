package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Date(

    @SerializedName("date") var date : String? = null,
    @SerializedName("times") var times : ArrayList<TimeInWorkWindows> = arrayListOf(),

) : Serializable

data class TimeInWorkWindows(

    @SerializedName("time") var time : String? = null,
    @SerializedName("is_free") var isFree: Boolean?

)
