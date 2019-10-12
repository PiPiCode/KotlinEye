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

class VideoDetailModel @Inject constructor(){

    fun requestRelatedData(id:Long):Observable<HomeBean.Issue>{
        return RetrofitFactory.instance.create(ApiService::class.java)
            .getRelatedData(id)
    }



}