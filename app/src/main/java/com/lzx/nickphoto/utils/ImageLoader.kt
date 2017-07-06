package com.lzx.nickphoto.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception

/**
 * Created by lzx on 2017/7/6.
 */
object ImageLoader {
    fun loadImage(context: Context, url: String, defaultImage: Int, imageView: ImageView) {
        Glide.with(context).load(url)
                .placeholder(defaultImage)
                .error(defaultImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun loadImageWithListener(context: Context, url: String, defaultImage: Int, imageView: ImageView) {
        Glide.with(context).load(url)
                .placeholder(defaultImage)
                .error(defaultImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        imageView.setImageResource(defaultImage)
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        imageView.setImageDrawable(resource)
                        return false
                    }

                })
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
    }


}