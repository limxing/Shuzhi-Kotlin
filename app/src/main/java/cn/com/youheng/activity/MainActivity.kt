package cn.com.youheng.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.com.youheng.NetUtil
import cn.com.youheng.ProjectApplication
import cn.com.youheng.R
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun wechat(view: View) {
        if (ProjectApplication.wxApi?.isWXAppInstalled == true) {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "wechat_sdk_demo_test"
            ProjectApplication.wxApi?.sendReq(req)
        }else{
            Logger.i("未安装微信")
        }

    }

    fun testNet(view:View){
        NetUtil.instance.api?.let {
            it.getTest().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Logger.json(Gson().toJson(it))
                    }){
                        it.printStackTrace()

                    }
        }
    }





}
