package com.outside.kotlineye.data.net.exception

import android.util.Log
import com.google.gson.JsonParseException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * className:    ExceptionHandle
 * description:  异常处理类
 * author:       CLW2018
 * creatTime:    2019/9/16 10:53
 */

class ExceptionHandle {

    var TAG = ""
    companion object{
        var errorCode = ErrorStatus.UNKNOWN_ERROR
        var errorMsg = "请求失败，请稍后重试"

        fun handleException(e:Throwable):String{
            e.printStackTrace()
            when(e){
                is SocketTimeoutException -> {//网络超时
                    Log.e("ExceptionHandle", "网络连接异常：" + e.message)
                    errorMsg = "网络连接异常"
                    errorCode = ErrorStatus.NETWORK_ERROR
                }

                is  ConnectException ->{ //网络错误
                    Log.e("ExceptionHandle", "网络连接异常: " + e.message)
                    errorMsg = "网络连接异常"
                    errorCode = ErrorStatus.NETWORK_ERROR
                }

                is JsonParseException, is JSONException,is ParseException ->{ //解析错误
                    Log.e("ExceptionHandle", "数据解析异常: " + e.message)
                    errorMsg = "数据解析异常"
                    errorCode = ErrorStatus.SERVER_ERROR
                }

                is ApiException -> { //服务器异常
                    Log.e("ExceptionHandle", "服务器异常: " + e.message)
                    errorMsg = e.message.toString()
                    errorCode = ErrorStatus.SERVER_ERROR
                }

                is UnknownHostException ->{//服务器地址异常
                    Log.e("ExceptionHandle", "网络连接异常: " + e.message)
                    errorMsg = "网络连接异常"
                    errorCode = ErrorStatus.NETWORK_ERROR
                }

                is IllegalArgumentException ->{//参数错误
                    Log.e("ExceptionHandle", "网络连接异常: " + e.message)
                    errorMsg = "参数错误"
                    errorCode = ErrorStatus.SERVER_ERROR
                }

                else->{
                    //未知错误
                    try {
                        Log.e("ExceptionHandle", "错误: " + e.message)
                    } catch (e1: Exception) {
                        Log.e("ExceptionHandle", "未知错误Debug调试 ")
                    }

                    errorMsg = "未知错误"
                    errorCode = ErrorStatus.UNKNOWN_ERROR
                }

            }
            return errorMsg
        }
    }
}