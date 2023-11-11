package com.example.drivingschool.data.models

data class Time(
    val text : String ?= "8:00",
    var isActive : Boolean ?= true,
    var isSelected : Boolean ?= false
)
