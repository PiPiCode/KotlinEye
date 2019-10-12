package com.outside.kotlineye.rx

import com.outside.kotlineye.base.IBaseView
import com.outside.kotlineye.data.net.exception.ExceptionHandle
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * className:    BaseObserver
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 15:20
 */

open class BaseObserver<T>(val baseView:IBaseView) :Observer<T> {

    override fun onComplete() {
        baseView.hideLoading()
    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {

        baseView.hideLoading()

        //处理异常
        baseView.showError(ExceptionHandle.handleException(e),ExceptionHandle.errorCode)

    }


}