package com.fxqyem.bean

class SongGrpInfo {
    var indx: Int = 0
    var id: Int = 0
    var name: String? = null
    var subname: String? = null
    var tbName: String? = null
    var picName: String? = null
    var picUrl: String? = null

    constructor() {}

    constructor(indx: Int, name: String) {
        this.indx = indx
        this.name = name
    }
}
