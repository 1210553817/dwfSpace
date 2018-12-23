package com.fxqyem.msg.lay

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.act.AboutActivity
import com.fxqyem.msg.vw.COLOR_WHITE
import org.jetbrains.anko.*


object AboutLay {
    fun createView(ui: AboutActivity): View {

        return ui.linearLayout{
            backgroundColor = COLOR_WHITE
            orientation = LinearLayout.VERTICAL
            padding = dip(20)
            imageView {
                imageResource = R.mipmap.ic_launcher

            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER_HORIZONTAL
                margin = dip(6)
            }
//            .onClick {
//                ui.owner.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dwfHub/dwfSpace/")))
//            }
            textView{
                textColor = 0xff444444.toInt()
                textSize = 16f
                textResource = R.string.version_info
                gravity = Gravity.CENTER

            }.lparams{
                width = matchParent
                height = wrapContent
                margin = dip(6)
            }
            textView{
                textColor = 0xff555555.toInt()
                textSize = 14f
                textResource = R.string.about_info
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

            }.lparams{
                width = matchParent
                height = matchParent
                margin = dip(6)
            }
        }

    }


}