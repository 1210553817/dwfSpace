package com.fxqyem.lay

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.R
import com.fxqyem.act.SubActivity
import com.fxqyem.vw.COLOR_WHITE
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/7.
 */
class SubLay : AnkoComponent<SubActivity> {
    override fun createView(ui: AnkoContext<SubActivity>): View {

        return ui.linearLayout{
            backgroundColor = COLOR_WHITE
            orientation = LinearLayout.VERTICAL
            padding = dip(10)

            imageView {
                imageResource = R.mipmap.ic_launcher

            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER_HORIZONTAL
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
                id=R.id.subact_main_test_textvw
                textColor = 0xff555555.toInt()
                textSize = 14f
                textResource = R.string.sld_about_text
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

            }.lparams{
                width = matchParent
                height = matchParent
            }
        }

    }


}