package com.lzx.nickphoto.module.main.contract

import com.lzx.nickphoto.bean.PhotoInfo
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.annotations.NonNull
import okhttp3.ResponseBody

/**
 * Created by lzx on 2017/7/5.
 */
interface PhotoContract {

    interface IPhotoPresenter {
        fun getAllPhotoList(@NonNull transformer: LifecycleTransformer<ResponseBody>)
    }

    interface IPhotoView {
        fun showPro(isShow: Boolean)
        fun OnGetPhotoSuccess(result: ArrayList<PhotoInfo>)
        fun OnError(msg: String)
    }

}