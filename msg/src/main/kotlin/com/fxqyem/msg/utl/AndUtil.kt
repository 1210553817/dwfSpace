package com.fxqyem.msg.utl

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import java.security.MessageDigest

object AndUtil {
    fun dp2Px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.resources.displayMetrics).toInt()
    }

    fun px2Dp(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return px / scale
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics).toInt()
    }

    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }


    /**
     * 获得屏幕宽度

     * @param context
     * *
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得高度

     * @param context
     * *
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    fun getViewWidth(view: View?): Int {
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view?.measure(width, height)
        val w = view!!.measuredWidth
        return w
    }

    fun getViewHeight(view: View?): Int{
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view?.measure(width, height)
        val h = view!!.measuredHeight
        return h
    }

    fun getStatusBarHeight(context: Context?): Int?{
        var result: Int?= 0
        val resourceId = context?.resources?.getIdentifier(
                "status_bar_height", "dimen", "android")
        if (resourceId!! > 0) {
            result = context?.resources?.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun initStatus(hdAct: Activity?, vgp: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = hdAct?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            val statusHeight = getStatusBarHeight(hdAct)
            vgp.setPadding(0, statusHeight!!, 0, 0)
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏

     * @param activity
     * *
     * @return
     */
    fun snapShotWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap? = null
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp

    }

    /**
     * 获取当前屏幕截图，不包含状态栏

     * @param activity
     * *
     * @return
     */
    fun snapShotWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap? = null
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp

    }

    fun timeFormate(millTimes: Long): String {
        var mi = "" + millTimes / 1000 / 60
        if (mi.toString().length < 2)
            mi = "0" + mi
        var ss = "" + millTimes / 1000 % 60
        if (ss.toString().length < 2)
            ss = "0" + ss
        return mi + ":" + ss
    }

    fun MD5(str: String?): String {
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        val charArray = str?.toCharArray()
        val byteArray = ByteArray(charArray!!.size)

        for (i in charArray!!.indices) {
            byteArray[i] = charArray[i].toByte()
        }
        val md5Bytes = md5!!.digest(byteArray)

        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val `val` = md5Bytes[i].toInt() and 0xff
            if (`val` < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()
    }

}
