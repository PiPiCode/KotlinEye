package com.outside.kotlineye.base

/**
 * className:    IBaseView
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/12 15:55
 */

interface IBaseView {
    fun showLoading()
    fun hideLoading()
    /**
     * 显示错误信息
     */
    fun showError(msg: String,errorCode:Int)

}