package com.lzx.nickphoto.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.lzx.nickphoto.R

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

    /**
     * dip 转 px
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun showSnackBar(context: Context,view: View, text: String, duration: Int = Snackbar.LENGTH_SHORT) {
        val mSnackBar: Snackbar = Snackbar.make(view, text, duration)
        mSnackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        mSnackBar.show()
    }

    fun toast(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, duration).show()
    }
}