package com.lzx.nickphoto.callback

import io.reactivex.observers.ResourceObserver

/**
 * Created by lzx on 2017/7/5.
 */
class ListResourceObserver<T>(var callBack: ListDataCallBack<T>) : ResourceObserver<ArrayList<T>>() {

    override fun onStart() {
        super.onStart()
        callBack.onStart()
    }

    override fun onComplete() {
        callBack.onComplete()
    }

    override fun onNext(t: ArrayList<T>?) {
        callBack.OnSuccess(t!!)
    }

    override fun onError(e: Throwable?) {
        callBack.onError(e?.message.toString())
    }
}