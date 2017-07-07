package com.lzx.nickphoto.widget

import android.annotation.SuppressLint
import android.widget.ProgressBar
import android.widget.TextView
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.lzx.nickphoto.R
import com.lzx.nickphoto.common.RxBaseDialog
import com.lzx.nickphoto.utils.CommonUtil
import com.lzx.nickphoto.utils.FileUtil
import java.math.BigDecimal

/**
 * Created by lzx on 2017/7/7.
 */
class DownloadDialog : RxBaseDialog() {

    override fun getLayoutResId(): Int {
        return R.layout.dialog_download
    }

    lateinit var downloadUrl: String
    lateinit var mProBar: ProgressBar
    lateinit var mProTitle: TextView
    lateinit var mImageSizeView: TextView

    override fun initView() {


        downloadUrl = arguments.getString("downloadUrl")

        mProBar = findViewById(R.id.pro_bar) as ProgressBar
        mProTitle = findViewById(R.id.pro_title) as TextView
        mImageSizeView = findViewById(R.id.image_size) as TextView

        FileDownloader.getImpl().create(downloadUrl)
                .setPath(FileUtil.createPath(downloadUrl))
                .setListener(object : FileDownloadListener() {
                    override fun warn(task: BaseDownloadTask?) {
                        CommonUtil.toast(activity, "下载失败")
                        dismiss()
                    }

                    override fun completed(task: BaseDownloadTask?) {
                        mProBar.isIndeterminate = false
                        mProBar.progress = task?.smallFileTotalBytes!!
                        val msg = String.format("图片已保存至 %s 文件夹", "NickPhoto/download")
                        CommonUtil.toast(activity, msg)
                        dismiss()
                    }

                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        mProTitle.text = "下载准备中(稍等哦)..."
                        mProBar.isIndeterminate = true
                    }

                    override fun error(task: BaseDownloadTask?, e: Throwable?) {
                        CommonUtil.toast(activity, "下载失败")
                        dismiss()
                    }

                    @SuppressLint("SetTextI18n")
                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        mProBar.isIndeterminate = false
                        mProBar.max = totalBytes
                        mProBar.progress = soFarBytes
                        mProTitle.text = "下载中..." + getCurrPro(soFarBytes, totalBytes)
                        mImageSizeView.text = "大小:" + getFormatPercent(totalBytes.toFloat())

                    }

                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        mProTitle.text = "下载暂停中..."
                    }
                }).start()


    }

    override fun onPause() {
        super.onPause()
        FileDownloader.getImpl().pauseAll()
    }

    fun getFormatPercent(percent: Float): String {
        return byteToMb(percent.toLong()).toString() + "M"
    }

    fun byteToMb(bytes: Long): Float {
        val filesize: BigDecimal = BigDecimal(bytes)
        val megabyte: BigDecimal = BigDecimal(1024 * 1024)
        var returnValue: Float = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).toFloat()
        if (returnValue > 1) {
            return returnValue
        }
        return 0f
    }

    fun getCurrPro(soFarBytes: Int, totalBytes: Int): String {
        var proText: String = ((soFarBytes.toFloat() / totalBytes.toFloat()) * 100).toString()
        if (proText.contains(".")) {
            proText = proText.substring(0, proText.indexOf("."))
        }
        return proText + "%"
    }


}

