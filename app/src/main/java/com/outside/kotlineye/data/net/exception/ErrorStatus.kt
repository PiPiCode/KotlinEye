package com.outside.kotlineye.data.net.exception

/**
 * className:    ErrorStatus
 * description:  服务器访问成功，返回状态码
 * author:       CLW2018
 * creatTime:    2019/9/16 10:44
 */

object ErrorStatus {

    //相应成功
    const val SUCCESS = 0

    //未知错误
    const val UNKNOWN_ERROR = 1002

    //服务器内部错误
    const val SERVER_ERROR = 1003

    //网络连接超时
    const val NETWORK_ERROR = 1004

    //API 解析异常
    const val API_ERROR = 1005
}