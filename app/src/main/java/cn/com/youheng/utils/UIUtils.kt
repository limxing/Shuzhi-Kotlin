package cn.com.youheng.utils

import android.app.Activity
import android.os.Build
import android.util.TypedValue
import android.view.View
import cn.com.youheng.ProjectApplication

/**
 * Created by limxing on 2018/4/17.
 *
 */
class UIUtils {
    companion object {
        fun setRippBac(itemView: View) {
            if (itemView.background == null) {
                val typedValue = TypedValue()
                val theme = itemView.context.theme
                val top = itemView.paddingTop
                val bottom = itemView.paddingBottom
                val left = itemView.paddingLeft
                val right = itemView.paddingRight
                if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                    itemView.setBackgroundResource(typedValue.resourceId)
                }
                itemView.setPadding(left, top, right, bottom)
            }
        }


        fun getWidth(): Int? {

            return ProjectApplication.appContext?.resources?.displayMetrics?.widthPixels
        }
        fun getHeight(): Int? {

            return ProjectApplication.appContext?.resources?.displayMetrics?.heightPixels
        }

        var statusHeight: Int? = 0

        init {
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
                statusHeight = ProjectApplication.appContext?.resources?.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun statusBarWithTitleSetHeight(view: View) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.layoutParams.height = statusHeight!!
            }
        }

        fun getDensity(): Float? {
            return ProjectApplication.appContext?.resources?.displayMetrics?.density
        }

        fun statusBarBlack(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }
    }
}