package com.lzx.nickphoto.utils.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lzx.nickphoto.R

/**
 * Created by lzx on 2017/7/5.
 */
open class BaseViewHolder : RecyclerView.ViewHolder {

    constructor(itemView: View, context: Context, addBG: Boolean) : super(itemView) {
        if (addBG) {
            val attrs = intArrayOf(R.attr.selectableItemBackground)
            val typedArray = context.obtainStyledAttributes(attrs)
            val backgroundResource = typedArray.getResourceId(0, 0)
            this.itemView.setBackgroundResource(backgroundResource)
        }
    }

    fun <T : View> find(@IdRes id: Int): T {
        return itemView.findViewById(id) as T
    }

}