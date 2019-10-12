package com.outside.kotlineye.base

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.gyf.immersionbar.ktx.immersionBar
import com.outside.kotlineye.R
import com.outside.kotlineye.common.AppManager
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import org.jetbrains.anko.find

/**
 * className:    BaseActivity
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:25
 */

abstract class BaseActivity:RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        //沉浸式
        immersionBar {
            statusBarColor(R.color.colorPrimary)
            fitsSystemWindows(true)
        }
        //activity任务栈
        AppManager.instance.addActivity(this)

        //dagger2
        initActivityInjection()

        //初始化试图
        initView()
        //初始化监听
        initListener()
        //初始化数据
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun initActivityInjection(){}

    open fun initView(){}
    open fun initListener(){}
    open fun initData(){}



    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.finishActivity(this)
    }

    //获取根布局
    val contentView: View
        get() {
            val content = find<FrameLayout>(android.R.id.content) //content本身布局
            return content.getChildAt(0)
        }



}