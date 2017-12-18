package com.fxqyem.msg.lay

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.act.AboutActivity
import org.jetbrains.anko.*
import android.support.v4.app.ActivityCompat.startActivity
import android.content.Intent
import android.net.Uri


/**
 * Created by Dwf on 2017/6/7.
 */
class AboutLay : AnkoComponent<AboutActivity> {
    override fun createView(ui: AnkoContext<AboutActivity>): View {

        val coreVw = ui.linearLayout{
            orientation = LinearLayout.VERTICAL
            padding = dip(10)
            imageView {
                imageResource = R.mipmap.ic_launcher

            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER_HORIZONTAL
            }.onClick {
                ui.owner.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dwfHub/dwfSpace/")))
            }
            textView{
                textColor = 0xff444444.toInt()
                textSize = 16f
                textResource = R.string.version_info
                gravity = Gravity.CENTER

            }.lparams{
                width = matchParent
                height = wrapContent
            }
            textView{
                textColor = 0xff555555.toInt()
                textSize = 14f
                textResource = R.string.about_info
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

            }.lparams{
                width = matchParent
                height = matchParent
            }
        }

        return coreVw

    }


}