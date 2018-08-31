package cn.com.youheng.activity

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.com.youheng.BaseActivity
import cn.com.youheng.NetUtil
import cn.com.youheng.ProjectApplication
import cn.com.youheng.R
import cn.com.youheng.extensions.addTo
import cn.com.youheng.extensions.toast
import cn.com.youheng.fragment.*
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun initData() {
        RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe({
            if (it)
                toast("授权读写本地存储")
            else {
                toast("拒绝读写本地存储")
            }
        }) {
        }.addTo(disposeBag)
    }

    override fun initView() {
        main_rg.setOnCheckedChangeListener { group, checkedId ->
            supportFragmentManager.fragments.filter { !it.isHidden }.map { supportFragmentManager.beginTransaction().hide(it).commit() }
            val fragment = when (checkedId) {
                R.id.main_tab_home -> HomeFragment.instance

                R.id.main_tab_find -> FindFragment.instance

                R.id.main_tab_vip -> VipFragment.instance
                R.id.main_tab_cart -> CartFragment.instance

                R.id.main_tab_mine -> MineFragment.instance

                else -> HomeFragment.instance

            }
            if (fragment.isAdded) {
                supportFragmentManager.beginTransaction().show(fragment).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.main_fragment, fragment).commit()
            }
        }
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, HomeFragment.instance).commit()
    }


    fun testNet(view: View) {
        NetUtil.instance.api?.let {
            it.getAllCity(0).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Logger.json(Gson().toJson(it))
                    }) {
                        it.printStackTrace()

                    }
        }
    }


}
