package com.outside.kotlineye.di.module

import android.content.Context
import com.outside.kotlineye.base.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * className:    AppModule
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:03
 */

@Module
class AppModule (private val context: BaseApplication){

    @Singleton
    @Provides
    fun providesContext():Context{
        return context
    }

}