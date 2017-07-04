package com.lzx.nickphoto.bean

/**
 * Created by lzx on 2017/7/4.
 */
data class Links(val map: MutableMap<String, Any?>) {

    var self: String by map
    var html: String by map
    var photos: String by map
    var likes: String by map
    var portfolio: String by map
    var following: String by map
    var followers: String by map
    var download: String by map
    var download_location: String by map

    constructor(self: String,
                html: String,
                download: String,
                download_location: String) : this(HashMap()) {
        this.self = self
        this.html = html
        this.download = download
        this.download_location = download_location
    }

}


