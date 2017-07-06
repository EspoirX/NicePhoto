package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/6.
 */
data class ExifInfo(
        var make: String,
        var model: String,
        var exposure_time: String,
        var aperture: String,
        var focal_length: String,
        var iso: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(make ?: "")
        parcel.writeString(model ?: "")
        parcel.writeString(exposure_time ?: "")
        parcel.writeString(aperture ?: "")
        parcel.writeString(focal_length ?: "")
        parcel.writeString(iso ?: "")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExifInfo> {
        override fun createFromParcel(parcel: Parcel): ExifInfo {
            return ExifInfo(parcel)
        }

        override fun newArray(size: Int): Array<ExifInfo?> {
            return arrayOfNulls(size)
        }
    }
}