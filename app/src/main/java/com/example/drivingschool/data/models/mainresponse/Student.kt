package com.example.drivingschool.data.models.mainresponse

data class Student(
    var group: Group? = null,
    var id: Int? = null,
    var lastname: String? = null,
    var name: String? = null,
    var phone_number: String? = null,
    var profile_photo: ProfilePhoto? = null,
    var surname: String? = null
)