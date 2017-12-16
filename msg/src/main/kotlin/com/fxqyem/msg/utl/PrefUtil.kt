package com.fxqyem.msg.utl

import android.content.Context

import com.fxqyem.msg.ben.AppConstants

/**
 * Created by Administrator on 2016/12/29.
 */

object PrefUtil {
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法

     * @param context
     * *
     * @param key
     * *
     * @param object
     */
    fun put(context: Context, key: String, `object`: Any, fname: String) {

        val sp = context.getSharedPreferences(fname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()

        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }

        editor.commit()
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值

     * @param context
     * *
     * @param key
     * *
     * @param defaultObject
     * *
     * @return
     */
    operator fun get(context: Context, key: String, defaultObject: Any, fname: String): Any? {
        val sp = context.getSharedPreferences(fname,
                Context.MODE_PRIVATE)

        if (defaultObject is String) {
            return sp.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)
        }

        return null
    }

    /**
     * 移除某个key值已经对应的值

     * @param context
     * *
     * @param key
     */
    fun remove(context: Context, key: String, fname: String) {
        val sp = context.getSharedPreferences(fname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        editor.commit()
    }

    /**
     * 清除所有数据

     * @param context
     */
    fun clear(context: Context, fname: String) {
        val sp = context.getSharedPreferences(fname,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.commit()
    }

    /**
     * 查询某个key是否已经存在

     * @param context
     * *
     * @param key
     * *
     * @return
     */
    fun contains(context: Context, key: String, fname: String): Boolean {
        val sp = context.getSharedPreferences(fname,Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对

     * @param context
     * *
     * @return
     */
    fun getAll(context: Context, fname: String): Map<String, *> {
        val sp = context.getSharedPreferences(fname,
                Context.MODE_PRIVATE)
        return sp.all
    }


}
