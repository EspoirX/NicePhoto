package com.lzx.nickphoto.module.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import com.lzx.nickphoto.R
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.bean.StatisticsInfo
import com.lzx.nickphoto.common.RxBaseActivity
import com.lzx.nickphoto.utils.CommonUtil
import com.lzx.nickphoto.utils.ImageLoader
import com.lzx.nickphoto.utils.network.RetrofitHelper
import com.lzx.nickphoto.widget.DownloadDialog
import com.lzx.nickphoto.widget.ParallaxScrollView
import com.lzx.nickphoto.widget.ProDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.layout_photo_detail.*
import org.json.JSONObject


class PhotoDetailActivity : RxBaseActivity(), View.OnClickListener {


    override fun getContentViewId(): Int {
        return R.layout.activity_photo_detail
    }

    lateinit var photoId: String
    lateinit var photoUrl: String
    lateinit var photoColor: String
    lateinit var mPhotoInfo: PhotoInfo
    lateinit var mWallpaperManager: WallpaperManager  //壁纸管理器
    lateinit var mRxPermissions: RxPermissions
    lateinit var dialog: ProDialog

    override fun init() {
        photoId = intent.getStringExtra("photoId")
        photoUrl = intent.getStringExtra("photoUrl")
        photoColor = intent.getStringExtra("photoColor")

        mWallpaperManager = WallpaperManager.getInstance(this)
        mRxPermissions = RxPermissions(this)
        dialog = ProDialog()

        btnBack.setOnClickListener { finishActivity(0) }

        //视差效果
        scrollView.setScrollViewListener(object : ParallaxScrollView.ScrollviewListener {
            override fun onScrollChanged(scrollView: ParallaxScrollView, x: Int, y: Int, oldx: Int, oldy: Int) {
                image_photo.scrollTo(x, -y / 3)
            }
        })
        //加载图片
        ImageLoader.loadImageWithListener(this, photoUrl, Color.parseColor(photoColor), image_photo)
        //请求详情
        RetrofitHelper.instance.getPhotoAPI().getPhotoDetail(photoId)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map {
                    jsonBody ->
                    val jsonObject: JSONObject = JSONObject(jsonBody.string())
                    val photoInfo: PhotoInfo = Gson().fromJson(jsonObject.toString(), PhotoInfo::class.java)
                    return@map photoInfo
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initDetailUI)
        //请求统计
        RetrofitHelper.instance.getPhotoAPI().getPhotoStatistics(photoId)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map {
                    jsonBody ->
                    val jsonObject: JSONObject = JSONObject(jsonBody.string())
                    val downloadTotal: String = jsonObject.getJSONObject("downloads").getString("total")
                    val viewsTotal: String = jsonObject.getJSONObject("views").getString("total")
                    val likesTotal: String = jsonObject.getJSONObject("likes").getString("total")
                    val statistics: StatisticsInfo = StatisticsInfo(downloadTotal, viewsTotal, likesTotal)
                    return@map statistics
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initStatisticsUI)
    }

    @SuppressLint("SetTextI18n")
    fun initDetailUI(info: PhotoInfo) {
        mPhotoInfo = info
        ImageLoader.loadImage(this, info.user.profile_image.large, Color.parseColor(info.color), userAvatar)
        nickName.text = "来自 " + info.user.name
        photoTime.text = "拍摄于 " + info.created_at.substring(0, info.created_at.indexOf("T"))
        detail_size.text = info.width + "x" + info.height
        detail_time.text = info.exif.exposure_time ?: "Unknown"
        detail_color.text = info.color
        view_color.setBackgroundColor(Color.parseColor(info.color))
        detail_aperture.text = info.exif.aperture ?: "Unknown"
        var location: String = "Unknown"
        if (info.location != null) {
            location = info.location.city + "," + info.location.country
        }
        detail_location.text = location
        detail_focal_length.text = info.exif.focal_length
        detail_camera.text = info.exif.model
        detail_exposure.text = info.exif.iso

        detail_size.setOnClickListener(this)
        detail_time.setOnClickListener(this)
        detail_color.setOnClickListener(this)
        detail_aperture.setOnClickListener(this)
        detail_location.setOnClickListener(this)
        detail_focal_length.setOnClickListener(this)
        detail_camera.setOnClickListener(this)
        detail_exposure.setOnClickListener(this)
        btn_share.setOnClickListener(this)
        btn_wallpaper.setOnClickListener(this)

        downloadImage()
    }

