package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/4.
 */
data class Links(var self: String,
                 var html: String,
                 var photos: String,
                 var likes: String,
                 var portfolio: String,
                 var following: String,
                 var followers: String,
                 var download: String,
                 var download_location: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(self)
        dest.writeString(html)
        dest.writeString(photos ?: "")
        dest.writeString(likes ?: "")
        dest.writeString(portfolio ?: "")
        dest.writeString(following ?: "")
        dest.writeString(followers ?: "")
        dest.writeString(download ?: "")
        dest.writeString(download_location ?: "")
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Links> {

        override fun createFromParcel(parcel: Parcel): Links = Links(parcel)

        override fun newArray(size: Int): Array<Links?> = arrayOfNulls(size)
    }
}


