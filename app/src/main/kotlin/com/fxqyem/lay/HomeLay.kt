package com.fxqyem.lay

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.fxqyem.R
import com.fxqyem.utils.BitMapUtil
import com.fxqyem.vw.COLOR_TRANS
import com.fxqyem.vw.appColorArray
import com.fxqyem.vw.getResColor
import com.fxqyem.vw.getResDrawable
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/15.
 */
class HomeLay {
    fun createView(ctx: Context): View {

        val coreVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL

            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    imageButton {
                        id= R.id.home_ex_localBtn
                        backgroundResource = R.drawable.home_ex_local_btn
                        scaleType = ImageView.ScaleType.FIT_XY

                    }.lparams{
                        width= dip(50)
                        height= dip(50)
                    }

                    textView {
                        gravity = Gravity.CENTER
                        maxLines=2
                        textColor= 0xffffffff.toInt()
                        textSize = 12f
                        textResource =R.string.home_ex_localtxt

                    }.lparams{
                        width= dip(50)
                        height= wrapContent
                    }

                }.lparams{
                    width= wrapContent
                    height= wrapContent
                    gravity = Gravity.CENTER
                    margin = dip(15)


                }

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    imageButton {
                        id= R.id.home_ex_dnldBtn
                        backgroundResource = R.drawable.home_ex_dnld_btn
                        scaleType = ImageView.ScaleType.FIT_XY


                    }.lparams{
                        width= dip(50)
                        height= dip(50)
                    }

