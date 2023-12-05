package com.example.drivingschool.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("date") var date: String? = null,
    @SerializedName("times") var times: ArrayList<TimeInWorkWindows>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(TimeInWorkWindows)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeTypedList(times)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Date> {
        override fun createFromParcel(parcel: Parcel): Date {
            return Date(parcel)
        }

        override fun newArray(size: Int): Array<Date?> {
            return arrayOfNulls(size)
        }
    }
}

data class TimeInWorkWindows(
    @SerializedName("time") var time: String? = null,
    @SerializedName("is_free") var isFree: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeValue(isFree)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeInWorkWindows> {
        override fun createFromParcel(parcel: Parcel): TimeInWorkWindows {
            return TimeInWorkWindows(parcel)
        }

        override fun newArray(size: Int): Array<TimeInWorkWindows?> {
            return arrayOfNulls(size)
        }
    }
}
