package com.lzx.nickphoto.common

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lzx.nickphoto.R
import com.trello.rxlifecycle2.components.support.RxDialogFragment

/**
 * Created by lzx on 2017/7/7.
 */
abstract class RxBaseDialog : RxDialogFragment() {

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parentView = inflater!!.inflate(getLayoutResId(), container, false)
        initView()
        return parentView
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initView()

    protected fun findViewById(id: Int): View {
        return parentView!!.findViewById(id)
    }
}