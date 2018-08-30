package cn.com.youheng.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import cn.com.youheng.activity.OpenShopActivity
import cn.com.youheng.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by laplace on 2018/8/30.
 *
 */
class MineFragment : BaseFragment() {
    override fun initData() {

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
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MineFragment() }
    }
}