package com.lzx.nickphoto.bean

/**
 * Created by lzx on 2017/7/4.
 */
data class PhotoInfo(var id: String,
                     var created_at: String,
                     var updated_at: String,
                     var width: String,
                     var height: String,
                     var color: String,
                     var likes: String,
                     var liked_by_user: String,
                     var description: String,
                     val userInfo: UserInfo,
                     val urls: Urls,
                     val links: Links)