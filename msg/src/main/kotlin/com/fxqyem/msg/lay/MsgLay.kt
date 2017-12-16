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

                            act.btmGrpImg = imageView {
                                id= R.id.msg_lay_core_btm_grp_img
                                backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(act,R.mipmap.red_thm_30, appColorArrayLightGrey)
                                scaleType = ImageView.ScaleType.FIT_XY

                            }.lparams{
                                width= dip(34)
                                height= dip(34)
                            }

                            act.btmGrpTxt = textView {
                                id= R.id.msg_lay_core_btm_grp_txt
                                gravity = Gravity.CENTER
                                maxLines=2
                                textColor= COLOR_LIGHTGREY
                                textSize = 12f
                                textResource =R.string.grpls

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

            act.sldBarLy = linearLayout {
                id=R.id.msg_lay_core_sldbar_ly
                orientation = LinearLayout.VERTICAL
            }.lparams {
                width = dip(15)
                height = matchParent
                alignParentLeft()
                alignParentStart()
                alignParentTop()
                bottomMargin = dip(48)
            }

            act.sldCovLy = linearLayout {
                id=R.id.msg_lay_core_sldcov_ly
                orientation = LinearLayout.VERTICAL
                visibility = View.GONE
                backgroundColor = 0x02555555.toInt()

            }.lparams {
                width = matchParent
                height = matchParent
                alignParentLeft()
                alignParentStart()
                alignParentTop()
            }

            act.sldLy = relativeLayout {
                id=R.id.msg_lay_core_sld_ly
                visibility = View.GONE

                textView{
                    id=R.id.msg_lay_core_sld_ly_gra
                    backgroundResource = R.drawable.shp_blk_div_gra


                }.lparams{
                    width = dip(6)
                    height = matchParent
                    alignParentRight()
                    alignParentEnd()
                    alignParentTop()
                }

                relativeLayout {
                    id=R.id.msg_lay_core_sld_ly_hd
                    backgroundResource = R.drawable.shp_blk_div

                    act.sldLyImg= imageView{
                        id=R.id.msg_lay_core_sld_ly_img
                        backgroundColor = 0xffeeeeee.toInt()
                        //backgroundResource = R.mipmap.bkg

                    }.lparams{
                        width = matchParent
                        height = dip(150)
                        alignParentTop()
                    }

                    linearLayout {
                        id=R.id.msg_lay_core_sld_ly_btm
                        orientation = LinearLayout.HORIZONTAL

                        act.appAboutBtn = button {
                            id=R.id.msg_lay_core_sld_ly_appabout_btn
                            backgroundResource = R.drawable.shp_blk_div_hl
                            textColor = 0xff555555.toInt()
                            textSize = 18f
                            textResource=R.string.about

                        }.lparams{
                            width=dip(115)
                            height=dip(50)

                        }

                        act.appOverBtn = button {
                            id=R.id.msg_lay_core_sld_ly_appover_btn
                            backgroundResource = R.drawable.shp_blk_div_h
                            textColor = 0xff555555.toInt()
                            textSize = 18f
                            textResource=R.string.exit

                        }.lparams{
                            width=dip(115)
                            height=dip(50)

                        }

                    }.lparams{
                        width = matchParent
                        height = wrapContent
                        alignParentBottom()
                    }

                    scrollView {
                        id=R.id.msg_lay_core_sld_ly_scrvw
                        isVerticalScrollBarEnabled=false
                        isHorizontalScrollBarEnabled=false

                        linearLayout{
                            id=R.id.msg_lay_core_sld_ly_ls
                            orientation = LinearLayout.VERTICAL

                            act.msgBkgSetBtn = button {
                                id=R.id.msg_lay_core_sld_ly_msgbkgset_btn
                                backgroundResource = R.drawable.shp_blk_div
                                textColor = 0xff555555.toInt()
                                textSize = 16f
                                textResource= R.string.msgbkg_set

                            }.lparams{
                                width= matchParent
                                height=dip(50)

                            }

                            act.pepPicSetBtn = button {
                                id=R.id.msg_lay_core_sld_ly_peppicset_btn
                                backgroundResource = R.drawable.shp_blk_div
                                textColor = 0xff555555.toInt()
                                textSize = 16f
                                textResource= R.string.peppic_set

                            }.lparams{
                                width= matchParent
                                height=dip(50)

                            }

                            button {
                                backgroundResource = R.drawable.shp_blk_div
                                textColor = 0xff555555.toInt()
                                textSize = 16f
                                textResource= R.string.peppic_set

                            }.lparams{
                                width= matchParent
                                height=dip(50)

                            }

                            button {
                                backgroundResource = R.drawable.shp_blk_div
                                textColor = 0xff555555.toInt()
                                textSize = 16f
                                textResource= R.string.peppic_set

                            }.lparams{
                                width= matchParent
                                height=dip(50)

                            }

                            button {
                                backgroundResource = R.drawable.shp_blk_div
                                textColor = 0xff555555.toInt()
                                textSize = 16f
                                textResource= R.string.peppic_set

                            }.lparams{
                                width= matchParent
                                height=dip(50)

                            }



                        }.lparams{
                            width = matchParent
                            height = wrapContent
                        }

                    }.lparams{
                        width = matchParent
                        height = matchParent
                        below(R.id.msg_lay_core_sld_ly_img)
                        above(R.id.msg_lay_core_sld_ly_btm)
                    }

                }.lparams{
                    width = matchParent
                    height = matchParent
                    leftOf(R.id.msg_lay_core_sld_ly_gra)
                }

            }.lparams {
                width = dip(230)
                height = matchParent
                topMargin=dip(0)

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