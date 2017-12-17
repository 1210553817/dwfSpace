package com.fxqyem.msg.vw

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import java.util.*
import java.util.regex.Pattern

val COLOR_TRANS=0x00000000.toInt()
val COLOR_GREEN=0xff00ff00.toInt()
val COLOR_LIGHTGREEN=0xff77aa77.toInt()
val COLOR_LIGHTGREEN1=0xff32CD32.toInt()
val COLOR_ORANGE=0xFFFFA500.toInt()
val COLOR_RED=0xFFFF0000.toInt()
val COLOR_LIGHTGREY=0xffaaaaaa.toInt()
val COLOR_LIGHTGREY1=0xffcdcdcd.toInt()
val COLOR_LIGHTGREY2=0xffececec.toInt()


val appColorArrayBlue = floatArrayOf(
        0.1f,0f,0f,0.1f,0f,
        0f,0.35f,0f,0.35f,0f,
        0f,0f,0.9f,0.9f,0f,
        0f,0f,0f,1f,0f)

val appColorArrayLightGreen = floatArrayOf(
        0.4f,0f,0f,0.1f,0f,
        0f,0.6f,0f,0.1f,0f,
        0f,0f,0.4f,0.1f,0f,
        0f,0f,0f,1f,0f)

val appColorArrayLightGrey = floatArrayOf(
        0.7f,0f,0f,0.1f,0f,
        0f,0.7f,0f,0.1f,0f,
        0f,0f,0.7f,0.1f,0f,
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
 * VALUES
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

fun utilHex2Int(str: String?): Int? {
    var rei: Int?
    try{
        rei = str?.toInt(16)
    }catch (e: Exception){
        rei = null
    }
    return rei
}
fun utilStr2Int(str: String?): Int? {
    var rei: Int?
    try{
        rei = str?.toInt(10)
    }catch (e: Exception){
        rei = null
    }
    return rei
}

fun utilHex2Long(str: String?): Long? {
    var rei: Long?
    try{
        rei = str?.toLong(16)
    }catch (e: Exception){
        rei = null
    }
    return rei
}
fun utilStr2Long(str: String?): Long? {
    var rei: Long?
    try{
        rei = str?.toLong(10)
    }catch (e: Exception){
        rei = null
    }
    return rei
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
fun utilGetRandomColorArray(): FloatArray{
    val r = utilRandInt(0, 40).toFloat()/100
    val g = utilRandInt(0, 40).toFloat()/100
    val b = utilRandInt(0, 40).toFloat()/100
    return floatArrayOf(
            r,0f,0f,1f,0f,
            0f,g,0f,1f,0f,
            0f,0f,b,1f,0f,
            0.2f,0.2f,0.2f,0.2f,0f)
}

fun utilGetRandomColor(): Int{
    val r = utilRandInt(0, 180)
    val g = utilRandInt(0, 180)
    val b = utilRandInt(0, 180)
    return Color.argb(60, r, g, b)
}



