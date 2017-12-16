package com.fxqyem.lay

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fxqyem.R
import com.fxqyem.act.MainActivity
import com.fxqyem.act.SubActivity
import com.fxqyem.vw.COLOR_TRANS
import com.fxqyem.vw.getResColor
import com.fxqyem.vw.getResDrawable
import com.fxqyem.vw.getResString
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/7.
 */
class SubLay : AnkoComponent<SubActivity> {
    override fun createView(ui: AnkoContext<SubActivity>): View {
        //var act = ui.owner

        val coreVw = ui.linearLayout{
            orientation = LinearLayout.VERTICAL
            textView{
                id=R.id.subact_main_test_textvw
                backgroundColor = 0x55000000.toInt()
                textColor = 0xffdddddd.toInt()
                textResource = R.string.sld_about_text
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            }.lparams{
                width = matchParent
                height = matchParent
            }
        }

        return coreVw

    }


}