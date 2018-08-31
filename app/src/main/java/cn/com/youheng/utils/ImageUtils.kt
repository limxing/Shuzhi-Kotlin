package cn.com.youheng.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import com.orhanobut.logger.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by limxing on 2018/4/22.
 *
 */
object ImageUtils {
    internal fun handleImageBeforeKitKat(data: Intent?, activity: Activity): String? {
        return data?.data?.let { getImagePath(it, null, activity) }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    internal fun handleImageOnKitKat(data: Intent?, activity: Activity): String? {
        var imagePath: String? = null
        val uri = data?.getData()
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, activity)
            } else if ("com.android.providers.downloads.documents" == uri?.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId)!!)
                imagePath = getImagePath(contentUri, null, activity)
            }
        } else if ("content".equals(uri?.scheme, ignoreCase = true)) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri!!, null, activity)
        } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri?.path
        }
//            Logger.i(imagePath)
//            cropPhoto(uri, activity)
        return imagePath
    }

    private fun getImagePath(uri: Uri, selection: String?, activity: Activity): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor = activity.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    val CROP_PHOTO: Int = 8001

    //开启裁剪
    fun cropPhoto(uri: Uri?, activity: Activity, aspectY: Int = 1, outputX: Int = 0) {
        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setDataAndType(uri, "image/*")
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true")
        intent.putExtra("scale", true)
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", aspectY)
        if (outputX != 0) {
            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", outputX)
            intent.putExtra("outputY", outputX * aspectY)
        }

        intent.putExtra("return-data", false)
        intent.putExtra("noFaceDetection", true)

        val out = File(FileUtils.tempImage)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        activity.startActivityForResult(intent, CROP_PHOTO)

    }


    fun getImagePathFromIntent(data: Intent, activity: Activity): String? {
        return if (Build.VERSION.SDK_INT >= 19) {
            // 4.4及以上系统使用这个方法处理图片
            handleImageOnKitKat(data, activity)
        } else {
            // 4.4以下系统使用这个方法处理图片
            handleImageBeforeKitKat(data, activity)
        }
    }


    /**
     * 保存bitmap到相册
     */
    fun saveImageToGallery(context: Context?, bitmap: ByteArray?, name: String? = "shuzi"): Boolean? {
        if (bitmap == null) {
            return false
        }
        try {
            val appDir = FileUtils.getDir("share")
            val file = File(appDir, name)
            Logger.i(file.absolutePath)
            val fos = FileOutputStream(file)

            //通过io流的方式来压缩保存图片
            fos.write(bitmap)
//                val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            MediaStore.Images.Media.insertImage(context?.contentResolver, file.absolutePath, name, null)
            file.delete()
            val uri = Uri.fromFile(file)
            context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return true
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * 保存bitmap到相册
     */
    fun saveImageToGallery(context: Context?, bitmap: Bitmap?, name: String? = "tuoke"): Boolean? {
        if (bitmap == null) {
            return false
        }
        try {
            val appDir = FileUtils.getDir("share")
            val file = File(appDir, name)
            Logger.i(file.absolutePath)
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            MediaStore.Images.Media.insertImage(context?.contentResolver, file.absolutePath, name, null)
            file.delete()
            val uri = Uri.fromFile(file)
            context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return isSuccess
        } catch (e: Exception) {
            return false
        }

    }


}