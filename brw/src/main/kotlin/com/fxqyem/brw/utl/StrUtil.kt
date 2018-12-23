package com.fxqyem.brw.utl

import android.util.Log
import java.text.SimpleDateFormat

object StrUtil {
    fun notNull(str:String?): Boolean {
        str?: return false
        return true
    }
    fun isInt(str:String): Boolean {
        val regx = Regex("""^\d+$""")
        return regx.matches(str)
    }

    fun formatDatea(date: java.util.Date): String{
        return formatDate(date,"yyyy-MM-dd HH:mm:ss")
    }
    fun formatDate(date: java.util.Date,pattern: String): String{
        var restr = ""
        val sdf = SimpleDateFormat(pattern)
        try {
            restr = sdf.format(date)
        }catch (e: Exception){
            Log.e(TAG,"format Date error! \n"+e.message)
        }
        return restr
    }

    private val TAG="StrUril"
}
