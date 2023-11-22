package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorWorkWindowResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("current_week") val currentWeek: ArrayList<CurrentWeek>? = null,
    @SerializedName("next_week") val nextWeek: ArrayList<NextWeek>? = null
) {
    data class CurrentWeek(
        @SerializedName("date") val date: String? = null,
        @SerializedName("times") val times: ArrayList<Time>? = null
    ) {
        data class Time(
            @SerializedName("is_free") val isFree: Boolean? = null,
            @SerializedName("time") val time: String? = null
        )
    }

    data class NextWeek(
        @SerializedName("date") val date: String? = null,
        @SerializedName("times") val times: ArrayList<Time>? = null
    ) {
        data class Time(
            @SerializedName("is_free") val isFree: Boolean? = null,
            @SerializedName("time") val time: String? = null
        )
    }
}