    @SuppressLint("SetTextI18n")
    fun initStatisticsUI(info: StatisticsInfo) {
        detail_likes.text = info.likesTotal
        detail_see.text = info.viewsTotal
        detail_download.text = info.downloadTotal
        detail_likes.setOnClickListener(this)
        detail_see.setOnClickListener(this)
        detail_download.setOnClickListener(this)
        detail_likes_text.setOnClickListener(this)
        detail_see_text.setOnClickListener(this)
        detail_download_text.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            detail_size -> CommonUtil.showSnackBar(this, detail_size, "尺寸：" + detail_size.text)
            detail_time -> CommonUtil.showSnackBar(this, detail_time, "快门时间：" + detail_time.text)
            detail_color -> CommonUtil.showSnackBar(this, detail_color, "颜色：" + detail_color.text)
            detail_aperture -> CommonUtil.showSnackBar(this, detail_aperture, "光圈：" + detail_aperture.text)
            detail_location -> CommonUtil.showSnackBar(this, detail_location, "位置：" + detail_location.text)
            detail_focal_length -> CommonUtil.showSnackBar(this, detail_focal_length, "焦距：" + detail_focal_length.text)
            detail_camera -> CommonUtil.showSnackBar(this, detail_camera, "器材：" + detail_camera.text)
            detail_exposure -> CommonUtil.showSnackBar(this, detail_exposure, "曝光率：" + detail_exposure.text)
            detail_likes, detail_likes_text -> CommonUtil.showSnackBar(this, detail_likes, "喜欢：" + detail_likes.text)
            detail_see, detail_see_text -> CommonUtil.showSnackBar(this, detail_see, "浏览次数：" + detail_see.text)
            detail_download, detail_download_text -> CommonUtil.showSnackBar(this, detail_size, "下载次数：" + detail_download.text)
            btn_share -> shareMsg()
            btn_wallpaper -> setWallpaper()
        }
    }

    /**
     * 分享
     */
    fun shareMsg() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        var shareLink: String
        if (!mPhotoInfo.links.html.contains("https")) {
            shareLink = mPhotoInfo.links.html.replace("http", "https")
        } else {
            shareLink = mPhotoInfo.links.html
        }
        textIntent.putExtra(Intent.EXTRA_TEXT, "分享自" + getString(R.string.app_name) + "\n拍摄自" + mPhotoInfo.user.name + "\n于 " + shareLink)
        startActivity(Intent.createChooser(textIntent, "分享"))
    }

    /**
     * 设置壁纸
     */
    fun setWallpaper() {
        val dialog: ProDialog = ProDialog()
        Glide.with(this).load(mPhotoInfo.urls.regular).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        dialog.dismiss()
                        mWallpaperManager.setBitmap(resource)
                        CommonUtil.showSnackBar(this@PhotoDetailActivity, btn_wallpaper, "设置壁纸成功")
                    }

                    override fun onStart() {
                        super.onStart()
                        dialog.show(supportFragmentManager, "ProDialog")
                    }

                    override fun onDestroy() {
                        super.onDestroy()
                        CommonUtil.toast(this@PhotoDetailActivity, "设置壁纸已取消")
                        dialog.dismiss()
                    }
                })
    }

    /**
     * 下载
     */
    fun downloadImage() {
        RxView.clicks(btn_download)
                .compose(bindToLifecycle())
                .compose<Boolean>(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter { aBoolean -> aBoolean }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val downloadDialog: DownloadDialog = DownloadDialog()
                    val bundle: Bundle = Bundle()
                    bundle.putString("downloadUrl", mPhotoInfo.links.download)
                    downloadDialog.arguments = bundle
                    downloadDialog.show(supportFragmentManager, "DownloadDialog")
                }, {
                    CommonUtil.toast(this@PhotoDetailActivity, "下载失败,请重试")
                })
    }


    override fun onBackPressed() {
        finishActivity(0)
    }

    override fun finishActivity(requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish()
        } else {
            finishAfterTransition()
        }
    }

}
