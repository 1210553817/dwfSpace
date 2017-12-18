package com.fxqyem.msg.ben

import android.app.Application
import android.content.Context
import android.util.Log
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.utl.PrefUtil


class AppContext : Application() {
    var chatMp: HashMap<String,Int>? = null
    var uname: String? = ""
    var utit: String? = ""
    var usub: String? = ""
    var hdicon = "17"
    var fileRcvPath="/sdcard/"

    override fun onCreate() {
        super.onCreate()
        AppContext.instance = this
        initChatMp()
        initSelfInfo()
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


    fun initSelfInfo(){
        uname = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_UNM,"MSG", AppConstants.PREF_NAME_PARAMS) as String
        utit = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_TIT,"androidPhone", AppConstants.PREF_NAME_PARAMS) as String
        usub = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_SUB,"m5", AppConstants.PREF_NAME_PARAMS) as String
    }
    fun setSelfInfo(unm: String?,tit: String?,sub: String?){
        if(unm!=null){
            this.uname = unm
            PrefUtil.put(this, AppConstants.PREF_KEY_SELF_UNM,unm, AppConstants.PREF_NAME_PARAMS)
        }
        if(tit!=null){
            this.utit = tit
            PrefUtil.put(this, AppConstants.PREF_KEY_SELF_TIT,tit, AppConstants.PREF_NAME_PARAMS)
        }
        if(sub!=null){
            this.usub = sub
            PrefUtil.put(this, AppConstants.PREF_KEY_SELF_SUB,sub, AppConstants.PREF_NAME_PARAMS)
        }

    }

    companion object{
        val TAG = "AppContext"
        var instance: AppContext?=null
    }
}