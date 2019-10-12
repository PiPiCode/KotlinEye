package com.outside.kotlineye.mvp.view

import com.outside.kotlineye.base.IBaseView
import com.outside.kotlineye.mvp.model.bean.HomeBean
import java.util.ArrayList

/**
 * className:    HomeView
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 9:36
 */

interface HomeView:IBaseView{

    /**
     * 设置第一次请求的数据
     */
    fun setHomeData(homeBean: HomeBean)

    fun setMoreData(newItemList: ArrayList<HomeBean.Issue.Item>)

}