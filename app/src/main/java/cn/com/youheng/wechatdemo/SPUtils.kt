package cn.com.youheng.utils

import android.annotation.SuppressLint
import cn.com.youheng.wechatdemo.ProjectApplication

/**
 * Created by limxing on 2018/4/21.
 *
 */
class SPUtils {
    companion object {
        private val sp = ProjectApplication.appContext?.getSharedPreferences("app", 0)
        @SuppressLint("ApplySharedPref")
        fun saveString(key: String, string: String): Boolean? {
            return sp?.edit()?.putString(key, string)?.commit()
        }

        fun getString(key: String): String? {
            return sp?.getString(key, "")
        }

        fun saveInt(key: String, int: Int): Boolean? {
            return sp?.edit()?.putInt(key, int)?.commit()
        }

        fun getInt(key: String): Int? {
            return sp?.getInt(key, 0)
        }

        fun saveLong(key: String,value:Long):Boolean?{
            return sp?.edit()?.putLong(key,value)?.commit()
        }
        fun getLong(key: String):Long?{
            return sp?.getLong(key,0)
        }
    }
}