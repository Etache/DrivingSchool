package com.example.drivingschool.data.models.mainresponse

import com.example.drivingschool.data.models.ProfilePhoto

data class InstructorX(
    var car: String? = null,
    var experience: Int? = null,
    var id: Int? = null,
    var lastname: String? = null,
    var name: String? = null,
    var phone_number: String? = null,
    var profile_photo: ProfilePhoto? = null,
    var rate: Double? = null,
    var surname: String? = null
)