package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/6.
 */
data class PositionInfo(
        var latitude: Double,
        var longitude: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude ?: 0.0)
        parcel.writeDouble(longitude ?: 0.0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PositionInfo> {
        override fun createFromParcel(parcel: Parcel): PositionInfo {
            return PositionInfo(parcel)
        }

        override fun newArray(size: Int): Array<PositionInfo?> {
            return arrayOfNulls(size)
        }
    }
}