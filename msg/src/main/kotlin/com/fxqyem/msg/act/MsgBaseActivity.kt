package com.fxqyem.msg.act

import android.app.Activity
import com.fxqyem.msg.ben.BackLsnHolder
import com.fxqyem.msg.ben.OnBackListener

/**
 * Created by Dong on 2017/12/6.
 */
abstract class MsgBaseActivity : Activity() {
    abstract fun push2BackLsnStack(obkLsnHd: BackLsnHolder)
    abstract fun popBackLsnStack(reqCod: Int, bkLsn: OnBackListener):BackLsnHolder?
}