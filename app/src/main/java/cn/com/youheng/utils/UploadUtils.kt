package cn.com.youheng.utils

import android.content.Context
import cn.com.youheng.extensions.md5
import cn.com.youheng.extensions.md5Name
import com.netease.cloud.nos.android.core.*
import com.orhanobut.logger.Logger
import java.io.File

/**
 * Created by laplace on 2018/8/31.
 *
 */
object UploadUtils {

    fun uploadFile(context: Context, file: File, uploadNos: String, progress: (Int) -> Unit,
                   sucecss: (String) -> Unit, error: () -> Unit): UploadTaskExecutor? {
        val name = file.md5Name()
        val wanNOSObject = WanNOSObject()
        wanNOSObject.nosBucketName = "shuzhi"
        wanNOSObject.nosObjectName = name
        wanNOSObject.contentType = "image/jpeg"     // 请根据实际情况设置正确的MIME-TYPE
        wanNOSObject.uploadToken = uploadNos
        return WanAccelerator.putFileByHttp(context, file, "", "", wanNOSObject, object : Callback {
            override fun onProcess(p0: Any?, p1: Long, p2: Long) {
                progress.invoke((p1 / p2).toInt() * 100)
            }

            override fun onSuccess(p0: CallRet?) {
                Logger.i("上传成功")
                sucecss.invoke(name)
            }

            override fun onFailure(p0: CallRet?) {
                Logger.i("上传失败")
                error.invoke()
            }

            override fun onCanceled(p0: CallRet?) {
                Logger.i("上传取消")
            }

            override fun onUploadContextCreate(p0: Any?, p1: String?, p2: String?) {
            }

        })
    }


}