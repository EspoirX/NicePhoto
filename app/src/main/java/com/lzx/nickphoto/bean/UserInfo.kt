package com.lzx.nickphoto.bean

/**
 * Created by lzx on 2017/7/4.
 */
data class UserInfo(val map: MutableMap<String, Any?>,
                    val profile_image: ProfileImage,
                    val links: Links) {

    var id: String by map
    var updated_at: String by map
    var username: String by map
    var name: String by map
    var first_name: String by map
    var last_name: String by map
    var twitter_username: String by map
    var portfolio_url: String by map
    var bio: String by map
    var location: String by map
    var total_likes: String by map
    var total_photos: String by map
    var total_collections: String by map


}