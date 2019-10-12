package com.outside.kotlineye.common

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.system.exitProcess

/**
 * className:    AppManager
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/3 13:43
 */

class AppManager private constructor() {

    companion object {
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AppManager() }
    }

    private val activityStack:Stack<AppCompatActivity>  = Stack()

    /**
     * 入栈
     */
    fun addActivity(activity: AppCompatActivity){
        activityStack.add(activity)
    }

    /**
     * 出栈
     */
    fun finishActivity(activity: AppCompatActivity){
        activity.finish()
        activityStack.remove(activity)
    }

    /**
     * 获取当前栈顶
     */
    fun currentActivity():AppCompatActivity{
        return activityStack.lastElement()
    }

    /**
     * 清理栈
     */
    fun finishAllActivity(){
        activityStack.forEach {
            it.finish()
        }
        activityStack.clear()
    }
    /**
     * 退出应用
     */
    fun  exitApp(context:Context){
        finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }
}