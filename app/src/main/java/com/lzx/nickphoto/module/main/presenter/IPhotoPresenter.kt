package com.lzx.nickphoto.module.main.presenter

import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.annotations.NonNull
import okhttp3.ResponseBody

/**
 * Created by lzx on 2017/7/5.
 */
interface IPhotoPresenter {
    fun getAllPhotoList(@NonNull transformer: LifecycleTransformer<ResponseBody>)
}