package com.tuokejingji.tz.utils

import cn.com.youheng.utils.SPUtils
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import cn.com.youheng.wechatdemo.Api

/**
 * Created by laplace on 2018/8/15.
 *
 */
class NetUtil {

    var api: Api?
    var token: String? = null

    init {
        token = SPUtils.getString("token")

        val client: OkHttpClient = OkHttpClient.Builder()

                .addInterceptor {
                    val r = it.request()
                            .newBuilder()
                            .addHeader("AppType", "Android")
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    Logger.i("请求地址：${r?.url()} 请求类型：${r?.method()}")
                    it.proceed(r)
                }.build()



        api = Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api::class.java)
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetUtil() }
        val baseUrl = "http://192.168.111.144:5000"

    }
}