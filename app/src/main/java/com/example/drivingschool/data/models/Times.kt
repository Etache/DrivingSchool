package com.example.drivingschool.data.models

import java.io.Serializable

data class Times(
    val time: String? = null,
    var pressed: Boolean? = false
) : Serializable
