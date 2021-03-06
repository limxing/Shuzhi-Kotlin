package cn.com.youheng.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import cn.com.youheng.BaseActivity
import cn.com.youheng.extensions.addTo
import cn.com.youheng.extensions.toast
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_open_shop.*
import kotlinx.android.synthetic.main.title.*
import java.io.File
import cn.com.youheng.NetUtil
import cn.com.youheng.extensions.md5Name
import cn.com.youheng.utils.*
import top.zibin.luban.Luban


/**
 * Created by laplace on 2018/8/30.
 *
 */
class OpenShopActivity : BaseActivity() {
    private val CHOOSE_PHOTO: Int = 8000

    override fun initData() {
        upload.setOnClickListener {
            RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
                if (it) {

                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, CHOOSE_PHOTO)
                } else {
                    toast("请先赋予读取照片权限")
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CHOOSE_PHOTO -> ImageUtils.cropPhoto(data?.data, this, outputX = 1080)
                ImageUtils.CROP_PHOTO -> {
                    var file = File(FileUtils.tempImage)
                    Logger.i("${file.absolutePath} ${file.length() / 1024}")
                    Observable.just(file).subscribeOn(Schedulers.io())
                            .map {
                                file = Luban.with(this).load(file).get()[0]
                                file
                            }
//                    Luban.compress(this, file)
//                            .putGear(Luban.CUSTOM_GEAR)
//                            .setMaxSize(500)                // limit the final image size（unit：Kb）
//                            .setMaxHeight(1920)             // limit image height
//                            .setMaxWidth(1080)
//                            .asObservable()
//                            .observeOn(Schedulers.io())
                            .flatMap {
                                file = it
                                api?.getWangyiUpload(it.md5Name())
                            }
                            .filter { it.code == 200 }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                UploadUtils.uploadFile(this, file, it.msg ?: "", {

                                }, {
                                    Logger.i(it)
                                    toast("上传成功")
                                    Glide.with(this).load(NetUtil.imageUrl + it).into(image)
                                }) {

                                }
                            }) {
                                it.printStackTrace()
                                toast("认证失败或过期，请重新登录")

                            }.addTo(disposeBag)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initView() {
        title_name.text = "开通店铺"
        title_back.setOnClickListener {
            finish()
        }
    }
}