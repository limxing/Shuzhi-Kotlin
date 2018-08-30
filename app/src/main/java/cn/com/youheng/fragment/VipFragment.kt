package cn.com.youheng.fragment

import cn.com.youheng.base.BaseFragment

/**
 * Created by laplace on 2018/8/30.
 */
class VipFragment : BaseFragment() {
    override fun initData() {

    }

    override fun initView() {
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { VipFragment() }
    }
}