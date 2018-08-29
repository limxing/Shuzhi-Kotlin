package cn.com.youheng.wechatdemo

import cn.com.youheng.wechatdemo.bean.City
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by laplace on 2018/8/27.
 */
interface Api {

    @GET("login")
    fun getToken(@Query("wx_code") wx_code: String): Observable<Any>


    @GET("city")
    fun getAllCity(@Query("parent_id") parent_id: Int): Observable<Array<City>>

    @GET("cityy")
    fun getTest():Observable<Any>

}