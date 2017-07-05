package com.lzx.nickphoto.common

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by lzx on 2017/7/4.
 */
abstract class RxBaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getContentViewId() <= 0) {
            throw RuntimeException("layout resId undefind")
        }
        setContentView(getContentViewId())
        init()
    }

    /**
     * 当前布局文件资源
     */
    protected abstract fun getContentViewId(): Int

    /**
     * 初始化
     */
    protected abstract fun init()
}