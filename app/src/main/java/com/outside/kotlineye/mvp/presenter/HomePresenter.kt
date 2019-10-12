package com.outside.kotlineye.mvp.presenter

import android.util.Log
import com.outside.kotlineye.base.BasePresenter
import com.outside.kotlineye.ext.excute
import com.outside.kotlineye.mvp.model.HomeModel
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.mvp.view.HomeView
import com.outside.kotlineye.rx.BaseObserver
import javax.inject.Inject

/**
 * className:    HomePresenter
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 9:36
 */

class HomePresenter @Inject constructor() : BasePresenter<HomeView>() {


    @Inject
    lateinit var homeModel: HomeModel


    private var bannerHomeBean: HomeBean? = null

    private var nextPageUrl: String? = null     //加载首页的Banner 数据+一页数据合并后，nextPageUrl没 add


    fun requestHomeData(num: Int) {

        if (!checkNetWork()) {
            return
        }

//        mView.showLoading()
        homeModel.requestHomeData(num)

            .flatMap { homeBean ->

                //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                val bannerItemList = homeBean.issueList[0].itemList

                bannerItemList.filter { item ->
                    item.type == "banner2" || item.type == "horizontalScrollCard"
                }.forEach { item ->
                    //移除 item
                    bannerItemList.remove(item)
                }

                bannerHomeBean = homeBean //记录第一页是当做 banner 数据


                //根据 nextPageUrl 请求下一页数据
                homeModel.loadMoreData(homeBean.nextPageUrl)

            }
            .excute(object : BaseObserver<HomeBean>(mView) {
                override fun onNext(homeBean: HomeBean) {
                    super.onNext(homeBean)
                    Log.e("requestHomeData", homeBean.toString())

                    nextPageUrl = homeBean.nextPageUrl
                    //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                    val newBannerItemList = homeBean.issueList[0].itemList

                    newBannerItemList.filter { item ->
                        item.type == "banner2" || item.type == "horizontalScrollCard"
                    }.forEach { item ->
                        //移除 item
                        newBannerItemList.remove(item)
                    }
                    // 重新赋值 Banner 长度
                    bannerHomeBean!!.issueList[0].count =
                        bannerHomeBean!!.issueList[0].itemList.size

                    //赋值过滤后的数据 + banner 数据
                    bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)

                    mView.setHomeData(bannerHomeBean!!)

                }
            }, lifecycleProvider)


    }

    fun loadMoreData() {
        if (!checkNetWork()) {
            return
        }

        mView.showLoading()
        nextPageUrl?.let {
            Log.e("nextPageUrl>>>",nextPageUrl)
            homeModel.loadMoreData(it)
                .excute(object : BaseObserver<HomeBean>(mView) {
                    override fun onNext(homeBean: HomeBean) {
                        super.onNext(homeBean)
                        Log.e("loadMoreData", homeBean.toString())

                        //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
                        val newItemList = homeBean.issueList[0].itemList

                        newItemList.filter { item ->
                            item.type == "banner2" || item.type == "horizontalScrollCard"
                        }.forEach { item ->
                            //移除 item
                            newItemList.remove(item)
                        }
                        nextPageUrl = homeBean.nextPageUrl
                        mView.setMoreData(newItemList)
                    }

                }, lifecycleProvider)
        }


    }


}