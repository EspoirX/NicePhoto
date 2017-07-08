package com.lzx.nickphoto.utils

import android.os.Environment
import android.text.TextUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils.generateFileName
import java.io.File



/**
 * Created by lzx on 2017/7/7.
 */
object FileUtil {
    /**
     * 创建下载文件夹路径
     */
    fun createPath(url: String): String? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        return getDefaultSaveFilePath(url)
    }

    /**
     * 创建下载文件夹路径
     */
    fun getDefaultSaveFilePath(url: String): String {
        return FileDownloadUtils.generateFilePath(getDownloadPath(), FileDownloadUtils.generateFileName(url) + ".png")
    }

    /**
     * 创建下载文件夹路径
     */
    fun getDownloadPath(): String {
        val filePath = Environment.getExternalStorageDirectory().path + "/NickPhoto/download"
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.path
    }

    /**
     * 查找是否存在图片文件
     */
    fun isExistsImage(url: String): Boolean {
        try {
            val filePath = getDownloadPath() + "/" + generateFileName(url) + ".png"
            val file = File(filePath)
            return file.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }
}