package com.lzx.nickphoto.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by lzx on 2017/7/7.
 */
object GlideDownloadImageUtil {
    fun saveImageToLocal(context: Context, url: String): Observable<Uri> {
        return Observable.create(ObservableOnSubscribe<File> {
            e ->
            val file: File
            try {
                file = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                e.onNext(file)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }).flatMap { file ->
            var mFile: File? = null
            try {
                val path = Environment.getExternalStorageDirectory().toString() + File.separator + "NickPhoto"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + ".jpg"
                mFile = File(dir, fileName)
                val fis = FileInputStream(file.absolutePath)
                var byteread: Int = -1
                val buf = ByteArray(1444)
                val fos = FileOutputStream(mFile.absolutePath)
//                while ((byteread = fis.read(buf)) != -1) {
//                    fos.write(buf, 0, byteread)
//                }
                while (byteread != -1) {
                    fos.write(buf, 0, byteread)
                    byteread = fis.read(buf)
                }

                fos.close()
                fis.close()
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.all("图片下载失败")
            }
            //更新本地图库
            val uri = Uri.fromFile(mFile)
            val mIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            context.sendBroadcast(mIntent)
            Observable.just(uri)
        }.subscribeOn(Schedulers.io())
    }
}



























































