package com.outside.kotlineye.di.component

import androidx.fragment.app.FragmentActivity
import com.outside.kotlineye.di.module.FragmentModule
import com.outside.kotlineye.di.scope.FragmentScope
import com.outside.kotlineye.ui.fragment.HomeFragment
import dagger.Component

/**
 * className:    ActivityComponent
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:26
 */
@FragmentScope
@Component(dependencies = arrayOf(ActivityComponent::class),
    modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun activity(): FragmentActivity

    fun inject(homeFragment: HomeFragment)

}