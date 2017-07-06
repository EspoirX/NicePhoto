package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/6.
 */
data class LocationInfo(
        var title: String,
        var name: String,
        var city: String,
        var country: String,
        var position: PositionInfo
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(PositionInfo::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title ?: "")
        parcel.writeString(name ?: "")
        parcel.writeString(city ?: "")
        parcel.writeString(country ?: "")
        parcel.writeParcelable(position?:PositionInfo(0.0,0.0)  , flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationInfo> {
        override fun createFromParcel(parcel: Parcel): LocationInfo {
            return LocationInfo(parcel)
        }

        override fun newArray(size: Int): Array<LocationInfo?> {
            return arrayOfNulls(size)
        }
    }
}