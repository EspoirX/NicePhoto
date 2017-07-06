package com.lzx.nickphoto.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView


/**
 * Created by lzx on 2017/7/5.
 */
class ParallaxScrollView : ScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var mScrollViewListener: ScrollviewListener? = null

    fun setScrollViewListener(scrollViewListener: ScrollviewListener) {
        this.mScrollViewListener = scrollViewListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mScrollViewListener != null) {
            mScrollViewListener?.onScrollChanged(this, l, t, oldl, oldt)
        }
    }

    interface ScrollviewListener {
        fun onScrollChanged(scrollView: ParallaxScrollView, x: Int, y: Int, oldx: Int, oldy: Int)
    }
}