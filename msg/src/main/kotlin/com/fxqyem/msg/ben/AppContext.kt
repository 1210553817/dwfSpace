package com.fxqyem.msg.ben

import android.app.Application
import android.util.Log
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.utl.PrefUtil


class AppContext : Application() {
    var chatMp: HashMap<String,Int>? = null
    var uname = "CUIHW"
    var utit = "CUIHW_PC"
    var usub = "CUIHW_PC2141231234"
    var hdicon = "17"
    var fileRcvPath="/sdcard/"

    override fun onCreate() {
        super.onCreate()
        AppContext.instance = this
        initChatMp()

    }

    private fun initChatMp(){
        val omp = PrefUtil.getAll(this,AppConstants.PREF_NAME_CHAT_NUMS)
        chatMp = HashMap()
        for((key,value) in omp){
            val v = value as Int?
            chatMp?.put(key,v?:0)
        }
    }

    fun addChat(ip: String?){
        ip?:return
        var o = chatMp?.get(ip)
        o = o?:0
        o++
        chatMp?.put(ip,o)
        PrefUtil.put(this,ip,o,AppConstants.PREF_NAME_CHAT_NUMS)
    }
    fun clearChat(ip: String?){
        ip?:return
        chatMp?.remove(ip)
        PrefUtil.remove(this,ip,AppConstants.PREF_NAME_CHAT_NUMS)
    }
    fun getChat(ip: String?): Int{
        ip?:return 0
        val o = chatMp?.get(ip)
        return o?:0
    }

    companion object{
        val TAG = "AppContext"
        var instance: AppContext?=null
    }
}