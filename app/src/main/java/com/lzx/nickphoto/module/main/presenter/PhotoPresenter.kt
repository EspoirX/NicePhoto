package com.lzx.nickphoto.module.main.presenter

import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.callback.ListDataCallBack
import com.lzx.nickphoto.module.main.contract.PhotoContract
import com.lzx.nickphoto.module.main.model.PhotoModel
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.annotations.NonNull
import okhttp3.ResponseBody

/**
 * Created by lzx on 2017/7/5.
 */
class PhotoPresenter constructor(var mView: PhotoContract.IPhotoView) : PhotoContract.IPhotoPresenter {


    var model: PhotoModel = PhotoModel()
    var page: Int = 1
    var isMore: Boolean = false

    override fun getAllPhotoList(@NonNull transformer: LifecycleTransformer<ResponseBody>) {
        page = 1
        model.getAllPhotoList(page, transformer, object : ListDataCallBack<PhotoInfo> {
            override fun OnSuccess(result: ArrayList<PhotoInfo>) {
                mView.showPro(false)
                mView.OnGetPhotoSuccess(result)
                isMore = result.size >= PhotoModel.per_page
            }

            override fun onStart() {
                mView.showPro(true)
            }

            override fun onComplete() {
                mView.showPro(false)
            }

            override fun onError(msg: String) {
                super.onError(msg)
                mView.showPro(false)
                mView.OnError(msg)
            }
        })
    }

    override fun loadMorePhotoList(transformer: LifecycleTransformer<ResponseBody>) {
        if (isMore) {
            page++
            model.getAllPhotoList(page, transformer, object : ListDataCallBack<PhotoInfo> {
                override fun OnSuccess(result: ArrayList<PhotoInfo>) {
                    mView.loadMoreSuccess(result)
                    isMore = result.size >= PhotoModel.per_page
                }

                override fun onStart() {
                }

                override fun onComplete() {
                }

                override fun onError(msg: String) {
                    super.onError(msg)
                    mView.loadMoreError(msg)
                }
            })
        } else {
            mView.loadFinishAllData()
        }
    }

}