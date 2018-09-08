package cn.com.youheng

import cn.com.youheng.utils.SPUtils
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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
                            .addHeader("Authorization", "token $token")
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
        val baseUrl = "http://192.168.31.221:5000"
        val imageUrl = "http://img.leefeng.top/"

    }
}