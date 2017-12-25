package com.fxqyem.vw

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import java.util.*
import java.util.regex.Pattern

val COLOR_TRANS=0x00000000.toInt()
val COLOR_WHITE=0xffffffff.toInt()
val appColorArray = floatArrayOf(
        0.1f,0f,0f,0.1f,0f,
        0f,0.35f,0f,0.35f,0f,
        0f,0f,0.9f,0.9f,0f,
        0f,0f,0f,1f,0f)
val appColorArrayA = floatArrayOf(
        0.3f,0f,0f,1f,0f,
        0f,0.35f,0f,1f,0f,
        0f,0f,0.6f,1f,0f,
        0f,0f,0f,1f,0f)

/**
 * verticalHolderView
 */
fun ViewManager.verticalHolderView(theme: Int =0)=verticalHolderView(theme){}
inline fun ViewManager.verticalHolderView(
        theme: Int =0,
        init: VerticalHolderView.() -> Unit)=ankoView(::VerticalHolderView,theme,init)

inline fun Activity.verticalHolderView(theme: Int =0)=verticalHolderView(theme){}
inline fun Activity.verticalHolderView(
        theme: Int =0,
        init: VerticalHolderView.() -> Unit)=ankoView(::VerticalHolderView,theme,init)
inline fun Context.verticalHolderView(theme: Int =0)=verticalHolderView(theme){}
inline fun Context.verticalHolderView(
        theme: Int =0,
        init: VerticalHolderView.() -> Unit)=ankoView(::VerticalHolderView,theme,init)

/**
 * others
 */
fun getResString(ctx: Context, rid: Int): String{
    return ctx.resources.getString(rid)
}
fun getResColor(ctx: Context,rid: Int): Int{
    return ctx.resources.getColor(rid)
}
fun getResDrawable(ctx: Context,rid: Int): Drawable {
    return ctx.resources.getDrawable(rid)
}

/**
 ***************global utils***************
 */

fun utilNotNull(str: String?): Boolean{
    str?: return false
    if(""==str.trim())
        return false
    return true
}
fun utilIsEmpty(str: String?): Boolean {
    return !utilNotNull(str)
}

fun utilIsInt(text: String?)= Pattern.matches("^\\d+$", text)

fun utilRegexString(regex: String, text: String): List<String>?{
    val regexa = Regex(regex)
    val result = regexa.find(text)
    if (result != null) {
        return result.groupValues
    }
    return null
}

fun utilSqlEsc(str: String?): String? {
    if (utilIsEmpty(str)) return null
    return str?.replace("'".toRegex(), "''")
}

fun utilSqlNull(str: String?): String {
    val s = utilSqlEsc(str) ?: return "NULL"
    return "'$s'"
}

fun utilRandInt(min: Int, max: Int): Int {
    val rand = Random()
    val randomNum = rand.nextInt((max - min) + 1) + min
    return randomNum
}
fun utilRandLong(): Long {
    val rand = Random()
    val randomNum = rand.nextLong()
    return randomNum
}
fun utilGetRandomColorArray(): FloatArray{
    val r = utilRandInt(0, 90).toFloat()/100
    val g = utilRandInt(0, 90).toFloat()/100
    val b = utilRandInt(0, 90).toFloat()/100
    return floatArrayOf(
            r,0f,0f,0.1f,0f,
            0f,g,0f,0.1f,0f,
            0f,0f,b,0.1f,0f,
            0f,0f,0f,0.7f,0f)
}

fun utilGetRandomColor(): Int{
    val r = utilRandInt(0, 180)
    val g = utilRandInt(0, 180)
    val b = utilRandInt(0, 180)
    return Color.argb(60, r, g, b)
}



