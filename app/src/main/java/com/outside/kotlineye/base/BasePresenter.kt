package com.outside.kotlineye.base

import android.content.Context
import com.outside.kotlineye.data.net.exception.ErrorStatus
import com.outside.kotlineye.utils.NetWorkUtils
import com.trello.rxlifecycle3.LifecycleProvider
import javax.inject.Inject


/**
 * className:    BasePresenter
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:53
 */

open class BasePresenter<T:IBaseView>{
    lateinit var mView:T

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var lifecycleProvider:LifecycleProvider<*>


    fun checkNetWork():Boolean{
        if(NetWorkUtils.isNetWorkAvailable(context)){
            return true
        }
        mView.showError("网络不可用",ErrorStatus.NETWORK_ERROR)
        return false
    }

}
