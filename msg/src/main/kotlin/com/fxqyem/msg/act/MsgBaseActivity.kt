package com.fxqyem.msg.act

import android.app.Activity
import com.fxqyem.msg.ben.BackLsnHolder
import com.fxqyem.msg.ben.OnBackListener

abstract class MsgBaseActivity : Activity() {
    abstract fun push2BackLsnStack(obkLsnHd: BackLsnHolder)
    abstract fun popBackLsnStack(reqCod: Int, bkLsn: OnBackListener):BackLsnHolder?
}