package cn.com.youheng.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.modelmsg.SendAuth
import cn.com.youheng.wechatdemo.ProjectApplication
import com.tuokejingji.tz.utils.NetUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


/**
 * Created by limxing on 2018/4/25.
 *
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProjectApplication.wxApi?.handleIntent(intent, this)

    }

    override fun onResp(resp: BaseResp?) {
        Logger.i("${resp?.errCode}")
        when (resp?.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                if (resp is SendAuth.Resp) {
                    //获取微信传回的code
                    val code = resp.code
                    val intent = Intent()
                    intent.putExtra("wx_code", code)
                    setResult(Activity.RESULT_OK, intent)
                    Logger.i(code)
                    NetUtil.instance.api?.getToken(code)
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({
                                Logger.json(it.toString())
                            }) {
                                it.printStackTrace()
                            }
                }
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> Logger.i("WXTest", "onResp ERR_USER_CANCEL ")
            BaseResp.ErrCode.ERR_AUTH_DENIED -> Logger.i("WXTest", "onResp ERR_AUTH_DENIED")
            else -> Logger.i("WXTest", "onResp default errCode " + resp?.errCode)
        }
        finish()
    }


    override fun onReq(p0: BaseReq?) {

    }
}