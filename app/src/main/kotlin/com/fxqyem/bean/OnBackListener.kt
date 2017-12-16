package com.fxqyem.bean

import android.view.KeyEvent

interface OnBackListener {
    fun onBackKeyUp(reqValue: Int, event: KeyEvent, reqCod: Int): Boolean
}
