package cn.com.youheng.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import cn.com.youheng.activity.LoginActivity
import cn.com.youheng.bean.Result
import cn.com.youheng.extensions.getLayoutId
import cn.com.youheng.extensions.toast
import cn.com.youheng.utils.user
import io.reactivex.disposables.Disposable
import org.json.JSONObject

/**
 * Created by laplace on 2018/8/14.
 *
 */
abstract class BaseFragment : Fragment() {
    open var title = "自选"

    protected val disposeBag: ArrayList<Disposable> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
//        Logger.i("onActivityCreated ${javaClass.simpleName}")
    }


    abstract fun initData()

    abstract fun initView()


    open fun localReceiverComing(intent: Intent?) {
        for (fragment in childFragmentManager.fragments){
            (fragment as? BaseFragment)?.localReceiverComing(intent)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag
                .filterNot { it.isDisposed }
                .forEach { it.dispose() }
        disposeBag.clear()
//        Logger.i("destory view ${javaClass.simpleName}")
    }
    fun checkToLogin(): Boolean {
        if (user == null){
            startActivity(Intent(context, LoginActivity::class.java))
            return false
        }
        return true
    }

}