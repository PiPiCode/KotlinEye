package com.outside.kotlineye.data.net.exception

import java.lang.RuntimeException

/**
 * className:    ApiException
 * description:  服务器接口访问失败 报错
 * author:       CLW2018
 * creatTime:    2019/9/16 10:50
 */

class ApiException : RuntimeException {


    private var code:Int ? = null

    constructor(throwable: Throwable,code:Int):super(throwable){
        this.code = code
    }

    constructor(message:String):super(Throwable(message))
}