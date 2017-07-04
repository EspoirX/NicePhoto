package com.lzx.nickphoto.bean

/**
 * Created by lzx on 2017/7/4.
 */
data class PhotoInfo(val map: MutableMap<String, Any?>,
                     val userInfo: UserInfo,
                     val urls: Urls,
                     val links: Links) {
    var id: String by map
    var created_at: String by map
    var updated_at: String by map
    var width: String by map
    var height: String by map
    var color: String by map
    var likes: String by map
    var liked_by_user: String by map
    var description: String by map


}