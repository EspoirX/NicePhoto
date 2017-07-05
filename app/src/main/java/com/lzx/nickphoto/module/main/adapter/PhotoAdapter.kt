package com.lzx.nickphoto.module.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lzx.nickphoto.R
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.utils.adapter.BaseViewHolder
import com.lzx.nickphoto.utils.adapter.LoadMoreAdapter

/**
 * Created by lzx on 2017/7/5.
 */
class PhotoAdapter(context: Context) : LoadMoreAdapter<PhotoInfo>(context) {
    override fun getViewType(position: Int): Int {
        return 0
    }

    override fun BindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        var holder: BannerHolder = viewHolder as BannerHolder
        var info: PhotoInfo = mDataList[position]
        Glide.with(context).load(info.urls.small).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mImageView);
    }

    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_photo, parent, false)
        return BannerHolder(view)
    }

    private inner class BannerHolder(itemView: View) : BaseViewHolder(itemView, context, false) {
        var mImageView: ImageView = find(R.id.image_photo)
    }
}