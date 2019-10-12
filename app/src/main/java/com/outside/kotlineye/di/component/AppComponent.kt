package com.outside.kotlineye.di.component

import android.content.Context
import com.outside.kotlineye.di.module.AppModule
import com.outside.kotlineye.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * className:    AppComponent
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:02
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun conext(): Context

}