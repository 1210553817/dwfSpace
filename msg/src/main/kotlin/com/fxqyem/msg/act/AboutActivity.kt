package com.fxqyem.msg.act

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.setContentView
import com.fxqyem.msg.lay.AboutLay

/**
 * Created by Dwf on 2017/9/13.
 */
class AboutActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AboutLay().setContentView(this)
    }


}