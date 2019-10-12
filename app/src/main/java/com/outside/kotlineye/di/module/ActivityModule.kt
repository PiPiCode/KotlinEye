package com.outside.kotlineye.di.module

import androidx.appcompat.app.AppCompatActivity
import com.outside.kotlineye.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * className:    ActivityModule
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:18
 */

@Module
class ActivityModule (private val activity: AppCompatActivity){

    @Provides
    @ActivityScope
    fun providesActivity():AppCompatActivity{
        return activity
    }
}