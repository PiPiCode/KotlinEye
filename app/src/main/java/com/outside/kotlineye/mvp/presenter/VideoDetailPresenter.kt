package com.outside.kotlineye.mvp.presenter

import android.app.Activity
import android.util.Log

import com.outside.kotlineye.base.BaseApplication
import com.outside.kotlineye.base.BasePresenter
import com.outside.kotlineye.data.net.exception.ExceptionHandle
import com.outside.kotlineye.ext.dataFormat
import com.outside.kotlineye.mvp.model.VideoDetailModel
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.mvp.view.VideoDetailView
import com.outside.kotlineye.utils.DisplayManager
import com.outside.kotlineye.utils.NetWorkUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * className:    VideoDetailPresenter
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/10/8 17:32
 */

class VideoDetailPresenter @Inject constructor():BasePresenter<VideoDetailView>() {



    @Inject
    lateinit var videoDetailModel: VideoDetailModel

    /**
     * 加载视频相关的数据
     */

    fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {

        val playInfo = itemInfo.data?.playInfo

        val netType = NetWorkUtils.isWifiConnected(BaseApplication.context)

        if (playInfo!!.size > 1) {
            // 当前网络是 Wifi环境下选择高清的视频
            if (netType) {
                for (i in playInfo) {
                    if (i.type == "high") {
                        val playUrl = i.url
                        mView?.setVideo(playUrl)
                        break
                    }
                }
            } else {
                //否则就选标清的视频
                for (i in playInfo) {
                    if (i.type == "normal") {
                        val playUrl = i.url
                        mView?.setVideo(playUrl)
                        //Todo 待完善
                        (mView as Activity).toast("本次消耗${(mView as Activity).
                            dataFormat(i.urlList[0].size)}流量")
                        break
                    }
                }
            }
        } else {
            mView?.setVideo(itemInfo.data.playUrl)
        }

        //设置背景
        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        backgroundUrl?.let { mView?.setBackground(it) }

        mView?.setVideoInfo(itemInfo)


    }

    /**
     * 请求相关的视频数据
     */

    fun requestRelatedVideo(l: Long) {

        if (!checkNetWork()) {
            return
        }

//        mView.showLoading()


        //需要自定义处理规则
        videoDetailModel.requestRelatedData(l)
            .observeOn(AndroidSchedulers.mainThread())
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<HomeBean.Issue> {
                override fun onComplete() {
                    mView.hideLoading()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: HomeBean.Issue) {
                    mView.hideLoading()
                    mView.setRecentRelatedVideo(t.itemList)
                }

                override fun onError(e: Throwable) {
                    mView.hideLoading()
                    mView.setErrorMsg(ExceptionHandle.handleException(e))
                }

            })

    }


}