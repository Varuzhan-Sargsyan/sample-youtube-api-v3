package com.api.youtube.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.api.youtube.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class ImageLoader(private val context: Context, private val defaultResId : Int = R.drawable.icon_empty) {

    private fun options() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
    private fun strategy() = DiskCacheStrategy.AUTOMATIC

    private fun loadImageIntoView(imageView: ImageView?, url: String?, block: (Boolean) -> Unit) {
        if (imageView == null) {
            block(false)
            return
        }

        if (url == null) {
            block(true)
            return
        }

        val listenerGlide = object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                block(false)
                return false;
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                block(false)
                return false
            }

        }

        Glide
            .with(context)
            .load(url)
            .addListener(listenerGlide)
            .apply(options())
            .diskCacheStrategy(strategy())
            .error(defaultResId)
            .into(imageView)
    }

    fun loadImage(viewAvatar: ImageView?, url: String?) {
        loadImageIntoView(viewAvatar, url) { passed ->
            if (!passed)
                viewAvatar?.setImageResource(defaultResId)
        }
    }

}