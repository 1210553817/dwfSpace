package com.fxqyem.bean

import java.io.Serializable

class SongInfo : Serializable {
    var id: Int = 0
    var name: String? = null
    var artist: String? = null
    var album: String? = null
    var disName: String? = null
    var lrcNm: String? = null
    var lrcUrl: String? = null
    var picNm: String? = null
    var picUrl: String? = null
    var data: String? = null
    var dataUrl: String? = null
    var duration: Int = 0
    var size: Int = 0
    var indx: Int = 0
    var oid = -1

    var songId:String? = null
    var type: String?=null
    var qmid: String?=null

    //unpersistent param
    var dataType: Int?=0

    constructor() {}

    constructor(indx: Int, name: String) {
        this.indx = indx
        this.name = name
    }
}
