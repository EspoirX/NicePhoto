package com.lzx.nickphoto.module.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import com.google.gson.Gson
import com.lzx.nickphoto.R
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.bean.StatisticsInfo
import com.lzx.nickphoto.common.RxBaseActivity
import com.lzx.nickphoto.utils.ImageLoader
import com.lzx.nickphoto.utils.network.RetrofitHelper
import com.lzx.nickphoto.widget.ParallaxScrollView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.layout_photo_detail.*
import org.json.JSONObject

class PhotoDetailActivity : RxBaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_photo_detail
    }

    lateinit var photoId: String

    override fun init() {
        photoId = intent.getStringExtra("photoId")

        btnBack.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        scrollView.setScrollViewListener(object : ParallaxScrollView.ScrollviewListener {
            override fun onScrollChanged(scrollView: ParallaxScrollView, x: Int, y: Int, oldx: Int, oldy: Int) {
                image_photo.scrollTo(x, -y / 3)
            }
        })
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

        RetrofitHelper.instance.getPhotoAPI().getPhotoStatistics(photoId)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map {
                    jsonBody ->
                    val jsonObject: JSONObject = JSONObject(jsonBody.string())
                    var downloadTotal: String = jsonObject.getJSONObject("downloads").getString("total")
                    var viewsTotal: String = jsonObject.getJSONObject("views").getString("total")
                    var likesTotal: String = jsonObject.getJSONObject("likes").getString("total")
                    val statistics: StatisticsInfo = StatisticsInfo(downloadTotal, viewsTotal, likesTotal)
                    return@map statistics
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initStatisticsUI)

    }

    @SuppressLint("SetTextI18n")
    fun initDetailUI(info: PhotoInfo) {
        ImageLoader.loadImageWithListener(this, info.urls.small, Color.parseColor(info.color), image_photo)
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
    }

    @SuppressLint("SetTextI18n")
    fun initStatisticsUI(info: StatisticsInfo) {
        detail_likes.text = info.likesTotal
        detail_see.text = info.viewsTotal
        detail_download.text = info.downloadTotal
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
