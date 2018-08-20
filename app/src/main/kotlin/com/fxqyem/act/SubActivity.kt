package com.fxqyem.act

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.fxqyem.R
import com.fxqyem.lay.SubLay
import org.jetbrains.anko.setContentView

/**
 * Created by Dwf on 2017/9/13.
 */
class SubActivity: Activity(){
    private var textVw: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SubLay().setContentView(this)
        textVw = findViewById(R.id.subact_main_test_textvw) as TextView

        //textVw?.text = stringFromJNI()
    }

//    init{
//        System.loadLibrary("myNtv")
//    }
//    external fun stringFromJNI(): String


}