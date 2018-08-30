package cn.com.youheng

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import cn.com.youheng.utils.UIUtils
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import cn.com.youheng.base.BaseFragment
import cn.com.youheng.bean.Result
import cn.com.youheng.extensions.getLayoutId
import cn.com.youheng.extensions.toast
import cn.com.youheng.utils.INTENT_FILTER_LOGIN_SUCCESS
import cn.com.youheng.utils.INTENT_FILTER_NET_CHANGED
import cn.com.youheng.utils.SELECTION_NEED_FRESH
import cn.com.youheng.utils.SELECTION_NEED_REFRESH
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.title.*

/**
 * Created by laplace on 2018/7/26.
 *
 */
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    protected val disposeBag: ArrayList<Disposable> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //强制竖屏
//        try {
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        initWindow()
        setContentView(getLayoutId())
        if (title_top != null) {
            UIUtils.statusBarWithTitleSetHeight(title_top)
        }
        initView()
        initData()

        filter.addAction(INTENT_FILTER_LOGIN_SUCCESS)
        filter.addAction(SELECTION_NEED_FRESH)
        filter.addAction(SELECTION_NEED_REFRESH)
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, filter)
    }

    protected open fun initWindow() {
        UIUtils.statusBarBlack(this)
    }

    private val filter = IntentFilter(INTENT_FILTER_NET_CHANGED)
    private val localReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            localReceiverComing(intent)
        }

    }

    open fun localReceiverComing(intent: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            (fragment as? BaseFragment)?.localReceiverComing(intent)
        }
    }

    abstract fun initData()

    abstract fun initView()


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver)
        disposeBag
                .filterNot { it.isDisposed }
                .forEach { it.dispose() }
        disposeBag.clear()
    }

    /**
     * 检测网络请求是否正确
     * 错误弹窗
     */
    fun checkResult(result: Result<*>): Boolean {
        Logger.json(Gson().toJson(result))
        return when {
            result.code == 0 -> true
            result.code == 1 -> {
                toast(result.msg ?: "")
                false
            }

            else -> false
        }
    }
}