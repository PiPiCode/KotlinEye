package com.outside.kotlineye.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.outside.kotlineye.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * className:    FragmentModule
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:30
 */

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    fun providesActivity():FragmentActivity{
        return fragment.activity!!
    }
}