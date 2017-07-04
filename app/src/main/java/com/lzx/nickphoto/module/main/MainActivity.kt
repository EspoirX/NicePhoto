package com.lzx.nickphoto.module.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lzx.nickphoto.R
import com.lzx.nickphoto.utils.network.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    //api文档：https://unsplash.com/documentation#user-authentication
    //Application ID： 6c18f0d4f3c1fcd37b2388ec2c543f272777584f8ed62a4bcd0fba0fe904c6f8
    //Secret： a36ad9805b0f97f3e3d553763e957c0fcc4abf0026d8602314fbea844992a6f8
    //Callback URLs： https://lzx-images.com/callback (Authorize)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        RetrofitHelper.instance.getPhotoAPI().getAllPhoto()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result ->
                    val jsonArray: JSONArray = JSONArray(result.string())
                    message.text = jsonArray.toString()
                    Log.i("MainActivity", "-----success------->" + jsonArray.length())
                })

    }

//    override fun getContentViewId(): Int {
//        return R.layout.activity_main
//    }
//
//    override fun init() {
//        RetrofitHelper.instance.getPhotoAPI().getAllPhoto()
//            //    .compose(bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    result ->
//                    val jsonObject: JSONObject = JSONObject(result.string())
//                    message.text = jsonObject.toString()
//                    Log.i("MainActivity", "-----success------->" + jsonObject.toString())
//                }, {
//                    e ->
//                    Log.i("MainActivity", "-----error------->" + e.message)
//                })
//    }


}
