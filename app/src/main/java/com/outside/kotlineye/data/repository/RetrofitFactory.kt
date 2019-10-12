package com.outside.kotlineye.data.repository

import com.outside.kotlineye.base.BaseApplication
import com.outside.kotlineye.common.UrlConstant
import com.outside.kotlineye.utils.AppPrefsUtils
import com.outside.kotlineye.utils.AppUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * className:    RetrofitFactory
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/16 11:25
 */

class RetrofitFactory private constructor() {


    lateinit var retrofit: Retrofit

    companion object {
        val instance: RetrofitFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitFactory() }
    }


    init {
        retrofit = Retrofit.Builder()
            .baseUrl(UrlConstant.BASE_URL)
            .client(getOkhttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkhttpClient(): OkHttpClient {

        val cacheFile = File(BaseApplication.context.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)

        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor())  //参数添加
            .addInterceptor(addHeaderInterceptor())
            .addInterceptor(httpLoggingInterceptor())//日志拦截器
            .cache(cache)//添加缓存
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    }

    /**
     *
     */
    private fun httpLoggingInterceptor(): Interceptor {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor

    }

    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedUrl = originalRequest.url.newBuilder()
                .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()

            val request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }

    }

    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("token", "")
                .method(originalRequest.method, originalRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    //构建API接口
    fun<T> create(clz:Class<T>):T{
        return  retrofit.create(clz)
    }

}