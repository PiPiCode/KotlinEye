package com.outside.kotlineye.di.module

import com.trello.rxlifecycle3.LifecycleProvider
import dagger.Module
import dagger.Provides

/**
 * className:    LifecycleProviderModule
 * description:  生命周期
 * author:       CLW2018
 * creatTime:    2019/9/12 15:28
 */

@Module
class LifecycleProviderModule(private val lifecycleProvider: LifecycleProvider<*>) {

    @Provides
    fun providesLifeCycleProvider():LifecycleProvider<*>{
        return lifecycleProvider
    }
}