package com.fxqyem.msg.ent

import com.fxqyem.msg.ben.AppConstants
import java.io.Serializable

class AttaEnt : Serializable{
    var useful = 0
    var id: String? = null
    var tit: String? = null
    var size: String? = null
    var time: String? = null
    var type: String? = null


    constructor(id: String,tit: String?, size: String) {
        this.id = id
        this.tit = tit
        this.size = size
        this.time = java.lang.Long.toHexString(java.util.Date().time)
        this.type = "1"
    }

    constructor(astr: String?) {
        astr?:return
        val inx = astr.indexOf(AppConstants.MSG_GLOBAL_ZERO)
        if(inx<0)return
        val inxs = inx.plus(1)
        val attaStr = astr.substring(inxs)
        this.useful = 0
        val itms = attaStr.split(":")
        if(itms.isEmpty())return
        for(i in 0 until itms.size){
            if(i==0)id = itms[i]
            if(i==1)tit = itms[i]
            if(i==2)size = itms[i]
            if(i==3)time = itms[i]
            if(i==4)type = itms[i]

        }
        //Log.d("AttaEnt","----------->$this")

    }

    fun genAttaStr() = "$id:$tit:$size:$time:$type"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttaEnt

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = id?.hashCode() ?: 0

    override fun toString() = "AttaEnt(id=$id, tit=$tit, size=$size, time=$time, type=$type)"


}
