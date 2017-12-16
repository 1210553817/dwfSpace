package com.fxqyem.msg.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.act.MsgActivity
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*

class MsgPagerLay {

    fun createMemVw(ctx: Context): View {
        val memVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL

            listView {
                id=R.id.msg_lay_cen_mempg_lsvw
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.ls_divi_bkg)
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = COLOR_TRANS
                //selector =
            }.lparams{
                width= matchParent
                height= matchParent

            }


        }
        return memVw

    }

    fun createMemItmVw(ctx:Context): View {

        val itmVw = ctx.relativeLayout {
            backgroundColor = 0xffffffff.toInt()
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            backgroundResource = R.drawable.ls_itm_bkg
            padding = dip(5)

            imageView {
                id = R.id.msg_lay_cen_mempg_itm_hiconbtn
                backgroundColor = COLOR_LIGHTGREY2

            }.lparams{
                width=dip(50)
                height=dip(50)
                alignParentLeft()
                alignParentStart()
            }
            button{
                id=R.id.msg_lay_cen_mempg_itm_chatnumbtn
                backgroundResource = R.drawable.ls_count_num
                setPadding(0,0,0,0)
                visibility = View.GONE
                textSize = 14f

            }.lparams{
                width= dip(30)
                height=dip(30)
                alignParentRight()
                alignParentEnd()
                centerVertically()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                setPadding(dip(5),0,0,0)

                textView{
                    id=R.id.msg_lay_cen_mempg_itm_mtit
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0xff444444.toInt()
                    textSize =  16f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

                textView{
                    id=R.id.msg_lay_cen_mempg_itm_stit
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0xffaaaaaa.toInt()
                    textSize =  14f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

            }.lparams{
                width= matchParent
                height= dip(50)
                rightOf(R.id.msg_lay_cen_mempg_itm_hiconbtn)
                leftOf(R.id.msg_lay_cen_mempg_itm_chatnumbtn)
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        return itmVw
    }


}