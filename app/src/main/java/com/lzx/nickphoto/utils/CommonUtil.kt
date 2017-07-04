package com.lzx.nickphoto.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by lzx on 2017/7/4.
 */
object CommonUtil {
    /**
     * 检查是否有网络
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info.isAvailable
    }

    private fun getNetworkInfo(context: Context): NetworkInfo {
        val cm = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

}