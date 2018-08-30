package cn.com.youheng.extensions

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import cn.com.youheng.ProjectApplication
import cn.com.youheng.R

/**
 * Created by laplace on 2018/8/17.
 */
class MyToast(context: Context?) : Toast(context) {
    init {
        val desity = context?.resources?.displayMetrics?.density ?: 1f
        setGravity(Gravity.CENTER, 0, 0)
        val text = TextView(context)

        text.setPadding(20 * desity.toInt(), 10 * desity.toInt(), 20 * desity.toInt(), 10 * desity.toInt())
        text.setBackgroundResource(R.drawable.toast_bac)
        text.setTextColor(Color.WHITE)
        text.textSize = 18f
        view = text
    }

    override fun setText(s: CharSequence?) {
        (view as TextView).text = s
    }

    companion object {
        val instance by lazy { MyToast(ProjectApplication.appContext) }
    }
}

fun Context.toast(msg: String) {
    val toast = MyToast.instance
    toast.setText(msg)
    toast.show()
}

fun Fragment.toast(msg: String) {
    val toast = MyToast.instance
    toast.setText(msg)
    toast.show()
}