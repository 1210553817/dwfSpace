package com.fxqyem.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.fxqyem.R
import com.fxqyem.utils.BitMapUtil
import com.fxqyem.vw.COLOR_TRANS
import com.fxqyem.vw.appColorArray
import com.fxqyem.vw.getResDrawable
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/29.
 */
class FrgmMenuLay {

    fun createMenu(ctx: Context): View {

        val menuVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            scrollView{
                id=R.id.frgm_hm_mn_scrvw
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                linearLayout {
                    id=R.id.frgm_hm_mn_hdly
                    orientation = LinearLayout.VERTICAL


                    linearLayout {
                        id=R.id.frgm_hm_mn_add2fav
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_47, appColorArray))
                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.frgm_hm_mn_add2fav
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
                        id=R.id.frgm_hm_mn_next2play
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_52, appColorArray))

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.frgm_hm_mn_next2play
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
                        id=R.id.frgm_hm_mn_rm
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
                            textResource=R.string.frgm_hm_mn_rm
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
                        id=R.id.frgm_hm_mn_add2p
                        backgroundResource=R.drawable.hm_song_opt_menu_mem
                        orientation = LinearLayout.HORIZONTAL
                        verticalGravity=Gravity.CENTER_VERTICAL

                        imageView {
                            backgroundColor= COLOR_TRANS
                            setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_50, appColorArray))

                        }.lparams{
                            width= dip(30)
                            height= dip(30)
                        }

                        textView{
                            textResource=R.string.frgm_hm_mn_add2p
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
                        id=R.id.frgm_hm_mn_info
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
                            textResource=R.string.frgm_hm_mn_info
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

        return menuVw
    }

    fun chsFav(ctx: Context): View {

        val reVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            listView {
                id=R.id.frgm_hm_mn_chsfav_ls
                backgroundColor = COLOR_TRANS
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.tmp_song_ls_divi_bkg)
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = COLOR_TRANS

            }.lparams{
                width= matchParent
                height=dip(200)


            }
            linearLayout {
                id=R.id.frgm_hm_mn_chsfav_new
                backgroundResource=R.drawable.hm_song_opt_menu_tit
                orientation = LinearLayout.HORIZONTAL

                imageView {
                    backgroundColor= COLOR_TRANS
                    setImageDrawable(BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_21, appColorArray))
                    padding = dip(3)

                }.lparams{
                    width= wrapContent
                    height= matchParent
                }

                textView{
                    textResource=R.string.frgm_hm_mn_chsfav_new
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


        }

        return reVw
    }

    fun chsFavItm(ctx: Context): View {

        val reVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn
            setPadding(dip(5),0,0,0)

            textView{
                id=R.id.frgm_hm_mn_chsfav_item_tit
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                textColor = 0xff666666.toInt()
                textSize = 16f

            }.lparams{
                width= matchParent
                height= dip(28)
            }

            textView{
                id=R.id.frgm_hm_mn_chsfav_item_sub
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                textColor = 0xffaaaaaa.toInt()
                textSize = 14f

            }.lparams{
                width= matchParent
                height= dip(16)
            }


        }

        return reVw
    }

    fun createFrgmInfo(ctx: Context): View {

        return ctx.linearLayout {
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.drawable.hm_song_opt_menu_ctn

            scrollView {
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = true

                linearLayout {
                    orientation = LinearLayout.VERTICAL

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource = R.drawable.hm_song_opt_menu_mem
                        verticalGravity = Gravity.CENTER_VERTICAL

                        textView {
                            textResource = R.string.frgm_hm_mn_songnm
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
                            id = R.id.hm_song_opt_menu_info_titShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
                            textResource = R.string.frgm_hm_mn_artnm
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
                            id = R.id.hm_song_opt_menu_info_artistShtxxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
                            textResource = R.string.frgm_hm_mn_albumnm
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
                            id = R.id.hm_song_opt_menu_info_albShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
                            textResource = R.string.frgm_hm_mn_duration
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
                            id = R.id.hm_song_opt_menu_info_durShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
                            textResource = R.string.frgm_hm_mn_filesz
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
                            id = R.id.hm_song_opt_menu_info_szShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
                            textResource = R.string.frgm_hm_mn_filepath
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
                            id = R.id.hm_song_opt_menu_info_dataShtxt
                            gravity = Gravity.START or Gravity.TOP
                            textColor = 0xff555555.toInt()
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
}