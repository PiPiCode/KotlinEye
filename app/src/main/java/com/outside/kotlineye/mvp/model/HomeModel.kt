package com.outside.kotlineye.mvp.model

import com.outside.kotlineye.data.api.ApiService
import com.outside.kotlineye.data.repository.RetrofitFactory
import com.outside.kotlineye.mvp.model.bean.HomeBean
import io.reactivex.Observable
import javax.inject.Inject

/**
 * className:    HomeModel
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 15:15
 */

class HomeModel @Inject constructor(){

    fun requestHomeData(num:Int):Observable<HomeBean>{
        return RetrofitFactory.instance.create(ApiService::class.java)
            .getFirstHomeData(num)
    }

    /**
     * 加载更多
     */
    fun loadMoreData(url:String):Observable<HomeBean>{

        return RetrofitFactory.instance.create(ApiService::class.java).getMoreHomeData(url)
    }



}