package com.lzx.nickphoto.module.main.model

import com.google.gson.Gson
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.callback.ListDataCallBack
import com.lzx.nickphoto.callback.ListResourceObserver
import com.lzx.nickphoto.utils.network.RetrofitHelper
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONArray

/**
 * Created by lzx on 2017/7/5.
 */
class PhotoModel {

    companion object {
        val per_page: Int = 15
    }

    fun getAllPhotoList(page: Int, @NonNull transformer: LifecycleTransformer<ResponseBody>, @NonNull callback: ListDataCallBack<PhotoInfo>) {
        checkNotNull(transformer)
        checkNotNull(callback)
        RetrofitHelper.instance.getPhotoAPI().getAllPhoto(page, per_page)
                .compose(transformer)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map {
                    jsonBody ->
                    val jsonArray: JSONArray = JSONArray(jsonBody.string())
                    val array: ArrayList<PhotoInfo> = ArrayList()
                    (0..(jsonArray.length() - 1))
                            .map { jsonArray.getJSONObject(it) }
                            .mapTo(array) { Gson().fromJson(it.toString(), PhotoInfo::class.java) }
                    return@map array
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ListResourceObserver(callback))
    }

}