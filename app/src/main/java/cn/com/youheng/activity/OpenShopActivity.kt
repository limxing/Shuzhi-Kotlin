package cn.com.youheng.activity

import cn.com.youheng.BaseActivity
import kotlinx.android.synthetic.main.title.*

/**
 * Created by laplace on 2018/8/30.
 *
 */
class OpenShopActivity : BaseActivity() {
    override fun initData() {

    }

    override fun initView() {
        title_name.text = "开通店铺"
        title_back.setOnClickListener {
            finish()
        }
    }
}