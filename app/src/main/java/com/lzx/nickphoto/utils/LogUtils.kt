package com.lzx.nickphoto.utils

import android.util.Log

/**
 * Created by lzx on 2017/7/5.
 */
object LogUtils {
    private val TAG = "LogUtil"

    private var isShow = true

    fun isShow(): Boolean {
        return isShow
    }

    fun setShow(show: Boolean) {
        isShow = show
    }

    fun i(tag: String, msg: String) {
        if (isShow) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (isShow) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (isShow) {
            Log.e(tag, msg)
        }
    }


    fun all(msg: String) {
        if (isShow) {
            Log.e("all", msg)
        }
    }


    fun i(msg: String) {
        if (isShow) {
            Log.i(TAG, msg)
        }
    }

    fun w(msg: String) {
        if (isShow) {
            Log.w(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (isShow) {
            Log.e(TAG, msg)
        }
    }

    fun v(msg: String) {
        e(msg)
    }


    fun d(msg: String) {
        v(msg)
    }
}