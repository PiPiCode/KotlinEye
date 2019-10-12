package com.outside.kotlineye.mvp.view

import com.outside.kotlineye.base.IBaseView
import com.outside.kotlineye.mvp.model.bean.HomeBean

/**
 * className:    VideoDetailView
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/10/8 17:32
 */

interface VideoDetailView:IBaseView {

    /**
     * 设置视频播放源
     */
    fun setVideo(url: String)

    /**
     * 设置视频信息
     */
    fun setVideoInfo(itemInfo: HomeBean.Issue.Item)

    /**
     * 设置背景
     */
    fun setBackground(url: String)

    /**
     * 设置最新相关视频
     */
    fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>)

    /**
     * 设置错误信息
     */
    fun setErrorMsg(errorMsg: String)


}