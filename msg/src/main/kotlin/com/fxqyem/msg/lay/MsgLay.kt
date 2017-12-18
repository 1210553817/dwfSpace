package com.fxqyem.msg.lay

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.fxqyem.msg.R
import com.fxqyem.msg.act.MsgActivity
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class MsgLay : AnkoComponent<MsgActivity> {

    override fun createView(ui: AnkoContext<MsgActivity>): View {
        val act = ui.owner

        val coreVw = ui.relativeLayout {
            id = R.id.msg_lay_core_mainhd_ly
            backgroundResource = R.drawable.opt_menu_tit


            act.btmLy = relativeLayout {
                id=R.id.msg_lay_core_btm_ly
                backgroundResource = R.drawable.opt_menu_tit

                tableLayout {
                    isStretchAllColumns = true

                    tableRow {

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            setPadding(dip(10),0,dip(10),0)

                            act.btmMemImg = imageView {
                                id= R.id.msg_lay_core_btm_mem_img
                                backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(act,R.mipmap.red_thm_29, appColorArrayLightGreen)
                                scaleType = ImageView.ScaleType.FIT_XY

                            }.lparams{
                                width= dip(34)
                                height= dip(34)
                            }

                            act.btmMemTxt = textView {
                                id= R.id.msg_lay_core_btm_mem_txt
                                gravity = Gravity.CENTER
                                maxLines=1
                                textColor= COLOR_LIGHTGREEN
                                textSize = 12f
                                textResource =R.string.memls

                            }.lparams{
                                width= dip(34)
                                height= matchParent
                            }

                        }.lparams{
                            width= wrapContent
                            height= matchParent
                            gravity = Gravity.CENTER

                        }

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            setPadding(dip(8),0,dip(8),0)

                            act.btmSetImg= imageView {
                                id= R.id.msg_lay_core_btm_set_img
                                backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(act,R.mipmap.red_thm_15, appColorArrayLightGrey)
                                scaleType = ImageView.ScaleType.FIT_XY

                            }.lparams{
                                width= dip(34)
                                height= dip(34)
                            }

                            act.btmSetTxt = textView {
                                id= R.id.msg_lay_core_btm_set_txt
                                gravity = Gravity.CENTER
                                maxLines=2
                                textColor= COLOR_LIGHTGREY
                                textSize = 12f
                                textResource =R.string.setting

                            }.lparams{
                                width= dip(34)
                                height= matchParent
                            }

                        }.lparams{
                            width= wrapContent
                            height= matchParent
                            gravity = Gravity.CENTER

                        }
                    }.lparams {
                        width = matchParent
                        height = matchParent

                    }

                }.lparams {
                    width = matchParent
                    height = matchParent
                }

            }.lparams {
                width = matchParent
                height = dip(50)
                alignParentBottom()

            }

            act.cenLy = frameLayout {
                id=R.id.msg_lay_core_cen_ly
                backgroundColor = COLOR_TRANS

                act.cenhdLy = relativeLayout {
                    id=R.id.msg_lay_core_cenhd_ly

                    act.cenViewPager = viewPager {
                        id=R.id.msg_lay_core_cen_viewPager_ly

                    }.lparams {
                        width = matchParent
                        height = matchParent
                    }


                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

            }.lparams {
                width = matchParent
                height = matchParent
                above(R.id.msg_lay_core_btm_ly)
            }

            act.cenfrmLy = frameLayout {
                id=R.id.msg_lay_core_cenfrm_ly
                visibility= View.GONE
            }.lparams {
                width = matchParent
                height = matchParent
            }

            act.oneCtnFrmLy = frameLayout {
                id=R.id.msg_lay_core_onectnfrm_ly
                visibility = View.GONE
            }.lparams {
                width = matchParent
                height = matchParent
            }

        }
        act.msgHdLy = coreVw
        return coreVw

    }


}