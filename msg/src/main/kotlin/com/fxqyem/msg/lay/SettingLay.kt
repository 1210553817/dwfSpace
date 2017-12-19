package com.fxqyem.msg.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fxqyem.msg.R
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*

class SettingLay {

    fun createSetVw(ctx: Context): View {
        return ctx.linearLayout{
            orientation = LinearLayout.VERTICAL

            relativeLayout {
                id = R.id.setting_pglay__toply
                backgroundResource = R.drawable.pg_menu_tit
                padding = dip(5)

                imageButton {
                    id = R.id.setting_pglay_hdicon_btn
                    backgroundResource = R.mipmap.default_hicon

                }.lparams{
                    width= dip(30)
                    height= dip(30)
                    alignParentRight()
                    alignParentEnd()
                    rightMargin=dip(5)
                    centerVertically()
                }


            }.lparams{
                width= matchParent
                height= dip(50)

            }

            listView {
                id=R.id.setting_pglay_lsvw
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

    }

    fun createSetItmVw(ctx:Context): View {

        return ctx.relativeLayout {
            backgroundColor = 0xffffffff.toInt()
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            backgroundResource = R.drawable.ls_itm_bkg
            padding = dip(15)

            imageView {
                id = R.id.setting_pglay_item_himg
                backgroundColor = COLOR_LIGHTGREY2

            }.lparams{
                width=dip(20)
                height=dip(20)
                alignParentLeft()
                alignParentStart()
                centerVertically()
            }

            textView{
                id=R.id.setting_pglay_item_mtit
                textColor = 0xff444444.toInt()
                textSize =  16f
                gravity = Gravity.CENTER_VERTICAL

            }.lparams{
                width= matchParent
                height= dip(20)
                rightOf(R.id.setting_pglay_item_himg)
                centerVertically()
                leftMargin = dip(5)

            }

        }

    }

    fun createSelfVw(ctx: Context): View{
        return ctx.linearLayout {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.opt_menu_ctn)

            textView {
                id= R.id.file_slector_holder_tit
                backgroundResource = R.drawable.opt_menu_tit
                maxLines = 1
                textColor = COLOR_LIGHTGREEN
                textSize = 14f
                text = getResString(context,R.string.setting_pglay_slfdo)
                gravity = Gravity.CENTER

            }.lparams {
                width= matchParent
                height=dip(38)

            }

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                setPadding(dip(10),dip(5),dip(10),dip(5))

                editText {
                    id=R.id.setting_pglay_slfdo_unm
                    gravity =Gravity.START
                    textColor = 0xff555555.toInt()
                    backgroundResource = R.drawable.opt_menu_edittxt
                    hintResource = R.string.setting_pglay_slfdo_unm

                }.lparams{
                    width= matchParent
                    height= dip(36)
                    gravity= Gravity.CENTER_VERTICAL
                }

            }.lparams{
                width= matchParent
                height= wrapContent

            }

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                setPadding(dip(10),dip(5),dip(10),dip(5))

                editText {
                    id=R.id.setting_pglay_slfdo_tit
                    gravity =Gravity.START
                    textColor = 0xff555555.toInt()
                    backgroundResource = R.drawable.opt_menu_edittxt
                    hintResource = R.string.setting_pglay_slfdo_tit

                }.lparams{
                    width= matchParent
                    height= dip(36)
                    gravity= Gravity.CENTER_VERTICAL
                }

            }.lparams{
                width= matchParent
                height=wrapContent

            }

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                setPadding(dip(10),dip(5),dip(10),dip(5))

                editText {
                    id=R.id.setting_pglay_slfdo_sub
                    gravity =Gravity.START
                    textColor = 0xff555555.toInt()
                    backgroundResource = R.drawable.opt_menu_edittxt
                    hintResource = R.string.setting_pglay_slfdo_sub

                }.lparams{
                    width= matchParent
                    height= dip(36)
                    gravity= Gravity.CENTER_VERTICAL
                }

            }.lparams{
                width= matchParent
                height=wrapContent

            }

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                setGravity(Gravity.CENTER)
                padding=dip(10)

                button{
                    id=R.id.setting_pglay_slfdo_clbtn
                    backgroundResource = R.drawable.opt_menu_mbtn
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    textColor = COLOR_LIGHTGREEN
                    textSize = 16f
                    text = getResString(context,R.string.cancle)
                    padding = 0

                }.lparams{
                    width = dip(120)
                    height=dip(30)


                }

                button{
                    id=R.id.setting_pglay_slfdo_okbtn
                    backgroundResource = R.drawable.opt_menu_mbtn
                    textColor = COLOR_LIGHTGREEN
                    textSize = 16f
                    text = getResString(context,R.string.file_slector_holder_confirm)
                    padding = 0

                }.lparams{
                    width = dip(120)
                    height=dip(30)
                    leftMargin=dip(10)


                }

            }.lparams{
                width= matchParent
                height= wrapContent

            }

        }

    }


}