package com.lzx.nickphoto.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lzx on 2017/7/4.
 */
data class UserInfo(var id: String,
                    var updated_at: String,
                    var username: String,
                    var name: String,
                    var first_name: String,
                    var last_name: String  ,
                    var twitter_username: String,
                    var portfolio_url: String,
                    var bio: String,
                    var location: String  ,
                    var total_likes: String,
                    var total_photos: String,
                    var total_collections: String,
                    val profile_image: ProfileImage,
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
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(ProfileImage::class.java.classLoader),
            parcel.readParcelable(Links::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(updated_at?: "")
        parcel.writeString(username?: "")
        parcel.writeString(name?: "")
        parcel.writeString(first_name?: "")
        parcel.writeString(last_name?: "")
        parcel.writeString(twitter_username?: "")
        parcel.writeString(portfolio_url?: "")
        parcel.writeString(bio?: "")
        parcel.writeString(location?: "")
        parcel.writeString(total_likes?: "")
        parcel.writeString(total_photos?: "")
        parcel.writeString(total_collections?: "")
        parcel.writeParcelable(profile_image, flags)
        parcel.writeParcelable(links, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInfo> {
        override fun createFromParcel(parcel: Parcel): UserInfo {
            return UserInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserInfo?> {
            return arrayOfNulls(size)
        }
    }
}