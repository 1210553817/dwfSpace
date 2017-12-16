package com.fxqyem.msg.ben


class BackLsnHolder {
    var requestCode: Int = 0
    var requestValue: Int = 0
    var onBackListener: OnBackListener? = null

    constructor() {}

    constructor(requestCode: Int, onBackListener: OnBackListener) {
        this.requestCode = requestCode
        this.onBackListener = onBackListener
    }
    constructor(requestCode: Int,requestValue: Int, onBackListener: OnBackListener) {
        this.requestCode = requestCode
        this.requestValue = requestValue
        this.onBackListener = onBackListener
    }
}
