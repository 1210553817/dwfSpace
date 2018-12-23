package com.fxqyem.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fxqyem.R
import com.fxqyem.vw.COLOR_TRANS
import com.fxqyem.vw.getResColor
import com.fxqyem.vw.getResDrawable
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/16.
 */
object FrgmHmLay {
    fun createView(ctx: Context): View {

        return ctx.linearLayout {
            orientation = LinearLayout.VERTICAL

            relativeLayout {
                id= R.id.frgm_hm_lshd_titly
                backgroundColor = getResColor(ctx,R.color.mainTitleD)
                gravity = Gravity.CENTER_VERTICAL

                imageButton {
                    id=R.id.frgm_hm_lshd_backBtn
                    backgroundColor = COLOR_TRANS
                    backgroundResource = R.mipmap.main_frm_back

                }.lparams{
                    width = wrapContent
                    height= wrapContent
                    alignParentLeft()
                    alignParentStart()
                    setMargins(dip(12),dip(8),0,dip(8))
                }

                imageButton {
                    id=R.id.frgm_hm_lshd_optBtn
                    backgroundColor = COLOR_TRANS
                    backgroundResource = R.mipmap.hm_song_ls_set

                }.lparams{
                    width = wrapContent
                    height= wrapContent
                    alignParentRight()
                    alignParentEnd()
                    setMargins(0,dip(8),dip(12),dip(8))
                }

                textView {
                    id=R.id.frgm_hm_lshd_lsTit
                    gravity = Gravity.CENTER
                    textColor = 0xffcccccc.toInt()
                    textSize = 16f
                    maxLines = 1

                }.lparams{
                    width = matchParent
                    height= wrapContent
                    leftOf(R.id.frgm_hm_lshd_optBtn)
                    rightOf(R.id.frgm_hm_lshd_backBtn)
                    setMargins(0,dip(16),0,dip(8))
                }

            }.lparams{
                width = matchParent
                height= wrapContent

            }

            listView {
                id=R.id.frgm_hm_lshd_lsvw
                backgroundColor = COLOR_TRANS
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.home_ex_expdls_divi_bkg)
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = COLOR_TRANS
                selector = getResDrawable(ctx,R.drawable.hm_song_ls_itm_bkg)

            }.lparams{
                width = matchParent
                height= matchParent
            }
        }

    }

    fun createItmView(ctx: Context): View {
        return ctx.relativeLayout{
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

            imageButton{
                id=R.id.frgm_hm_lsitm_moreBtn
                backgroundColor = COLOR_TRANS
                backgroundResource = R.drawable.main_ctt_ls_more

            }.lparams{
                width= wrapContent
                height=dip(50)
                alignParentRight()
            }

            button{
                id=R.id.frgm_hm_lsitm_ckBtn
                backgroundResource = R.drawable.hm_song_ls_cknor
                visibility = View.GONE
                textColor = 0xff009933.toInt()
                textSize = 12f
                setPadding(0,0,0,0)

            }.lparams{
                width= dip(50)
                height=dip(50)
                alignParentLeft()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                minimumHeight = dip(50)
                setPadding(dip(14),0,0,0)
                setGravity(Gravity.CENTER_VERTICAL)

                textView{
                    id=R.id.frgm_hm_lsitm_titTxt
                    textColor = getResColor(ctx,R.color.mainTxt)
                    textSize =16f
                    maxLines = 1


                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
                textView{
                    id=R.id.frgm_hm_lsitm_subtitTxt
                    textColor = getResColor(ctx,R.color.subTxt)
                    textSize =14f
                    maxLines = 1

                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

            }.lparams{
                width = matchParent
                height = dip(50)
                leftOf(R.id.frgm_hm_lsitm_moreBtn)
                rightOf(R.id.frgm_hm_lsitm_ckBtn)
            }
        }

    }

    fun createLocOptSelectMenu(ctx: Context): View {

        return ctx.linearLayout{
            id=R.id.frgm_hm_loc_optmn_selector_tophd
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
                        id=R.id.frgm_hm_loc_optmn_selector_all
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="显示所有"
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
                        id=R.id.frgm_hm_loc_optmn_selector_folder
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="显示文件夹"
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
                        id=R.id.frgm_hm_songls_optmn_selector_multck
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="多选模式"
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
                        id=R.id.frgm_hm_songls_optmn_selector_del
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="删除选中"
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
                        id=R.id.frgm_hm_songls_optmn_selector_add2f
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="添加到歌单..."
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
                        id=R.id.frgm_hm_songls_optmn_selector_add2p
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="添加到播放列表"
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
                        id=R.id.frgm_hm_songls_optmn_selector_sortsv
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL


                        textView{
                            text="按当前顺序保存..."
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