package cn.com.youheng.utils

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import cn.com.youheng.ProjectApplication

import java.io.*
import java.util.HashMap
import java.util.Properties

/**
 *
 * Created by limxing .
 */
object FileUtils {

    private val ROOT_DIR = "Tuoke"
    private val DOWNLOAD_DIR = "download"
    private val CACHE_DIR = "cache"
    private val ICON_DIR = "icon"

    /**   */
    val isSDCardAvailable: Boolean
        get() = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            true
        } else {
            false
        }

    /**   */
    val downloadDir: String?
        get() = getDir(DOWNLOAD_DIR)

    /**   */
    val cacheDir: String?
        get() = getDir(CACHE_DIR)

    /**   */
    val iconDir: String?
        get() = getDir(ICON_DIR)

    /** obtain external app path  */
    val externalStoragePath: String
        get() {
            val sb = StringBuilder()
            sb.append(Environment.getExternalStorageDirectory().absolutePath)
            sb.append(File.separator)
            sb.append(ROOT_DIR)
            sb.append(File.separator)
            return sb.toString()
        }

    /** obtain an app cache path  */
    val cachePath: String?
        get() {
            val f = ProjectApplication.appContext?.cacheDir
            return if (null == f) {
                null
            } else {
                f.absolutePath + "/"
            }
        }

    /**   */
    fun getDir(name: String): String? {
        val sb = StringBuilder()
        if (isSDCardAvailable) {
            sb.append(externalStoragePath)
        } else {
            sb.append(cachePath)
        }
        sb.append(name)
        sb.append(File.separator)
        val path = sb.toString()
        return if (createDirs(path)) {
            path
        } else {
            null
        }
    }

    /** creat a document  */
    fun createDirs(dirPath: String): Boolean {
        val file = File(dirPath)
        return if (!file.exists() || !file.isDirectory) {
            file.mkdirs()
        } else true
    }

    /**
     * creat a file
     * @param filePath
     * @return
     */
    fun creatFile(filePath: String): Boolean {
        val file = File(filePath)
        val f = file.parentFile
        if (!f.exists()) {
            f.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }
        return true
    }

    /** copy a file */
    fun copyFile(srcPath: String, destPath: String, deleteSrc: Boolean): Boolean {
        val srcFile = File(srcPath)
        val destFile = File(destPath)
        return copyFile(srcFile, destFile, deleteSrc)
    }

    /** copy a file  */
    fun copyFile(srcFile: File, destFile: File, deleteSrc: Boolean): Boolean {
        if (!srcFile.exists() || !srcFile.isFile) {
            return false
        }
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = FileInputStream(srcFile)
            out = FileOutputStream(destFile)
            val buffer = ByteArray(1024)
            var i = `in`.read(buffer)
            while (i > 0) {
                out.write(buffer, 0, i)
                out.flush()
                i = `in`.read(buffer)
            }
            if (deleteSrc) {
                srcFile.delete()
            }
        } catch (e: Exception) {
            return false
        } finally {
            out?.close()
            `in`?.close()
        }
        return true
    }

    /** return a file if writable  */
    fun isWriteable(path: String): Boolean {
        try {
            if (path.isNullOrEmpty()) {
                return false
            }
            val f = File(path)
            return f.exists() && f.canWrite()
        } catch (e: Exception) {
            return false
        }

    }

    /** change permission like "777" and so on  */
    fun chmod(path: String, mode: String) {
        try {
            val command = "chmod $mode $path"
            val runtime = Runtime.getRuntime()
            runtime.exec(command)
        } catch (e: Exception) {
        }

    }

    /**
     * write stream into a file
     */
    fun writeFile(`is`: InputStream?, path: String, recreate: Boolean): Boolean {
        var res = false
        val f = File(path)
        var fos: FileOutputStream? = null
        try {
            if (recreate && f.exists()) {
                f.delete()
            }
            if (!f.exists() && null != `is`) {
                val parentFile = File(f.parent)
                parentFile.mkdirs()

                val buffer = ByteArray(1024)
                fos = FileOutputStream(f)

                var count = `is`.read(buffer)

                while (count > 0) {
                    fos.write(buffer, 0, count)
                    fos.flush()
                    count = `is`.read(buffer)
                }
                res = true
            }
        } catch (e: Exception) {
        } finally {
            fos?.close()
            `is`?.close()
        }
        return res
    }

    /**
     * write byte[] into a file
     */
    fun writeFile(content: ByteArray, path: String, append: Boolean): Boolean {
        var res = false
        val f = File(path)
        var raf: RandomAccessFile? = null
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete()
                    f.createNewFile()
                }
            } else {
                f.createNewFile()
            }
            if (f.canWrite()) {
                raf = RandomAccessFile(f, "rw")
                raf.seek(raf.length())
                raf.write(content)
                res = true
            }
        } catch (e: Exception) {
        } finally {
            raf?.close()
        }
        return res
    }

    /**
     * write string to fle
     */
    fun writeFile(content: String, path: String, append: Boolean): Boolean {
        return writeFile(content.toByteArray(), path, append)
    }

    /**
     * write a value into a file with a key
     */
    fun writeProperties(filePath: String, key: String, value: String, comment: String) {
        if (key.isNullOrEmpty() || filePath.isNullOrEmpty()) {
            return
        }
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)//
            p.setProperty(key, value)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
        } finally {
            fis?.close()
            fos?.close()
        }
    }

    /** read a file which is a map file find a value from a key  */
    fun readProperties(filePath: String, key: String, defaultValue: String): String? {
        if (key.isNullOrEmpty() || filePath.isNullOrEmpty()) {
            return null
        }
        var value: String? = null
        var fis: FileInputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)
            value = p.getProperty(key, defaultValue)
        } catch (e: IOException) {
        } finally {
            fis?.close()
        }
        return value
    }

    /** write map into file  */
    fun writeMap(filePath: String, map: Map<String, String>?, append: Boolean, comment: String) {
        if (map == null || map.size == 0 || filePath.isNullOrEmpty()) {
            return
        }
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            val p = Properties()
            if (append) {
                fis = FileInputStream(f)
                p.load(fis)
            }
            p.putAll(map)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
        } finally {
            fis?.close()
            fos?.close()
        }
    }


    /**
     * rename
     * @param src
     * @param des
     * @param delete
     * @return
     */
    fun copy(src: String, des: String, delete: Boolean): Boolean {
        val file = File(src)
        if (!file.exists()) {
            return false
        }
        val desFile = File(des)
        var `in`: FileInputStream? = null
        var out: FileOutputStream? = null
        try {
            `in` = FileInputStream(file)
            out = FileOutputStream(desFile)
            val buffer = ByteArray(1024)
            var count = `in`.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                out.flush()
                count = `in`.read(buffer)
            }
        } catch (e: Exception) {
            return false
        } finally {
            `in`?.close()
            out?.close()
        }
        if (delete) {
            file.delete()
        }
        return true
    }

    /**
     * @param context
     * @return
     */
    fun getFiles(context: Context): String {
        return context.filesDir.toString() + "/"
    }

    /**
     * @param context
     * @param dirname
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copyAssetDirToFiles(context: Context, dirname: String, file: File) {
        val dir = File(file.toString() + "/" + dirname)
        dir.mkdir()

        val assetManager = context.assets
        val children = assetManager.list(dirname)
        for (c in children) {

           val child = dirname + '/'.toString() + c
            val grandChildren = assetManager.list(child)
            if (0 == grandChildren.size)
                copyAssetFileToFiles(context, child, file)
            else
                copyAssetDirToFiles(context, child, file)
        }
    }

    @Throws(IOException::class)
    private fun copyAssetFileToFiles(context: Context, filename: String, where: File) {
        val `is` = context.assets.open(filename)
        val buffer = ByteArray(`is`.available())
        `is`.read(buffer)
        `is`.close()

        val of = File(where.toString() + "/" + filename)
        of.createNewFile()
        val os = FileOutputStream(of)
        os.write(buffer)
        os.close()
    }
}
