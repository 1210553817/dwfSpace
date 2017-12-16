package com.fxqyem.utils


import android.os.Environment
import android.os.StatFs

class SDCardUtils private constructor() {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * 判断SDCard是否可用

         * @return
         */
        val isSDCardEnable: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        /**
         * 获取SD卡路径

         * @return
         */
        val sdCardPath: String
            get() {
                if (isSDCardEnable)
                    return Environment.getExternalStorageDirectory().absolutePath
                else
                    return ""
            }

        /**
         * 获取SD卡的剩余容量 单位byte

         * @return
         */
        // 获取空闲的数据块的数量
        // 获取单个数据块的大小（byte）
        val sdCardAllSize: Long
            get() {
                if (isSDCardEnable) {
                    val stat = StatFs(sdCardPath)
                    val availableBlocks = stat.availableBlocks.toLong() - 4
                    val freeBlocks = stat.availableBlocks.toLong()
                    return freeBlocks * availableBlocks
                }
                return 0
            }

        /**
         * 获取指定路径所在空间的剩余可用容量字节数，单位byte

         * @param filePath
         * *
         * @return 容量字节 SDCard可用空间，内部存储可用空间
         */
        fun getFreeBytes(filePath: String): Long {
            var filePath = filePath
            // 如果是sd卡的下的路径，则获取sd卡可用容量
            if (filePath.startsWith(sdCardPath!!)) {
                filePath = sdCardPath
            } else {// 如果是内部存储的路径，则获取内存存储的可用容量
                filePath = Environment.getDataDirectory().absolutePath
            }
            val stat = StatFs(filePath)
            val availableBlocks = stat.availableBlocks.toLong() - 4
            return stat.blockSize * availableBlocks
        }

        /**
         * 获取系统存储路径

         * @return
         */
        val rootDirectoryPath: String
            get() = Environment.getRootDirectory().absolutePath
    }
}
