package cn.com.youheng.wechatdemo

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by laplace on 2018/8/27.
 *
 */
class ProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        appContext = applicationContext
        wxApi = WXAPIFactory.createWXAPI(applicationContext, WX_APP_ID, true)
        wxApi?.registerApp(WX_APP_ID)
    }


    companion object {
        val WX_APP_ID = "wx458a8ea5e60318ef"
        var wxApi: IWXAPI? = null
        var appContext:Context? = null
    }
}