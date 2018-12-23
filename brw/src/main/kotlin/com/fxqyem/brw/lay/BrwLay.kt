package com.fxqyem.brw.lay

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.brw.act.BrwActivity
import com.fxqyem.brw.R
import org.jetbrains.anko.*


object BrwLay {

    fun createView(act: BrwActivity): View {
       return act.verticalLayout {
            id = R.id.brw_lay_core_main_text
            backgroundColor = 0xffececec.toInt()

           textView{
               text = "好啊！测试一下"
               setTextColor(0xff333333.toInt())
               backgroundColor = Color.GRAY
           }.lparams{
                width = matchParent
                height = wrapContent
           }


        }
    }


}