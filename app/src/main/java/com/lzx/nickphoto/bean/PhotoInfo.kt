package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/4.
 */
data class PhotoInfo(var id: String,
                     var created_at: String,
                     var updated_at: String,
                     var width: String,
                     var height: String,
                     var color: String,
                     var slug: String,
                     var downloads: String,

                     var likes: String,
                     var views: String,
                     var liked_by_user: Boolean,
                     var description: String,
                     var exif: ExifInfo,
                     var location: LocationInfo,

                     val user: UserInfo,
                     val urls: Urls,
                     val links: Links) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readParcelable(ExifInfo::class.java.classLoader),
            parcel.readParcelable(LocationInfo::class.java.classLoader),
            parcel.readParcelable(UserInfo::class.java.classLoader),
            parcel.readParcelable(Urls::class.java.classLoader),
            parcel.readParcelable(Links::class.java.classLoader))


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id ?: "")
        parcel.writeString(created_at ?: "")
        parcel.writeString(updated_at ?: "")
        parcel.writeString(width ?: "")
        parcel.writeString(height ?: "")
        parcel.writeString(color ?: "")
        parcel.writeString(slug ?: "")
        parcel.writeString(downloads ?: "")
        parcel.writeString(likes ?: "")
        parcel.writeString(views ?: "")
        parcel.writeByte(if (liked_by_user ?: false) 1 else 0)
        parcel.writeString(description ?: "")
        parcel.writeParcelable(exif ?: ExifInfo("", "", "", "", "", ""), flags)
        parcel.writeParcelable(location ?: LocationInfo("", "", "", "", PositionInfo(0.0, 0.0)), flags)
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(urls, flags)
        parcel.writeParcelable(links, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoInfo> {
        override fun createFromParcel(parcel: Parcel): PhotoInfo {
            return PhotoInfo(parcel)
        }

        override fun newArray(size: Int): Array<PhotoInfo?> {
            return arrayOfNulls(size)
        }
    }
}