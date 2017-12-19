package com.fxqyem.lay

import android.app.Service
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import android.widget.SeekBar
import com.fxqyem.R
import com.fxqyem.act.MainActivity
import com.fxqyem.vw.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.widget.Toast
import com.fxqyem.bean.AppContext
import com.fxqyem.utils.BitMapUtil


/**
 * Created by Dwf on 2017/6/7.
 */
class MainLay : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>): View {
        var act = ui.owner

        val coreVw = ui.relativeLayout {
                id = R.id.main_hd_ly
                setBackgroundResource(R.mipmap.bkg1)

                act.btmLy = relativeLayout {
                    id=R.id.main_btmhd_ly
                    backgroundColor = getResColor(act,R.color.mainTitle)

                    act.btmProgBar = horizontalProgressBar{
                        id=R.id.main_btm_prgrs
                        progressDrawable = getResDrawable(act,R.drawable.main_btm_prgrs)

                    }.lparams {
                        width= matchParent
                        height=dip(1)
                    }

                    relativeLayout {
                        id=R.id.main_btm_ly
                        padding=0

                        var btmPic=imageView{
                            id=R.id.btmPic
                            backgroundColor= 0xff4499ee.toInt()
                            backgroundResource=R.mipmap.default_music_icon
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams{
                            width = dip(50)
                            height = matchParent
                            alignParentLeft()
                            alignParentStart()
                        }
                        act.btmImgVw=btmPic

                        act.btmLsBtn = imageButton{
                            id=R.id.btmLsBtn
                            backgroundColor= COLOR_TRANS
                            backgroundResource=R.drawable.main_btm_ls
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams{
                            width= dip(50)
                            height=dip(50)
                            alignParentRight()
                            alignParentEnd()
                        }

                        act.btmNextBtn = imageButton{
                            id=R.id.btmNextBtn
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.drawable.main_btm_next
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams{
                            width= dip(50)
                            height=dip(50)
                            leftOf(act.btmLsBtn!!)

                        }

                        act.btmPlayBtn = imageButton{
                            id=R.id.btmPlayBtn
                            backgroundColor= COLOR_TRANS
                            backgroundResource = R.drawable.main_btm_play
                            scaleType = ImageView.ScaleType.FIT_XY
                        }.lparams{
                            width= dip(50)
                            height=dip(50)
                            leftOf(act.btmNextBtn!!)

                        }
                        //main_btm_txt_ly
                        relativeLayout {
                            id=R.id.main_btm_txt_ly
                            padding=dip(5)

                            act.btmSongTitle = textView {
                                id=R.id.main_btm_txtTit
                                maxLines=1
                                textColor=getResColor(act,R.color.mainTxt)
                                textSize = 14f

                            }.lparams {
                                width= matchParent
                                height= wrapContent

                            }

                            act.btmSongArtist = textView {
                                id=R.id.main_btm_txtTitSub
                                maxLines=1
                                textColor=getResColor(act,R.color.subTxt)
                                textSize = 10f

                            }.lparams {
                                width= matchParent
                                height= wrapContent
                                below(act.btmSongTitle!!)


                            }


                        }.lparams {
                            width= matchParent
                            height= wrapContent
                            leftOf(act.btmPlayBtn!!)
                            rightOf(btmPic)

                        }



                    }.lparams {
                        width= matchParent
                        height=dip(50)
                        below(act.btmProgBar!!)
                    }


                }.lparams {
                    width = matchParent
                    height = wrapContent
                    alignParentBottom()

                }

                act.cenLy = frameLayout {
                    id=R.id.main_cen_ly
                    backgroundColor = COLOR_TRANS

                    act.cenOriLy = relativeLayout {
                        id=R.id.main_cen_ori_ly
                        backgroundColor = COLOR_TRANS
                       act.mTabLayout = tabLayout(theme=R.style.AppTheme){
                           id=R.id.main_tabs
                            backgroundColor=getResColor(act,R.color.mainTitle)
                           setSelectedTabIndicatorColor(getResColor(act,R.color.mainTxt))
                           setTabTextColors(getResColor(act,R.color.subTxt),getResColor(act,R.color.mainTxt))

                       }.lparams {
                           width = matchParent
                           height = wrapContent
                           alignParentTop()
                       }
                        act.mViewPager = viewPager {
                            id=R.id.main_viewPager

                        }.lparams {
                            width = matchParent
                            height = matchParent
                            below(act.mTabLayout!!)
                        }


                    }.lparams {
                        width = matchParent
                        height = wrapContent
                    }
                    act.cenHdLy = frameLayout {
                        id=R.id.main_cen_hd_ly
                        visibility= View.GONE
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                    }

                }.lparams {
                    width = matchParent
                    height = matchParent
                    above(act.btmLy!!)
                }

                act.frmLy = frameLayout {
                    id=R.id.main_frm_ly
                    visibility = View.GONE

                    verticalHolderView {
                        id=R.id.main_frm_scrvw
                        isFillViewport=true
                        isVerticalScrollBarEnabled=false
                        isHorizontalScrollBarEnabled=false

                        act.frmCtnLy = relativeLayout {
                            id=R.id.main_frm_ctn_ly
                            backgroundResource = R.mipmap.bkg1

                            act.frmCtnCvLy = relativeLayout {
                                id=R.id.main_frm_ctncv_ly
                                backgroundColor = getResColor(act,R.color.mainContent)
                                act.frmBackBtn = imageButton{
                                    id=R.id.main_frm_backBtn
                                    backgroundColor = COLOR_TRANS
                                    backgroundResource = R.mipmap.main_frm_back


                                }.lparams{
                                    width = wrapContent
                                    height = wrapContent
                                    alignParentLeft()
                                    alignParentStart()
                                    alignParentTop()
                                    setMargins(dip(15),dip(8),dip(5),dip(8))

                                }

                                linearLayout {
                                    id=R.id.main_frm_tit_ly
                                    orientation = VERTICAL

                                    act.frmSongTitle = textView {
                                        id=R.id.main_frm_songTitle
                                        maxLines = 1
                                        textColor = 0xffcccccc.toInt()
                                        textSize = 16f

                                    }.lparams{
                                        width = matchParent
                                        height = wrapContent

                                    }

                                    act.frmSongArtist = textView {
                                        id=R.id.main_frm_songArtist
                                        maxLines = 1
                                        textColor = 0xccaaaaaa.toInt()
                                        textSize = 12f

                                    }.lparams{
                                        width = matchParent
                                        height = wrapContent
                                    }

                                }.lparams {
                                    width = matchParent
                                    height = wrapContent
                                    alignParentRight()
                                    alignParentEnd()
                                    alignParentTop()
                                    setMargins(dip(8),dip(8),dip(0),dip(0))
                                    rightOf(act.frmBackBtn!!)

                                }
                                //main_frm_plchd
                                act.frmPlchd = textView {
                                    id=R.id.main_frm_plchd
                                    backgroundColor = COLOR_TRANS

                                }.lparams{
                                    width = matchParent
                                    height = 1
                                    leftMargin=dip(28)
                                    rightMargin=dip(28)
                                }

                                act.frmLrcVw = frameLayout {
                                    id=R.id.main_frm_midd_hdly
                                    setPadding(dip(35),dip(0),dip(35),dip(5))

                                }.lparams {
                                    width = matchParent
                                    height = 1
                                    below(act.frmBackBtn!!)


                                }

                                act.frmCurPosTime = textView {
                                    id=R.id.main_frm_curPosTime
                                    textColor = 0xccaaaaaa.toInt()
                                    textSize = 10f
                                    text="00:00"
                                }.lparams{
                                    width = wrapContent
                                    height = wrapContent
                                    sameLeft(act.frmPlchd!!)
                                    below(act.frmPlchd!!)

                                }

                                act.frmAllTime = textView {
                                    id=R.id.main_frm_allTime
                                    textColor = 0xccaaaaaa.toInt()
                                    textSize = 10f
                                    text="00:00"
                                }.lparams{
                                    width = wrapContent
                                    height = wrapContent
                                    sameRight(act.frmPlchd!!)
                                    below(act.frmPlchd!!)
                                }

                                act.frmSeekBar = seekBar{
                                    id=R.id.main_frm_progressSeekBar
                                    backgroundColor = COLOR_TRANS
                                    progressDrawable = getResDrawable(act,R.drawable.main_frm_seekbar_style)
                                    thumb = getResDrawable(act,R.drawable.main_frm_seekbar_thumb)
                                    setPadding(dip(26),0,dip(26),0)
                                }.lparams{
                                    width = matchParent
                                    height = dip(20)
                                    below(act.frmAllTime!!)
                                    centerHorizontally()

                                }

                                act.frmPlayBtn = imageButton {
                                    id=R.id.main_frm_playBtn
                                    backgroundColor = COLOR_TRANS
                                    scaleType = ImageView.ScaleType.FIT_XY


                                }.lparams{
                                    width= wrapContent
                                    height= wrapContent
                                    below(act.frmSeekBar!!)
                                    centerHorizontally()

                                }
                                act.frmPrevBtn = imageButton{
                                    id=R.id.main_frm_prevBtn
                                    backgroundColor = COLOR_TRANS
                                    backgroundResource = R.drawable.main_frm_next_btn_style
                                    scaleType = ImageView.ScaleType.FIT_XY
                                    rotationY = 180f
                                }.lparams{
                                    width= wrapContent
                                    height= wrapContent
                                    sameTop(act.frmPlayBtn!!)
                                    leftOf(act.frmPlayBtn!!)
                                    topMargin=dip(6)

                                }

                                act.frmNextBtn = imageButton{
                                    id=R.id.main_frm_nextBtn
                                    backgroundColor = COLOR_TRANS
                                    backgroundResource = R.drawable.main_frm_next_btn_style
                                    scaleType = ImageView.ScaleType.FIT_XY
                                }.lparams{
                                    width= wrapContent
                                    height= wrapContent
                                    sameTop(act.frmPlayBtn!!)
                                    rightOf(act.frmPlayBtn!!)
                                    topMargin=dip(6)
                                }

                                act.frmLsBtn = imageButton{
                                    id=R.id.main_frm_tmplsBtn
                                    backgroundColor = COLOR_TRANS
                                    backgroundResource = R.drawable.main_btm_ls
                                    scaleType = ImageView.ScaleType.FIT_XY
                                }.lparams{
                                    width= wrapContent
                                    height= wrapContent
                                    sameTop(act.frmPlayBtn!!)
                                    rightOf(act.frmNextBtn!!)
                                    topMargin=dip(8)
                                }




                            }.lparams{
                                width = matchParent
                                height = matchParent
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
                    height = matchParent
                }

                act.vwCtnFrmLy = frameLayout {
                    id=R.id.main_vwctn_frmly
                    visibility = View.GONE
                    backgroundColor = COLOR_TRANS
                }.lparams {
                    width = matchParent
                    height = matchParent
                }

                act.sldBarLy = linearLayout {
                    id=R.id.main_sldBarLy
                    orientation = VERTICAL
                    backgroundColor = COLOR_TRANS
                }.lparams {
                    width = dip(15)
                    height = matchParent
                    alignParentLeft()
                    alignParentStart()
                    alignParentTop()
                    bottomMargin = dip(30)
                }

                act.covLy = linearLayout {
                    id=R.id.main_covLy
                    orientation = VERTICAL
                    visibility = View.GONE
                    backgroundColor = 0x15000000.toInt()

                }.lparams {
                    width = matchParent
                    height = matchParent
                    alignParentLeft()
                    alignParentStart()
                    alignParentTop()
                }

                act.sldLy = relativeLayout {
                    id=R.id.main_sldLy
                    visibility = View.GONE

                    var sldShpBlkDivGrv = textView{
                        id=R.id.main_sldLy_gra
                        backgroundResource = R.drawable.shp_blk_div_gra

                    }.lparams{
                        width = dip(8)
                        height = matchParent
                        alignParentRight()
                        alignParentEnd()
                        alignParentTop()
                    }

                    relativeLayout {
                        id=R.id.main_sldLy_hd
                        backgroundResource = R.drawable.shp_blk_div

                        var mainSldLyImg = imageView{
                            id=R.id.main_sldLy_img
                            backgroundResource = R.drawable.shp_blk_div
                            backgroundColor = 0xffaaaaaa.toInt()

                        }.lparams{
                            width = matchParent
                            height = dip(150)
                            alignParentTop()
                        }

                        var mainSldLyBtm = linearLayout {
                            id=R.id.main_sldLy_btm
                            orientation = HORIZONTAL

                            act.appSettingBtn = button {
                                id=R.id.main_sldLy_appSettingBtn
                                backgroundResource = R.drawable.shp_blk_div_hl
                                textColor = 0xffcdcdcd.toInt()
                                textSize = 18f
                                text = getResString(act,R.string.sld_settings)

                            }.lparams{
                                width=dip(115)
                                height=dip(50)

                            }

                            act.appOverBtn = button {
                                id=R.id.main_sldLy_appOverBtn
                                backgroundResource = R.drawable.shp_blk_div_h
                                textColor = 0xffcdcdcd.toInt()
                                textSize = 18f
                                text = getResString(act,R.string.sld_quit)

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
                            id=R.id.main_sldLy_ctt_scrvw
                            isVerticalScrollBarEnabled=false
                            isHorizontalScrollBarEnabled=false

                            linearLayout{
                                id=R.id.main_sldLy_ls
                                orientation = VERTICAL

                                act.bkgSettingBtnA = button {
                                    id=R.id.main_sldLy_bkgSettingBtnA
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_lsbkgSetting)

                                }.lparams{
                                    width= matchParent
                                    height=dip(50)

                                }

                                act.bkgSettingBtnB = button {
                                    id=R.id.main_sldLy_bkgSettingBtnB
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_ctrlbkgSetting)

                                }.lparams{
                                    width= matchParent
                                    height=dip(50)

                                }
                                act.bkgBlurBtn = button {
                                    id=R.id.main_sldLy_bkgblurBtn
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_ctrlbkgblur1)

                                }.lparams{
                                    width= matchParent
                                    height=dip(50)

                                }

                                act.sldPlayModeBtn = button {
                                    id=R.id.main_sldLy_playModeBtn
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_ctrlPlayMode0)

                                }.lparams{
                                    width= matchParent
                                    height=dip(50)

                                }

                                button {
                                    id=R.id.main_sldLy_bkgSettingBtnC
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_musicEffectSetting)

                                }.lparams{
                                    width= matchParent
                                    height=dip(50)

                                }

                                button {
                                    id=R.id.main_sldLy_bkgSettingBtnD
                                    backgroundResource = R.drawable.shp_blk_div
                                    textColor = 0xffcdcdcd.toInt()
                                    textSize = 16f
                                    text = getResString(act,R.string.sld_ls_nightModelSetting)

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
                            below(mainSldLyImg)
                            above(mainSldLyBtm)
                        }

                    }.lparams{
                        width = matchParent
                        height = matchParent
                        leftOf(sldShpBlkDivGrv)
                    }

                }.lparams {
                    width = dip(230)
                    height = matchParent
                    topMargin=dip(0)

                }

                act.topCtnFrmLy = frameLayout {
                    id=R.id.main_topctn_frmly
                    visibility = View.GONE
                }.lparams {
                    width = matchParent
                    height = matchParent
                }

            }
        act.hdLy = coreVw

        return coreVw

    }

    fun createVolumeView(ctx: Context): View {
        val audio = ctx.getSystemService(Service.AUDIO_SERVICE) as AudioManager
        val curVlm = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVlm = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return ctx.linearLayout{
            orientation = LinearLayout.HORIZONTAL
            setPadding(dip(10),dip(20),dip(10),dip(20))
            backgroundColor = 0xcc555555.toInt()

            val muteBtn = imageButton{
                id=R.id.home_ex_grp_moreBtn
                backgroundColor = COLOR_TRANS

            }.lparams{
                width= dip(30)
                height=dip(30)
            }
            if(curVlm==0){
                muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_68, appColorArrayA)
                muteBtn.tag=0
            }else {
                AppContext?.instance?.appVolume = curVlm
                muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_69, appColorArrayA)
                muteBtn.tag=1
            }

            val vlmSeekBar = seekBar{
                id=R.id.main_frm_progressSeekBar
                backgroundColor = COLOR_TRANS
                progressDrawable = getResDrawable(ctx,R.drawable.main_frm_seekbar_style)
                thumb = getResDrawable(ctx,R.drawable.main_frm_seekbar_thumb)
                setPadding(dip(10),0,dip(10),0)
            }.lparams{
                width = matchParent
                height = dip(20)
                topMargin = dip(5)
            }

            vlmSeekBar.max = maxVlm
            vlmSeekBar.progress = curVlm
            vlmSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0)
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if(seekBar?.progress==0){
                        muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_68, appColorArrayA)
                    } else{
                        muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx,R.mipmap.red_thm_69, appColorArrayA)
                        AppContext?.instance?.appVolume = seekBar?.progress?:1
                    }
                }
            })
            muteBtn.onClick {
                if(muteBtn.tag==0){
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, AppContext?.instance?.appVolume?:1, 0)
                    muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx, R.mipmap.red_thm_69, appColorArrayA)
                    vlmSeekBar.progress = AppContext?.instance?.appVolume?:1
                    muteBtn.tag=1
                }else {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                    muteBtn.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(ctx, R.mipmap.red_thm_68, appColorArrayA)
                    vlmSeekBar.progress = 0
                    muteBtn.tag=0
                }
            }



        }
    }


}

