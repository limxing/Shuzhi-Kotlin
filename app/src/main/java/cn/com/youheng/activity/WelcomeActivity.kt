package cn.com.youheng.activity

import android.Manifest
import android.content.Intent
import cn.com.youheng.BaseActivity
import cn.com.youheng.extensions.addTo
import cn.com.youheng.extensions.toast
import cn.com.youheng.utils.SPUtils
import cn.com.youheng.utils.api
import cn.com.youheng.utils.user
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by laplace on 2018/8/31.
 *
 */
class WelcomeActivity : BaseActivity() {
    override fun initData() {
        if (!SPUtils.getString("token").isNullOrEmpty()) {
            api?.let {
                it.getUser().subscribeOn(Schedulers.io()).filter { it.code == 200 }
                        .subscribe({
                            user = it.data
                        }) {

                        }.addTo(disposeBag)
            }

        }
        Observable.interval(1, TimeUnit.SECONDS).take(3).map { it == 2L }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }.addTo(disposeBag)

//        OkHttpClient.Builder().build().newCall().execute()

    }

    override fun initView() {
    }
}