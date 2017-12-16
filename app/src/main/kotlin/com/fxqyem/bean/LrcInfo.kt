package com.fxqyem.bean

class LrcInfo {
    var tims: IntArray? = null
    var mes: Array<String?>? = null

    constructor() {}

    constructor(tims: IntArray, mes: Array<String?>) {
        this.tims = tims
        this.mes = mes
    }
}
