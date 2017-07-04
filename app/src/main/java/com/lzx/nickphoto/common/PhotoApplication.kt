package com.lzx.nickphoto.common

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created by lzx on 2017/7/4.
 */
class PhotoApplication : Application() {

    companion object {
        var instance: PhotoApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}