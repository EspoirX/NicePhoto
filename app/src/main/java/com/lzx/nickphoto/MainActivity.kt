package com.lzx.nickphoto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //api文档：https://unsplash.com/documentation#user-authentication
    //Application ID： 6c18f0d4f3c1fcd37b2388ec2c543f272777584f8ed62a4bcd0fba0fe904c6f8
    //Secret： a36ad9805b0f97f3e3d553763e957c0fcc4abf0026d8602314fbea844992a6f8
    //Callback URLs： https://lzx-images.com/callback (Authorize)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
