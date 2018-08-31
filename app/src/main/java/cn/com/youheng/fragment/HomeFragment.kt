package cn.com.youheng.fragment

import android.content.Intent
import cn.com.youheng.base.BaseFragment
import cn.com.youheng.utils.INTENT_FILTER_LOGIN_SUCCESS

/**
 * Created by laplace on 2018/8/30.
 */
class HomeFragment : BaseFragment() {
    override fun initData() {

    }

    override fun initView() {
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HomeFragment() }
    }


}