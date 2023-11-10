package com.example.drivingschool.data.models

import com.google.gson.annotations.SerializedName

data class InstructorCommentResponse(
    val car: String,
    val experience: Int,
    val feedbacks: List<Feedback>,
    val id: Int,
    val lastname: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profile_photo")
    val profilePhoto: String,
    val rate: Double,
    val surname: String
) {
    data class Feedback(
        @SerializedName("created_at")
        val createdAt: String,
        val lesson: Int,
        val mark: String,
        val student: Student,
        val text: String
    ) {
        data class Student(
            val group: Group,
            val id: Int,
            val lastname: Any,
            val name: String,
            @SerializedName("phone_number")
            val phoneNumber: String,
            @SerializedName("profile_photo")
            val profilePhoto: String,
            val surname: String
        ) {
            data class Group(
                val id: Int,
                val name: String
            )
        }
    }
}