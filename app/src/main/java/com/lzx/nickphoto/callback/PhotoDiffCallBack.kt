package com.lzx.nickphoto.callback

import android.support.v7.util.DiffUtil
import com.lzx.nickphoto.bean.PhotoInfo

/**
 * Created by lzx on 2017/7/5.
 */
class PhotoDiffCallBack(var oldDataList: ArrayList<PhotoInfo>, var newDataList: ArrayList<PhotoInfo>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition].id == newDataList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldDataList.size
    }

    override fun getNewListSize(): Int {
        return newDataList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataList[oldItemPosition] == newDataList[newItemPosition]
    }
}