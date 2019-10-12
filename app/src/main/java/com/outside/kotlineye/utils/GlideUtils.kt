package com.outside.kotlineye.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.outside.kotlineye.R

/*
    Glide工具类
 */
object GlideUtils {
    fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).centerCrop().into(imageView)
    }

    fun loadImageFitCenter(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).fitCenter().into(imageView)
    }

    fun loadImagetCrossFade(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder_banner).centerCrop()
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }


    fun loadImagePlaceholder(context: Context, url: String, imageView: ImageView,placeholder:Int) {
        Glide.with(context)
            .load(url)
            .placeholder(placeholder).centerCrop()
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }

    fun loadImagePlaceholderCirle(context: Context, url: String, imageView: ImageView,placeholder:Int) {
        Glide.with(context)
            .load(url)
            .placeholder(placeholder).circleCrop()
            .transition(DrawableTransitionOptions().crossFade())
            .into(imageView)
    }


    /*
        当fragment或者activity失去焦点或者destroyed的时候，Glide会自动停止加载相关资源，确保资源不会被浪费
     */
    fun loadUrlImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
//            .placeholder(R.drawable.icon_back)
//            .error(R.drawable.icon_back)
            .centerCrop()
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource)
                }
            })
    }
}
