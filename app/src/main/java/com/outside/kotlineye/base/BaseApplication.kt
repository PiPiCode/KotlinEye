package com.outside.kotlineye.base

import android.content.Context
import androidx.multidex.MultiDexApplication

import com.outside.kotlineye.di.component.AppComponent
import com.outside.kotlineye.di.component.DaggerAppComponent
import com.outside.kotlineye.di.module.AppModule
import com.outside.kotlineye.utils.DisplayManager

/**
 * className:    BaseApplication
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/3 9:44
 */

open class BaseApplication : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initAppInjection()
        DisplayManager.init(this)

    }

    private fun initAppInjection() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }



}