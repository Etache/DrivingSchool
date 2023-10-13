package com.example.drivingschool.tools.diffUtil

interface BaseDiffModel<T> {
    val id: T?
    override fun equals(other: Any?): Boolean
}
