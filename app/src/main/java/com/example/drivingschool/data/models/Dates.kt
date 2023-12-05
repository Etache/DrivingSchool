package com.example.drivingschool.data.models

import android.os.Parcel
import android.os.Parcelable

data class Dates (
    var dates: ArrayList<Date>
) : Parcelable {

    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        parcel.readArrayList(Date::class.java.classLoader) as ArrayList<Date>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(dates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dates> {
        override fun createFromParcel(parcel: Parcel): Dates {
            return Dates(parcel)
        }

        override fun newArray(size: Int): Array<Dates?> {
            return arrayOfNulls(size)
        }
    }
}