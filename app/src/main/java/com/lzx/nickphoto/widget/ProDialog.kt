package com.lzx.nickphoto.widget

import com.lzx.nickphoto.R
import com.lzx.nickphoto.common.RxBaseDialog
import com.lzx.nickphoto.utils.CommonUtil

/**
 * Created by lzx on 2017/7/7.
 */
class ProDialog : RxBaseDialog() {
    override fun getLayoutResId(): Int {
        return R.layout.layout_dialog
    }

    override fun initView() {

    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(CommonUtil.dip2px(activity, 150f),CommonUtil.dip2px(activity, 150f))
    }
}