                    textView {
                        gravity = Gravity.CENTER
                        maxLines=2
                        textColor= 0xffffffff.toInt()
                        textSize = 12f
                        textResource =R.string.home_ex_dnldtxt

                    }.lparams{
                        width= dip(50)
                        height= wrapContent
                    }

                }.lparams{
                    width= wrapContent
                    height= wrapContent
                    gravity = Gravity.CENTER
                    margin = dip(15)

                }

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    imageButton {
                        id= R.id.home_ex_lsnhisBtn
                        backgroundResource = R.drawable.home_ex_lsnhis_btn
                        scaleType = ImageView.ScaleType.FIT_XY

                    }.lparams{
                        width= dip(50)
                        height= dip(50)
                    }

                    textView {
                        gravity = Gravity.CENTER
                        maxLines=2
                        textColor= 0xffffffff.toInt()
                        textSize = 12f
                        textResource =R.string.home_ex_lsnhistxt

                    }.lparams{
                        width= dip(50)
                        height= wrapContent
                    }

                }.lparams{
                    width= wrapContent
                    height= wrapContent
                    gravity = Gravity.CENTER
                    margin = dip(15)

                }

            }.lparams{
                width= wrapContent
                height= wrapContent
                gravity = Gravity.CENTER
            }

            expandableListView {
                id=R.id.home_ex_expdls
                backgroundColor = 0x00000000
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.home_ex_expdls_divi_bkg)
                setChildDivider(getResDrawable(ctx,R.drawable.home_ex_expdls_divi_bkg))
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = 0x00000000
                selector = getResDrawable(ctx,R.drawable.hm_song_ls_itm_bkg)

            }.lparams{
                width= matchParent
                height= matchParent
            }

        }
        return coreVw
    }

    fun createGrpView(ctx: Context): View {
        return ctx.relativeLayout{
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

            imageButton{
                id=R.id.home_ex_grp_moreBtn
                backgroundColor = COLOR_TRANS
                backgroundResource = R.drawable.main_ctt_ls_more

            }.lparams{
                width= dip(30)
                height=dip(30)
                rightMargin = dip(10)
                alignParentRight()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                minimumHeight = dip(30)
                setPadding(dip(20),0,0,0)
                setGravity(Gravity.CENTER_VERTICAL)

                textView{
                    id=R.id.home_ex_grp_titTxt
                    textColor = getResColor(ctx,R.color.mainTxt)
                    textSize =16f
                    maxLines = 1

                }.lparams {
                    width = matchParent
                    height = matchParent
                }

            }.lparams{
                width = matchParent
                height = wrapContent

                leftOf(R.id.home_ex_grp_moreBtn)
            }

        }

    }

    fun createChdView(ctx: Context): View {
        return ctx.relativeLayout{
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

            imageButton{
                id=R.id.home_ex_chd_moreBtn
                backgroundColor = COLOR_TRANS
                backgroundResource = R.drawable.main_ctt_ls_more

            }.lparams{
                width= dip(50)
                height=dip(50)
                alignParentRight()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                minimumHeight = dip(50)
                setPadding(dip(14),0,0,0)
                setGravity(Gravity.CENTER_VERTICAL)

                textView{
                    id=R.id.home_ex_chd_titTxt
                    textColor = getResColor(ctx,R.color.mainTxt)
                    textSize =16f
                    maxLines = 1


                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
                textView{
                    id=R.id.home_ex_chd_subtitTxt
                    textColor = getResColor(ctx,R.color.subTxt)
                    textSize =14f
                    maxLines = 1


                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

            }.lparams{
                width = matchParent
                height = wrapContent
                leftOf(R.id.home_ex_chd_moreBtn)
            }
        }

    }

    fun createFavOptMenu(ctx: Context): View {

        return ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            scrollView{
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    linearLayout {
                        id=R.id.hm_fav_opt_menu_edtBtn
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_38, appColorArray))
                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.hm_fav_mn_modify
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)

                    }

                    linearLayout {
                        id=R.id.hm_fav_opt_menu_rmBtn
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_35, appColorArray))

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.hm_fav_mn_remove
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)

                    }
                    linearLayout {
                        id=R.id.hm_fav_opt_menu_infoBtn
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_33, appColorArray))

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.hm_fav_mn_info
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)

                    }

                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

            }.lparams{
                width= matchParent
                height= matchParent

            }

        }

    }

    fun createFavOptEdit(ctx: Context): View {

        return ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            scrollView{
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip(5),dip(5),dip(5),dip(5))

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL

                        textView{
                            textResource=R.string.hm_fav_mn_favnm
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            maxLines = 1
                            textSize = 16f

                        }.lparams{
                            width= dip(100)
                            height= dip(40)
                            rightMargin = dip(5)
                        }

                        editText {
                            id = R.id.hm_fav_opt_menu_edit_titEtxt
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.drawable.hm_song_opt_menu_edittxt
                            inputType = InputType.TYPE_CLASS_TEXT

                        }.lparams{
                            width= matchParent
                            height= dip(40)
                        }



                    }.lparams{
                        width= matchParent
                        height= dip(40)

                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL

                        textView{
                            textResource=R.string.hm_fav_mn_favdesc
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            maxLines = 1
                            textSize = 16f

                        }.lparams{
                            width= dip(100)
                            height= dip(40)
                            rightMargin = dip(5)
                        }

                        editText {
                            id = R.id.hm_fav_opt_menu_edit_subtitEtxt
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.drawable.hm_song_opt_menu_edittxt
                            inputType = InputType.TYPE_CLASS_TEXT

                        }.lparams{
                            width= matchParent
                            height= dip(40)
                        }



                    }.lparams{
                        width= matchParent
                        height= dip(40)
                        topMargin = dip(5)

                    }

                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

            }.lparams{
                width= matchParent
                height= matchParent

            }

            relativeLayout {
                setPadding(dip(5),dip(5),dip(5),dip(5))

                button {
                    id=R.id.hm_fav_opt_menu_edit_edtOkBtn
                    backgroundResource = R.drawable.hm_song_opt_menu_mem_btn
                    textColor = 0xaa049ff1.toInt()
                    textSize = 16f
                    padding = 0

                }.lparams{
                    width = dip(120)
                    height = dip(30)
                    alignParentRight()
                    alignParentEnd()

                }

            }.lparams{
                width = matchParent
                height = wrapContent

            }

        }
    }

    fun createFavOptInfo(ctx: Context): View {

        return ctx.linearLayout {
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            scrollView {
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource = R.drawable.hm_song_opt_menu_mem
                        verticalGravity = Gravity.CENTER_VERTICAL

                        textView {
                            textResource = R.string.hm_fav_mn_favnm
                            gravity = Gravity.END or Gravity.TOP
                            textColor = 0xff888888.toInt()
                            maxLines = 1
                            textSize = 16f
                            padding = dip(5)

                        }.lparams {
                            width = dip(80)
                            height = wrapContent
                        }

                        textView {
                            id = R.id.hm_fav_opt_menu_info_titShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
                            maxLines = 1
                            textSize = 16f
                            padding = dip(5)

                        }.lparams {
                            width = matchParent
                            height = wrapContent
                        }


                    }.lparams {
                        width = matchParent
                        height = wrapContent

                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource = R.drawable.hm_song_opt_menu_mem
                        verticalGravity = Gravity.CENTER_VERTICAL

                        textView {
                            textResource = R.string.hm_fav_mn_favdesc
                            gravity = Gravity.END or Gravity.TOP
                            textColor = 0xff888888.toInt()
                            maxLines = 1
                            textSize = 16f
                            padding = dip(5)

                        }.lparams {
                            width = dip(80)
                            height = wrapContent
                        }

                        textView {
                            id = R.id.hm_fav_opt_menu_info_subTitShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
                            maxLines = 1
                            textSize = 16f
                            padding = dip(5)

                        }.lparams {
                            width = matchParent
                            height = wrapContent
                        }


                    }.lparams {
                        width = matchParent
                        height = wrapContent

                    }

                }.lparams {
                    width = matchParent
                    height = matchParent

                }


            }.lparams {
                width = matchParent
                height = wrapContent
            }
        }
    }

    /**
     * net start
     */
    fun createNetView(ctx: Context): View {
        return ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundColor = COLOR_TRANS

            relativeLayout {
                backgroundColor = getResColor(ctx,R.color.mainTitleD)
                setPadding(dip(8),dip(3),dip(8),dip(3))

                imageButton {
                    id=R.id.main_net_ser_selector
                    backgroundColor = COLOR_TRANS
                    backgroundResource = R.mipmap.icon_wy
                    scaleType=ImageView.ScaleType.FIT_XY

                }.lparams{
                    width= dip(40)
                    height= dip(40)
                    alignParentTop()
                    alignParentLeft()
                    alignParentStart()
                }

                imageButton {
                    id=R.id.main_net_ser_serbtn
                    backgroundColor = COLOR_TRANS
                    backgroundResource = R.mipmap.red_thm_ser
                    scaleType=ImageView.ScaleType.FIT_XY
                }.lparams{
                    width= dip(40)
                    height= dip(40)
                    alignParentTop()
                    alignParentRight()
                    alignParentEnd()
                }

                editText {
                    id=R.id.main_net_ser_keyword
                    gravity =Gravity.START
                    maxLines=1
                    textColor = 0xffeeeeee.toInt()
                    backgroundColor = 0x22ffffff

                }.lparams{
                    width= matchParent
                    height= dip(40)
                    rightOf(R.id.main_net_ser_selector)
                    leftOf(R.id.main_net_ser_serbtn)
                    setMargins(dip(2),0,dip(2),0)

                }

            }.lparams{
                width= matchParent
                height= dip(46)
                gravity = Gravity.CENTER_VERTICAL

            }

            listView {
                id=R.id.main_net_ser_rstls
                backgroundColor = COLOR_TRANS
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.home_ex_expdls_divi_bkg)
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = COLOR_TRANS
                selector = getResDrawable(ctx,R.drawable.hm_song_ls_itm_bkg)

            }.lparams{
                width= matchParent
                height=matchParent

            }


        }

    }

    fun createNetItemView(ctx:Context): View {

        return ctx.relativeLayout {
            backgroundColor = getResColor(ctx,R.color.mainTitle)
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            setPadding(dip(2),dip(2),dip(2),dip(2))

            imageButton {
                id = R.id.main_net_ser_rstls_dld
                backgroundColor = COLOR_TRANS
                backgroundResource=R.mipmap.red_thm_36

            }.lparams{
                width=dip(40)
                height=dip(40)
                alignParentRight()
                alignParentEnd()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                minimumHeight = dip(40)
                setPadding(dip(8),0,0,0)

                textView{
                    id=R.id.main_net_ser_rstls_stit
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0xffffffff.toInt()
                    textSize =  16f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= matchParent

                }

                textView{
                    id=R.id.main_net_ser_rstls_sart
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0x66eeeeee
                    textSize =  14f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= matchParent

                }

            }.lparams{
                width= matchParent
                height= wrapContent
                leftOf(R.id.main_net_ser_rstls_dld)
                gravity = Gravity.CENTER_VERTICAL
            }
        }

    }

    fun createNetSelectMenu(ctx: Context): View {

        return ctx.linearLayout{
            id=R.id.main_net_ser_selector_tophd
            orientation = LinearLayout.VERTICAL
           //backgroundResource = R.drawable.hm_song_opt_menu_ctn
            backgroundColor = 0xffdddddd.toInt()

            scrollView{
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    backgroundColor = 0xffffffff.toInt()
                    setPadding(dip(10),0,0,0)

                    linearLayout {
                        id=R.id.main_net_ser_selector_wy
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_wy
                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="网易音乐"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)
                    }

                    linearLayout {
                        id=R.id.main_net_ser_selector_xm
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_xm

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="虾米音乐"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)
                    }

                    linearLayout {
                        id=R.id.main_net_ser_selector_kg
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_kg

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="酷狗音乐"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)
                    }

                    linearLayout {
                        id=R.id.main_net_ser_selector_qq
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_qq

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="QQ音乐"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)
                    }

                    linearLayout {
                        id=R.id.main_net_ser_selector_bd
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_bd

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="百度音乐"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)
                    }

                    linearLayout {
                        id=R.id.main_net_ser_selector_ws
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.mipmap.icon_ws

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            text="5sing伴奏"
                            gravity = Gravity.START or Gravity.CENTER_VERTICAL
                            textColor = 0xff888888.toInt()
                            textSize = 16f

                        }.lparams{
                            width= matchParent
                            height= matchParent
                            leftMargin=dip(10)
                        }

                    }.lparams{
                        width= matchParent
                        height= dip(45)

                    }


                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

            }.lparams{
                width= matchParent
                height= matchParent

            }

        }

    }
}