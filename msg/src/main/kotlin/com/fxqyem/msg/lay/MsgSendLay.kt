package com.fxqyem.msg.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*

class MsgSendLay{

    fun createView(ctx:Context): View {

        return  ctx.relativeLayout{
            setBackgroundResource(R.drawable.opt_menu_ctn)

            relativeLayout {
                id = R.id.msg_send_lay_btm
                backgroundResource = R.drawable.opt_menu_tit
                padding = dip(2)

                button {
                    id = R.id.msg_send_lay_sendBtn
                    backgroundResource = R.drawable.opt_menu_edittxt
                    textResource = R.string.send
                    textSize = 14f
                    textColor = COLOR_LIGHTGREEN

                }.lparams{
                    width= dip(50)
                    height= dip(38)
                    alignParentRight()
                    alignParentEnd()
                    centerVertically()
                }
                button {
                    id = R.id.msg_send_lay_attaBtn
                    backgroundResource = R.drawable.opt_menu_edittxt
                    textResource = R.string.file
                    textSize = 14f
                    textColor = COLOR_LIGHTGREEN

                }.lparams{
                    width= dip(50)
                    height= dip(38)
                    alignParentLeft()
                    alignParentStart()
                    centerVertically()
                }

                editText {
                    id=R.id.msg_send_lay_editTxt
                    gravity =Gravity.START
                    textColor = 0xff555555.toInt()
                    backgroundResource = R.drawable.opt_menu_edittxt
                    minimumHeight = dip(38)

                }.lparams{
                    width= matchParent
                    height= wrapContent
                    leftOf(R.id.msg_send_lay_sendBtn)
                    rightOf(R.id.msg_send_lay_attaBtn)
                    centerVertically()
                    setMargins(dip(2),0,dip(2),0)
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
                    backgroundResource = R.drawable.pg_menu_tit
                    gravity =Gravity.CENTER
                    padding = dip(5)

                    imageButton {
                        id = R.id.msg_send_lay_backBtn
                        backgroundResource = R.mipmap.tit_tool_back

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
                        backgroundResource = R.mipmap.tit_tool_opt

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
                        textColor = COLOR_WHITE
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

    }

}