package com.outside.kotlineye.base

import com.outside.kotlineye.di.component.ActivityComponent
import com.outside.kotlineye.di.component.DaggerActivityComponent
import com.outside.kotlineye.di.module.ActivityModule
import com.outside.kotlineye.di.module.LifecycleProviderModule
import com.outside.kotlineye.widgets.ProgressLoading
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * className:    BaseMvpActivity
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:49
 */

abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), IBaseView {

    @Inject
    lateinit var mPresenter: T

    lateinit var activityComponent: ActivityComponent


    override fun initActivityInjection() {
        super.initActivityInjection()
        mLoadingDialog = ProgressLoading.create(this)

        activityComponent = DaggerActivityComponent.builder()
            .appComponent((applicationContext as BaseApplication).appComponent)
            .activityModule(ActivityModule(this))
            .lifecycleProviderModule(LifecycleProviderModule(this)) //生命周期
            .build()
        injectComponent()
    }

    abstract fun injectComponent()


    lateinit var mLoadingDialog: ProgressLoading


    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }

    override fun showError(msg: String, errorCode: Int) {
        toast(msg)
    }

}