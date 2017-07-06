package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/4.
 */
data class Urls(var raw: String,
                var full: String,
                var regular: String,
                var small: String,
                var thumb: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(raw?: "")
        dest.writeString(full?: "")
        dest.writeString(regular?: "")
        dest.writeString(small?: "")
        dest.writeString(thumb?: "")
    }

    override fun describeContents(): Int  = 0

    companion object CREATOR : Parcelable.Creator<Urls> {
        override fun createFromParcel(parcel: Parcel): Urls {
            return Urls(parcel)
        }

        override fun newArray(size: Int): Array<Urls?> {
            return arrayOfNulls(size)
        }
    }
}