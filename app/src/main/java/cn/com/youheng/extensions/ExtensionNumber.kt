package cn.com.youheng.extensions

/**
 * Created by limxing on 2018/8/18.
 *
 */

fun Float.formatPercent(): String {
    val result = String.format("%.2f", this)
    if (result == "0.00") {
        return "0.00%"
    }
    if (this > 0) {
        return "+$result%"
    }
    return "$result%"
}

fun Double.formatPrice(s: String): String {
    return "$s$this"
}

fun Double.formatValue(rise: Boolean): String {
    val result = String.format("%.2f", this)
    if (rise) {
        return "+$result"
    }

    return "$result"

}

