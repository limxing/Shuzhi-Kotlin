package cn.com.youheng.utils

import android.content.Context
import cn.com.youheng.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by laplace on 2018/8/23.
 */
class DateUtil {
    private val ONE_MINUTE = (60 * 1000).toLong()
    private val ONE_HOUR = (60 * 60 * 1000).toLong()
    val ONE_DAY = (24 * 60 * 60 * 1000).toLong()

    fun formateDateDiffCurrent(date: Date): String {
        try {
            val diff = System.currentTimeMillis() - date.time
            if (diff < 0) {
                return ""
            }

            if (diff in (ONE_MINUTE + 1)..(ONE_HOUR - 1)) { // 大于1分钟，小于1小时
                return (if (diff / ONE_HOUR < 0) 1 else diff / ONE_MINUTE).toString() + "分钟前"
            }

            val current = Calendar.getInstance()
            val predate = Date(System.currentTimeMillis())
            current.time = predate
            val tamp = Calendar.getInstance()
            tamp.time = date
            if (current.get(Calendar.YEAR) == tamp.get(Calendar.YEAR)) {
                val dayDiff = current.get(Calendar.DAY_OF_YEAR) - tamp.get(Calendar.DAY_OF_YEAR)
                if (dayDiff == 0) {
                    val sdr = SimpleDateFormat("HH:mm")
                    return "今天" + sdr.format(date)
                } else if (dayDiff == 1) {
                    val sdr = SimpleDateFormat("HH:mm")
                    return "昨天" + sdr.format(date)
                }
            }
            val sdr = SimpleDateFormat("MM月dd日 HH:mm")
            return sdr.format(date)
        } catch (e: Exception) {
            return ""
        }

    }
}