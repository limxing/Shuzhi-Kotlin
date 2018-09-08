package cn.com.youheng.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import cn.com.youheng.ProjectApplication
import cn.com.youheng.R
import cn.com.youheng.activity.OpenShopActivity
import cn.com.youheng.base.BaseFragment
import cn.com.youheng.extensions.gone
import cn.com.youheng.extensions.visibleOrGone
import cn.com.youheng.utils.INTENT_FILTER_LOGIN_SUCCESS
import cn.com.youheng.utils.SPUtils
import cn.com.youheng.utils.api
import cn.com.youheng.utils.user
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by laplace on 2018/8/30.
 *
 */
class MineFragment : BaseFragment() {
    override fun initData() {
        user?.let {
            user_sex_iv.setImageResource(if (it.sex.id == 1) {
                R.drawable.icon_male
            } else {
                R.drawable.icon_female
            })
            user_name.text = user?.wx_name
        }
        Glide.with(this).load(user?.wx_head).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.head_male)).into(user_head)

    }

    override fun initView() {
        mine_my_shop.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("您还没有开通店铺，是否现在开通")
                    .setPositiveButton("开通", DialogInterface.OnClickListener { dialog, which ->
                        startActivity(Intent(context, OpenShopActivity::class.java))
                    }).setNegativeButton("取消", null).create().show()
        }
        wechat.visibleOrGone(SPUtils.getString("token").isNullOrEmpty())
        wechat.setOnClickListener {
            if (ProjectApplication.wxApi?.isWXAppInstalled == true) {
                val req = SendAuth.Req()
                req.scope = "snsapi_userinfo"
                req.state = "wechat_sdk_demo_test"
                ProjectApplication.wxApi?.sendReq(req)
            } else {
                Logger.i("未安装微信")
            }

        }
    }

    override fun localReceiverComing(intent: Intent?) {
        if (intent == INTENT_FILTER_LOGIN_SUCCESS) {
            wechat.gone()
            api?.let {
                it.getUser().subscribeOn(Schedulers.io()).filter { it.code == 200 }.observeOn(AndroidSchedulers.mainThread()).subscribe({
                    user = it.data
                    initData()
                }) {

                }
            }
        }
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MineFragment() }
    }
}