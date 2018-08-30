package cn.com.youheng.utils

import cn.com.youheng.BuildConfig
import cn.com.youheng.NetUtil
import cn.com.youheng.bean.User
import com.google.gson.Gson

/**
 * Created by laplace on 2018/8/15.
 *
 */

val INTENT_FILTER_NET_CHANGED = "${BuildConfig.APPLICATION_ID}.net.changed"
val INTENT_FILTER_LOGIN_SUCCESS = "${BuildConfig.APPLICATION_ID}.login.success"
val SELECTION_NEED_REFRESH = "${BuildConfig.APPLICATION_ID}.selection.needrefresh"
val SELECTION_NEED_FRESH = "${BuildConfig.APPLICATION_ID}.selection.needfresh"
val api = NetUtil.instance.api
var user: User? = null

val gson = Gson()
