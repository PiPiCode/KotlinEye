package com.outside.kotlineye.data.protocol

/**
 * className:    BaseResponse
 * description:  分装返回的数据 out 相当于 只读取，相当于Java中? extends T
 * author:       CLW2018
 * creatTime:    2019/9/16 11:18
 */

class BaseResponse<out T> (val code:Int,val msg:String,val data:T)