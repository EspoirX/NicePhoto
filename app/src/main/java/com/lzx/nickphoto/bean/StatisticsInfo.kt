package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/6.
 */
data class StatisticsInfo(
        var downloadTotal: String,
        var viewsTotal: String,
        var likesTotal: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(downloadTotal ?: "")
        parcel.writeString(viewsTotal ?: "")
        parcel.writeString(likesTotal ?: "")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatisticsInfo> {
        override fun createFromParcel(parcel: Parcel): StatisticsInfo {
            return StatisticsInfo(parcel)
        }

        override fun newArray(size: Int): Array<StatisticsInfo?> {
            return arrayOfNulls(size)
        }
    }
}