package com.fxqyem.msg.ben

import android.app.Application
import com.fxqyem.msg.utl.PrefUtil
import com.fxqyem.msg.utl.SDCardUtils
import com.fxqyem.msg.vw.utilIsEmpty
import java.io.File


class AppContext : Application() {
    private var chatMp: HashMap<String,Int>? = null
    var uname: String = ""
    var utit: String = ""
    var usub: String = ""
    //var hdicon = "17"
    var fileRcvPath=SDCardUtils.sdCardPath+File.separator

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


    private fun initSelfInfo(){
        uname = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_UNM,D_UNM, AppConstants.PREF_NAME_PARAMS) as String
        utit = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_TIT,D_TIT, AppConstants.PREF_NAME_PARAMS) as String
        usub = PrefUtil.get(this,AppConstants.PREF_KEY_SELF_SUB,D_SUB, AppConstants.PREF_NAME_PARAMS) as String
    }
    fun setSelfInfo(unm: String?,tit: String?,sub: String?){
        this.uname = if(utilIsEmpty(unm)) D_UNM else unm?:D_UNM
        this.utit = if(utilIsEmpty(tit)) D_TIT else tit?:D_TIT
        this.usub = if(utilIsEmpty(unm)) D_SUB else sub?:D_SUB
        PrefUtil.put(this, AppConstants.PREF_KEY_SELF_UNM,this.uname, AppConstants.PREF_NAME_PARAMS)
        PrefUtil.put(this, AppConstants.PREF_KEY_SELF_TIT,this.utit, AppConstants.PREF_NAME_PARAMS)
        PrefUtil.put(this, AppConstants.PREF_KEY_SELF_SUB,this.usub, AppConstants.PREF_NAME_PARAMS)
    }

    companion object{
        val TAG = "AppContext"
        val D_UNM = "flix"
        val D_TIT = "AndroidPhone"
        val D_SUB = "m5"
        var instance: AppContext?=null
    }
}