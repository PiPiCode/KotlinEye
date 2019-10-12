package com.outside.kotlineye.utils

import android.content.Context
import android.widget.ImageView
import com.youth.banner.loader.ImageLoader

/**
 * className:    BannerImageLoader
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 14:43
 */

/*
    Banner图片加载器
 */
class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        GlideUtils.loadUrlImage(context, path.toString(), imageView)
    }
}
