package com.lzx.nickphoto.module.main.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lzx.nickphoto.R
import com.lzx.nickphoto.bean.PhotoInfo
import com.lzx.nickphoto.module.detail.PhotoDetailActivity
import com.lzx.nickphoto.module.main.MainActivity
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
        val holder: BannerHolder = viewHolder as BannerHolder
        val info: PhotoInfo = mDataList[position]

        holder.mRootLayout.setBackgroundColor(Color.parseColor(info.color))
        Glide.with(context).load(info.urls.small)
                .placeholder(Color.parseColor(info.color))
                .error(Color.parseColor(info.color))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageView)
        holder.mImageTitle.text = info.user.name

        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(context, PhotoDetailActivity::class.java)
            intent.putExtra("photoId", info.id)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                context.startActivity(intent)
            } else {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as MainActivity,
                        Pair.create<View, String>(holder.mImageView, context.getString(R.string.transition_photo)))
                ActivityCompat.startActivity(context, intent, options.toBundle())
            }
        }
    }

    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_photo, parent, false)
        return BannerHolder(view)
    }

    private inner class BannerHolder(itemView: View) : BaseViewHolder(itemView, context, false) {
        var mImageView: ImageView = find(R.id.image_photo)
        var mImageTitle: TextView = find(R.id.image_title)
        var mRootLayout: RelativeLayout = find(R.id.root_layout)
    }


}