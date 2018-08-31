package cn.com.youheng

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import com.netease.cloud.nos.android.core.AcceleratorConf
import com.netease.cloud.nos.android.core.WanAccelerator


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
        init_bugly()
        init_wangyi()
    }

    private fun init_wangyi() {
        WanAccelerator.setConf(AcceleratorConf())
    }

    private fun init_bugly() {
        val context = applicationContext
        val packageName = context.packageName
        val processName = getProcessName(android.os.Process.myPid())
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
//        CrashReport.initCrashReport(context, "注册时申请的APPID", isDebug, strategy)
        CrashReport.initCrashReport(context, strategy)
        CrashReport.setIsDevelopmentDevice(appContext, BuildConfig.DEBUG)
    }

    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }


    companion object {
        val WX_APP_ID = "wx458a8ea5e60318ef"
        var wxApi: IWXAPI? = null
        var appContext: Context? = null
    }
}