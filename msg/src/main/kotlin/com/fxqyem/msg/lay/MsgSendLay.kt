package com.fxqyem.msg.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.utl.AndUtil
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.COLOR_LIGHTGREEN
import com.fxqyem.msg.vw.COLOR_LIGHTGREY
import com.fxqyem.msg.vw.COLOR_TRANS
import com.fxqyem.msg.vw.appColorArrayLightGreen
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/15.
 */
class MsgSendLay(){
    fun createView(ctx:Context): View {

        val coreVw = ctx.relativeLayout{
            setBackgroundResource(R.drawable.opt_menu_ctn)



            relativeLayout {
                id = R.id.msg_send_lay_btm
                backgroundResource = R.drawable.opt_menu_tit
                padding = dip(2)

                imageButton {
                    id = R.id.msg_send_lay_sendBtn
                    backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_44, appColorArrayLightGreen)
                    scaleType = ImageView.ScaleType.FIT_XY

                }.lparams{
                    width= dip(46)
                    height= dip(46)
                    alignParentRight()
                    alignParentEnd()
                    centerVertically()
                }
                imageButton {
                    id = R.id.msg_send_lay_attaBtn
                    backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_46, appColorArrayLightGreen)
                    scaleType = ImageView.ScaleType.FIT_XY

                }.lparams{
                    width= dip(46)
                    height= dip(46)
                    alignParentLeft()
                    alignParentStart()
                    centerVertically()
                }

                editText {
                    id=R.id.msg_send_lay_editTxt
                    gravity =Gravity.LEFT
                    textColor = 0xff555555.toInt()
                    backgroundResource = R.drawable.opt_menu_edittxt
                    minimumHeight = dip(38)

                }.lparams{
                    width= matchParent
                    height= wrapContent
                    leftOf(R.id.msg_send_lay_sendBtn)
                    rightOf(R.id.msg_send_lay_attaBtn)
                    centerVertically()
                }

            }.lparams{
                width= matchParent
                height= wrapContent
                alignParentBottom()

            }

            linearLayout {
                orientation = LinearLayout.VERTICAL

                relativeLayout {
                    id = R.id.msg_send_lay_top
                    backgroundResource = R.drawable.opt_menu_tit
                    gravity =Gravity.CENTER
                    padding = dip(5)

                    imageButton {
                        id = R.id.msg_send_lay_backBtn
                        backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.tit_tool_back, appColorArrayLightGreen)

                    }.lparams{
                        width= dip(30)
                        height= dip(30)
                        alignParentLeft()
                        alignParentStart()
                        leftMargin=dip(5)
                        centerVertically()
                    }

                    imageButton {
                        id = R.id.msg_send_lay_optBtn
                        backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.tit_tool_opt, appColorArrayLightGreen)

                    }.lparams{
                        width= dip(30)
                        height= dip(30)
                        alignParentRight()
                        alignParentEnd()
                        rightMargin=dip(5)
                        centerVertically()
                    }

                    textView {
                        id=R.id.msg_send_lay_tit
                        gravity =Gravity.CENTER
                        maxLines=1
                        textColor = COLOR_LIGHTGREEN
                        textSize = 16f

                    }.lparams{
                        width= matchParent
                        height= matchParent
                        rightOf(R.id.msg_send_lay_backBtn)
                        leftOf(R.id.msg_send_lay_optBtn)
                        centerVertically()

                    }

                }.lparams{
                    width= matchParent
                    height= dip(50)

                }

                listView {
                    id = R.id.msg_send_lay_msgls
                    isFocusable = false
                    isFocusableInTouchMode = false
                    cacheColorHint = COLOR_TRANS
                    dividerHeight = 0
                    visibility = View.VISIBLE
                }.lparams {
                    width = matchParent
                    height = matchParent

                }
            }.lparams {
                width = matchParent
                height = matchParent
                above(R.id.msg_send_lay_btm)
            }


        }

        return coreVw
    }

}