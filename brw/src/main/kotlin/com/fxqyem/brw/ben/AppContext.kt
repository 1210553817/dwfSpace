package com.fxqyem.brw.ben

import android.app.Application

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.instance = this

    }

    companion object{
        val TAG = "AppContext"
        var instance: AppContext?=null
    }
}