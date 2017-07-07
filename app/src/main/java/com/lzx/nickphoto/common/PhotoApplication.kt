package com.lzx.nickphoto.common

import android.app.Application
import com.liulishuo.filedownloader.FileDownloader
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
        FileDownloader.setup(this)
    }
}