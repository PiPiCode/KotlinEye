package com.outside.kotlineye.base


import androidx.appcompat.app.AppCompatActivity
import com.outside.kotlineye.di.component.ActivityComponent
import com.outside.kotlineye.di.component.DaggerActivityComponent
import com.outside.kotlineye.di.component.DaggerFragmentComponent
import com.outside.kotlineye.di.component.FragmentComponent
import com.outside.kotlineye.di.module.ActivityModule
import com.outside.kotlineye.di.module.FragmentModule
import com.outside.kotlineye.di.module.LifecycleProviderModule
import com.outside.kotlineye.widgets.ProgressLoading
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * className:    BaseFragment
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/16 9:40
 */

abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), IBaseView {


    @Inject
    lateinit var mPresenter: T
    lateinit var activityComponent: ActivityComponent
    lateinit var fragmentComponent: FragmentComponent

    lateinit var mLoadingDialog: ProgressLoading

    override fun initActivityInjection() {
        super.initActivityInjection()

        mLoadingDialog = ProgressLoading.create(context!!)

        activityComponent = DaggerActivityComponent.builder()
            .appComponent((activity?.application as BaseApplication).appComponent)
            .activityModule(ActivityModule(activity as AppCompatActivity))
            .lifecycleProviderModule(LifecycleProviderModule(this))//生命周期
            .build()

        fragmentComponent = DaggerFragmentComponent.builder()
            .activityComponent(activityComponent)
            .fragmentModule(FragmentModule(this))
            .build()


        injectComponent()
    }

    abstract fun injectComponent()


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