package com.lzx.nickphoto.bean

/**
 * Created by lzx on 2017/7/4.
 */
data class UserInfo(var id: String,
                    var updated_at: String,
                    var username: String,
                    var name: String,
                    var first_name: String,
                    var last_name: String,
                    var twitter_username: String,
                    var portfolio_url: String,
                    var bio: String,
                    var location: String,
                    var total_likes: String,
                    var total_photos: String,
                    var total_collections: String,
                    val profile_image: ProfileImage,
                    val links: Links)