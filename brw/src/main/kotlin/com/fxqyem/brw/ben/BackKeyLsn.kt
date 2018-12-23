package com.fxqyem.brw.ben

import android.view.KeyEvent

interface BackKeyLsn {
    fun onBackKeyUp(event: KeyEvent?, holder: BackHolder): Boolean

    class BackHolder {
        var code: Int = 0
        var value: Int = 0
        var backLsn: BackKeyLsn? = null

        constructor(code: Int, backLsn: BackKeyLsn) {
            this.code = code
            this.backLsn = backLsn
        }
        constructor(code: Int,value: Int, backLsn: BackKeyLsn) {
            this.code = code
            this.value = value
            this.backLsn = backLsn
        }
    }
}
