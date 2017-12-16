package com.fxqyem.msg.ben

import android.view.KeyEvent

interface OnBackListener {
    fun onBackKeyUp(reqValue: Int, event: KeyEvent, reqCod: Int): Boolean
}
