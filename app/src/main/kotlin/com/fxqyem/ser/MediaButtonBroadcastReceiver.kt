package com.fxqyem.ser

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.fxqyem.bean.AppContext


class MediaButtonBroadcastReceiver : BroadcastReceiver() {
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    override fun onReceive(context: Context, intent: Intent) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(AppContext.instance)
        mLocalBroadcastManager?.sendBroadcast(intent)
    }
}
