package com.fxqyem.brw.act

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import com.fxqyem.brw.ben.BackKeyLsn
import java.util.*

abstract class BrwBaseActivity : Activity(), BackKeyLsn {
    //back Key
    private var backLsnStack: Stack<BackKeyLsn.BackHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //back Key
        backLsnStack = Stack()
    }

    fun pushBack(requestCode: Int, onBackListener: BackKeyLsn) {
        backLsnStack?.push(BackKeyLsn.BackHolder(requestCode,onBackListener))
    }

    fun pushBack(requestCode: Int,requestValue: Int, onBackListener: BackKeyLsn) {
        backLsnStack?.push(BackKeyLsn.BackHolder(requestCode,requestValue,onBackListener))
    }

    fun popBack(event: KeyEvent?): Boolean{
        val bol = !(backLsnStack?.isEmpty()?:true)
        if (bol) {
            val holder = backLsnStack?.pop()
            event?:return bol
            holder?.backLsn?.onBackKeyUp(event, holder)
        }
        return bol
    }

    fun onBaseBackKeyUp(keyCode: Int, event: KeyEvent?): Boolean{
        return popBack(event)
    }
}