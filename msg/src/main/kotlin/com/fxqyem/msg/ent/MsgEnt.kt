package com.fxqyem.msg.ent

import com.fxqyem.msg.utl.StrUtil
import java.io.Serializable
import java.util.Date

class MsgEnt : Serializable {
    var useful: Int = 0
    var id: Long = 0
    var tit: String? = null
    var sub: String? = null
    var ip: String? = null
    var host: String? = null
    var path: String? = null
    var fname: String? = null
    var fsize: String? = null
    var time: String? = null

    var type: Int = 0 //消息方向，0收 1 发
    var mtype: Int = 0 //消息类型0：normal；1：收文件 2：接文件
    //protocol
    var ver: String? = null
    var pno: String? = null
    var cmd: Int = 0
    var add: String? = null


    constructor() {}

    constructor(tit: String, sub: String) {
        this.tit = tit
        this.sub = sub
    }
    constructor(tit: String?, sub: String?,cmd: Int?,add: String?) {
        this.ver = "1"
        this.tit = tit?:""
        this.sub = sub?:""
        this.cmd = cmd?:0
        this.add = add?:""
        this.pno = Date().time.toString()
    }

    constructor(msgStr: String?) {
        this.useful = 0
        msgStr?:return
        val itms = msgStr.split(":")
        if(itms.size>4&&itms[4]!=null&&StrUtil.isInt(itms[4])){
            this.ver = itms[0]
            this.pno = itms[1]
            this.tit = itms[2]
            this.sub = itms[3]
            this.cmd = itms[4].toInt()
            if(itms.size>5) {
                val sb = StringBuilder()
                for(i in 5 until itms.size){
                    if(i>5)sb.append(":")
                    sb.append(itms[i])
                }
                this.add = sb.toString()
            }
            this.useful = 1
        }


    }

    fun genMsgStr(): String{
        return "$ver:$pno:$tit:$sub:$cmd:$add"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MsgEnt

        if (ip != other.ip) return false

        return true
    }

    override fun hashCode(): Int {
        return ip?.hashCode() ?: 0
    }


}
