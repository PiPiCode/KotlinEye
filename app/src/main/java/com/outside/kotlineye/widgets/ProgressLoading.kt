package com.outside.kotlineye.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.widget.ImageView
import com.outside.kotlineye.R
import org.jetbrains.anko.find

/**
 * className:    ProgressLoading
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 15:10
 */


/*
    加载对话框封装
 */
class ProgressLoading private constructor(context: Context, theme:Int) : Dialog(context,theme) {

    companion object{
        private lateinit var mDialog:ProgressLoading
        private var animDrawable: AnimationDrawable? = null

        fun create(context: Context):ProgressLoading{
            mDialog = ProgressLoading( context,R.style.LightProgressDialog)
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.setCancelable(true)
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.window?.attributes?.gravity = Gravity.CENTER
            val attributes = mDialog.window?.attributes
            //灰暗程度
            attributes?.dimAmount  = 0.2f
            mDialog.window?.attributes = attributes
            val loadingView = mDialog.find<ImageView>(R.id.iv_loading)
            animDrawable = loadingView.background as AnimationDrawable

            return mDialog
        }
    }

    fun showLoading(){
        super.show()
        animDrawable?.start()
    }

    fun hideLoading(){
        super.dismiss()
        animDrawable?.stop()
    }

}