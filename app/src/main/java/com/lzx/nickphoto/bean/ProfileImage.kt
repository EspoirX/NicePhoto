package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/4.
 */
data class ProfileImage(var small: String,
                        var medium: String,
                        var large: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(small?: "")
        parcel.writeString(medium?: "")
        parcel.writeString(large?: "")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileImage> {
        override fun createFromParcel(parcel: Parcel): ProfileImage {
            return ProfileImage(parcel)
        }

        override fun newArray(size: Int): Array<ProfileImage?> {
            return arrayOfNulls(size)
        }
    }
}