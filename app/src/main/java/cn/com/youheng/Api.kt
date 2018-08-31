package cn.com.youheng

import cn.com.youheng.bean.City
import cn.com.youheng.bean.Result
import cn.com.youheng.bean.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by laplace on 2018/8/27.
 */
interface Api {

    @GET("login")
    fun getToken(@Query("wx_code") wx_code: String): Observable<Result<Any>>

    @GET("user")
    fun getUser():Observable<Result<User>>

    @GET("city")
    fun getAllCity(@Query("parent_id") parent_id: Int): Observable<Array<City>>


    @GET("wangyi")
    fun getWangyiUpload(@Query("name")name:String): Observable<Result<Any>>

}