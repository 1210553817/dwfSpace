package com.fxqyem.utils

import android.util.Log
import com.fxqyem.bean.LrcInfo
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.LinkedList
import java.util.regex.Pattern

object LrcParser {

    fun parseLrc(fPath: String?,duration: Int): LrcInfo? {
        var fis: InputStream? = null
        var bis: BufferedInputStream? = null
        var bs: ByteArray
        try {
            val f = File(fPath)
            if (!f.exists())
                return null
            fis = FileInputStream(f)
            if (fis == null || fis.available() <= 0)
                return null
            bs = ByteArray(fis.available())
            bis = BufferedInputStream(fis)
            bis.read(bs)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                if (fis != null) fis.close()
                if (bis != null) bis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return parseLrcA(bs,duration)
    }

    fun parseLrcA(bs: ByteArray,duration: Int): LrcInfo? {
        var encode: String? = null
        if (bs.size != 0)
            encode = judgeEncode(bs)
        val tims = LinkedList<Int>()
        val mes = LinkedList<String>()
        for(m in 0 until 4) {
            tims.add(m)
            mes.add("")
        }
        var bufferReader: BufferedReader? = null
        try {
            val fis = ByteArrayInputStream(bs)
            val inputReader = InputStreamReader(fis, encode!!)
            if (inputReader != null) {
                bufferReader = BufferedReader(inputReader)
            }
            val p = Pattern.compile("(\\[[\\d:\\.\\[\\]]+\\])(.*)")
            var lrcContent=""
            var addCount = 0
            var temp = bufferReader?.readLine()
            while (temp != null) {
                val m = p.matcher(temp!!)
                if (m.find()) {
                    for (i in 0..addCount - 1) {
                        mes.add(lrcContent!!.trim { it <= ' ' })
                    }
                    addCount = 0
                    val p1 = Pattern
                            .compile("\\[\\d{2}:\\d{2}\\.\\d{2,3}\\]")
                    val m1 = p1.matcher(m.group(1))
                    lrcContent = m.group(2)
                    while (m1.find()) {
                        val s = m1.group()
                        tims.add(timeStr2Int(s.substring(1, s.length - 1)))
                        addCount++

                    }
                } else {
                    lrcContent = lrcContent + temp

                }
                temp = bufferReader?.readLine()
            }
            for (j in 0 until addCount) {
                mes.add(lrcContent.trim { it <= ' ' })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                bufferReader!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        for(m in 1 until 5) {
            var latm=duration-m*400
            if(latm<0)latm = 0
            tims.add(latm)
            mes.add("")
        }
        val timemills = IntArray(tims.size)
        val messages = arrayOfNulls<String>(tims.size)
        for (i in tims.indices) {
            timemills[i] = tims[i]
            messages[i] = mes[i]
        }
        /*sort*/
        var b = false //sort is over flag
        var m = 0
        while (m < timemills.size && !b) {
            b = true
            var n = m
            while(n<timemills.size-1){
                if (timemills[n] > timemills[n + 1]) {
                    val tim = timemills[n]
                    val msg = messages[n]
                    timemills[n] = timemills[n + 1]
                    timemills[n + 1] = tim
                    messages[n] = messages[n + 1]
                    messages[n + 1] = msg
                    b = false
                }
                n++
            }
            m++
        }
        val lrcInfo = LrcInfo(timemills, messages)
        return lrcInfo
    }
    // //Pattern pattern=Pattern.compile("\\[([^\\]]+)\\]");

    /**
     * 区分GBK和utf-8编码

     * @param bs
     * *
     * @return
     */
    fun judgeEncode(bs: ByteArray): String {
        val ln = if (bs.size > 64) 64 else bs.size
        for (i in 0 until ln) {
            if (bs[i].toInt() and 240  == 240)
                return "GBK"
            if (bs[i].toInt() and 224 == 192) {
                if(i + 1>ln-1) return "utf-8"
                if (bs[i + 1].toInt() and 192 == 128)
                    continue
                else
                    return "GBK"
            }
            if (bs[i].toInt() and 240 == 224) {
                if(i + 2>ln-1) return "utf-8"
                if (bs[i + 1].toInt() and 192 == 128 && bs[i + 2] .toInt() and 192  == 128 ) {
                    continue
                } else {
                    return "GBK"
                }
            }
            if (bs[i].toInt() and 192 == 128 ) {
                if (i == 0) {
                    return "GBK"
                } else {
                    if (bs[i - 1].toInt() and 192 == 128 || bs[i - 1].toInt() and 224 == 192
                            || bs[i - 1].toInt() and 240  == 224)
                        continue
                    else
                        return "GBK"
                }

            }

        }
        return "utf-8"
    }

    private fun timeStr2Int(Str: String): Int {
        val s1 = Str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val min = Integer.parseInt(s1[0])
        val s2 = s1[1].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sec = Integer.parseInt(s2[0])
        var mill = 0
        if (s2[1].length == 2)
            mill = 10 * Integer.parseInt(s2[1])
        else
            mill = Integer.parseInt(s2[1])
        return ((min * 60000).toLong() + sec * 1000L + mill.toLong()).toInt()
    }
}

