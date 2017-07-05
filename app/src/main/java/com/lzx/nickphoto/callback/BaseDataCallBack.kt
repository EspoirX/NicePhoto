package com.lzx.nickphoto.callback

import com.lzx.nickphoto.utils.LogUtils

/**
 * Created by lzx on 2017/7/5.
 */
interface BaseDataCallBack {

    fun onStart()

    fun onComplete()

    fun onError(msg: String) {
        LogUtils.i("onError = " + msg)
    }
}