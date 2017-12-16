package com.fxqyem.lay

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fxqyem.R
import com.fxqyem.utils.BitMapUtil
import com.fxqyem.vw.COLOR_TRANS
import com.fxqyem.vw.appColorArray
import com.fxqyem.vw.getResDrawable
import org.jetbrains.anko.*

/**
 * Created by Dwf on 2017/6/15.
 */
class TmplsLay(){

    fun createView(ctx:Context): View {

        val coreVw = ctx.linearLayout{
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.hm_song_opt_menu_ctn)

            relativeLayout {
                id=R.id.tmp_lshd_titly
                backgroundResource = R.drawable.tmp_song_ls_tit_bkg
                gravity = Gravity.CENTER
                padding = dip(5)

                imageButton {
                    id=R.id.tmp_lshd_backBtn
                    backgroundColor = COLOR_TRANS
                    //backgroundResource = R.mipmap.main_frm_back

                }.lparams{
                    width= dip(38)
                    height= dip(38)
                    alignParentTop()
                    alignParentLeft()
                    alignParentStart()
                    leftMargin=dip(5)
                }

                imageButton {
                    id=R.id.tmp_lshd_optBtn
                    backgroundColor = COLOR_TRANS
                    backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_12, appColorArray)

                }.lparams{
                    width= dip(38)
                    height= dip(38)
                    alignParentTop()
                    alignParentRight()
                    alignParentEnd()
                    rightMargin=dip(5)
                }

                textView {
                    id=R.id.tmp_lshd_lsTit
                    gravity =Gravity.CENTER
                    maxLines=1
                    textColor = 0xff049ff1.toInt()
                    textSize = 16f

                }.lparams{
                    width= matchParent
                    height= matchParent
                    rightOf(R.id.tmp_lshd_backBtn)
                    leftOf(R.id.tmp_lshd_optBtn)

                }

            }.lparams{
                width= matchParent
                height= dip(40)

            }

            listView {
                id=R.id.tmp_lshd_ls
                backgroundColor = COLOR_TRANS
                dividerHeight = dip(1)
                divider= getResDrawable(ctx,R.drawable.tmp_song_ls_divi_bkg)
                isFocusable = false
                isFocusableInTouchMode = false
                cacheColorHint = COLOR_TRANS
                //selector =

            }.lparams{
                width= matchParent
                height=dip(240)

            }


        }

        return coreVw
    }

    fun createItmView(ctx:Context): View {

        val itmVw = ctx.relativeLayout {
            setBackgroundResource(R.drawable.tmp_song_ls_itm_bkg)
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

            imageButton {
                id = R.id.tmp_ls_item_rmBtn
                backgroundColor = 0x00000000.toInt()
                backgroundResource = R.drawable.main_tmp_ls_rm

            }.lparams{
                width=dip(45)
                height=dip(45)
                alignParentRight()
                alignParentEnd()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                minimumHeight = dip(45)
                setPadding(dip(8),0,0,0)

                textView{
                    id=R.id.tmp_ls_item_stit
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0xff444444.toInt()
                    textSize =  16f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= matchParent

                }

                textView{
                    id=R.id.tmp_ls_item_sart
                    gravity = Gravity.CENTER_VERTICAL
                    textColor = 0xffaaaaaa.toInt()
                    textSize =  14f
                    maxLines= 1

                }.lparams{
                    width= matchParent
                    height= matchParent

                }

            }.lparams{
                width= matchParent
                height= wrapContent
                leftOf(R.id.tmp_ls_item_rmBtn)
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        return itmVw
    }
}