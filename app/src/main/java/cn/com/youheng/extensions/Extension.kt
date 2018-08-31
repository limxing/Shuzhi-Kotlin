package cn.com.youheng.extensions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.com.youheng.utils.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import cn.com.youheng.ProjectApplication
import cn.com.youheng.R
import cn.com.youheng.utils.user
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.Timestamp
import java.util.regex.Pattern


/**
 * Created by laplace on 2018/8/15.
 * 扩展函数
 */
fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun TextView.visible(msg: String) {
    visible()
    text = msg
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible && visibility != View.VISIBLE) {
        visible()
    } else if (!visible && visibility != View.GONE) {
        gone()
    }

}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}


fun SwipeRefreshLayout.setColors() {
    setColorSchemeResources(R.color.colorAccent)
}

fun ImageView.load(url: String?, holder: Int) {
    url?.let {
        Glide.with(context)
                .applyDefaultRequestOptions(RequestOptions()
                        .placeholder(holder)
                        .circleCrop())
                .load(it)
                .into(this)
    }

}


fun Disposable.addTo(bag: ArrayList<Disposable>) {
    if (!bag.contains(this))
        bag.add(this)
}


fun Fragment.getLayoutId(): Int {
    return context?.getLayoutId(javaClass.simpleName) ?: 0
}


fun Context.getLayoutId(pageName: String = javaClass.simpleName): Int {
    var type = "Activity"
    if (pageName.endsWith("Fragment")) {
        type = "Fragment"
    }
    val name = pageName.substring(0, pageName.indexOf(type))
    val chars = name.toCharArray()
    val buf = StringBuffer()
    buf.append(type.toLowerCase())
    for (c: Char in chars) {
        if (c in 'A'..'Z') {
            buf.append("_" + c.toLowerCase())
        } else {
            buf.append(c)
        }
    }
    return resources.getIdentifier(buf.toString(), "layout", packageName)
}

/**
 * 初始化tab
 * 根据背景色调整
 */
fun TabLayout.Tab.initWithBac(id: Int, context: Context?) {
    val textView = TextView(context)
    textView.text = text
    textView.setBackgroundResource(id)
    textView.setTextColor(context?.resources?.getColorStateList(R.color.title_tab_text))
    textView.gravity = Gravity.CENTER
    customView = textView
    textView.layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
    textView.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT

}

/**
 * 用于简单保存图片的功能
 */
fun FragmentActivity.saveImage(imgUrl: String?) {
    RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
        if (it) {

            Glide.with(this).downloadOnly().load(imgUrl).listener(object : RequestListener<File> {
                override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (ImageUtils.saveImageToGallery(ProjectApplication.appContext, resource?.readBytes(), "tuoke.jpg") == true) {
                        toast("保存成功")
                    } else {
                        toast("保存失败")
                    }
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                    return false
                }

            }).preload()
        } else {
            toast("您拒绝了保存到本地")
        }
    }
}

/**
 * 开启一个提示dialog
 */
fun FragmentActivity.showAlert(msg: String) {
    val alert = AlertDialog.Builder(this)
    alert.setTitle("提示")
    alert.setMessage(msg)
    alert.setNegativeButton("知道啦", null)
    alert.create().show()
}

fun String.checkPhone(): Boolean {
    return Pattern.matches("^[1][3-9][0-9]{9}$", this)
}

fun Context.showMessageDialog(msg: String) {
    val alert = AlertDialog.Builder(this)
    alert.setMessage(msg)
    alert.setNegativeButton("确实", null)
    alert.create().show()
}

/**
 * 字符串MD5加密
 */
fun String.md5(): String {
    try {
        //获取md5加密对象
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        //对字符串加密，返回字节数组
        val digest: ByteArray = instance.digest(this.toByteArray())
        var sb: StringBuffer = StringBuffer()
        for (b in digest) {
            //获取低八位有效值
            var i: Int = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0" + hexString
            }
            sb.append(hexString)
        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

fun File.md5Name(): String {
    return "${user?.uuid}_$nameWithoutExtension".md5() + "." + name.substringAfterLast('.')
}


