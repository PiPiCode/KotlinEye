package com.outside.kotlineye.di.component

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.outside.kotlineye.di.module.ActivityModule
import com.outside.kotlineye.di.module.LifecycleProviderModule
import com.outside.kotlineye.di.scope.ActivityScope
import com.outside.kotlineye.ui.activity.MainActivity
import com.outside.kotlineye.ui.activity.VideoDetailActivity
import com.trello.rxlifecycle3.LifecycleProvider
import dagger.Component

/**
 * className:    ActivityComponent
 * description:  所有activity的组件管理
 * author:       CLW2018
 * creatTime:    2019/9/12 15:26
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(ActivityModule::class,LifecycleProviderModule::class))
interface ActivityComponent {
    fun activity(): AppCompatActivity
    fun context(): Context
    fun lifecycleProvider():LifecycleProvider<*>

    fun inject(mainActivity: MainActivity)
    fun inject(videoDetailActivity: VideoDetailActivity)

}