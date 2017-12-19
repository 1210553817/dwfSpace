package com.fxqyem.msg.adp

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*

class MemMsgLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){
    var load2Btm = 1
    private var mLocalBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun getCount() = list?.size?:0

    override fun getItem(indx: Int) = list?.get(indx)?.tit

    override fun getItemId(indx: Int) = indx.toLong()

    override fun getView(position: Int, convert: View?, arg2: ViewGroup): View {
        var convertView = convert
        val vwHolder: VwHolder
        val tlay: LinearLayout?
        val tit: TextView?
        val sub: TextView?
        val lbtn: ImageButton?
        val rbtn: ImageButton?
        val licon: ImageView?
        val ricon: ImageView?
        if (convertView == null) {
            convertView = createItmVw(context)
            tlay = convertView.findViewById(R.id.msg_send_lay_msgls_itm_tlay) as LinearLayout?
            tit = convertView.findViewById(R.id.msg_send_lay_msgls_itm_mtit) as TextView?
            sub = convertView.findViewById(R.id.msg_send_lay_msgls_itm_stit) as TextView?
            lbtn = convertView.findViewById(R.id.msg_send_lay_msgls_itm_liconbtn) as ImageButton?
            rbtn = convertView.findViewById(R.id.msg_send_lay_msgls_itm_riconbtn) as ImageButton?
            licon = convertView.findViewById(R.id.msg_send_lay_msgls_itm_licon) as ImageView?
            ricon = convertView.findViewById(R.id.msg_send_lay_msgls_itm_ricon) as ImageView?
            vwHolder = VwHolder()
            //vwHolder?.unm = unm
            vwHolder.tlay = tlay
            vwHolder.tit = tit
            vwHolder.sub = sub
            vwHolder.lbtn = lbtn
            vwHolder.rbtn = rbtn
            vwHolder.licon = licon
            vwHolder.ricon = ricon
            convertView.tag = vwHolder
        } else {
            vwHolder = convertView.tag as VwHolder
            tlay = vwHolder.tlay
            tit = vwHolder.tit
            //sub = vwHolder.sub
            lbtn = vwHolder.lbtn
            rbtn = vwHolder.rbtn
            licon = vwHolder.licon
            ricon = vwHolder.ricon
        }
        val itm =list?.get(position)
        tit?.text = itm?.add
        /*ajust position*/
        val prtly = tlay?.layoutParams as RelativeLayout.LayoutParams?
        val hdlay = tlay?.findViewById(R.id.msg_send_lay_msgls_itm_attalay) as FrameLayout?
        val mprt = tit?.parent as LinearLayout?
        if(itm?.type==1) {
            prtly?.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
            prtly?.removeRule(RelativeLayout.ALIGN_PARENT_START)
            prtly?.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            prtly?.addRule(RelativeLayout.ALIGN_PARENT_END)
            lbtn?.visibility = View.INVISIBLE
            rbtn?.visibility = View.VISIBLE
            licon?.visibility = View.INVISIBLE
            ricon?.visibility = View.VISIBLE
            tlay?.backgroundResource = R.drawable.slkt_me
            rbtn?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(context,R.mipmap.default_hicon, appColorArrayLightGreen)

            hdlay?.backgroundResource = R.drawable.file_prgrs_bkgr
            mprt?.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }else{
            prtly?.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            prtly?.removeRule(RelativeLayout.ALIGN_PARENT_END)
            prtly?.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            prtly?.addRule(RelativeLayout.ALIGN_PARENT_START)
            rbtn?.visibility = View.INVISIBLE
            lbtn?.visibility = View.VISIBLE
            ricon?.visibility = View.INVISIBLE
            licon?.visibility = View.VISIBLE
            tlay?.backgroundResource = R.drawable.slkt_others
            lbtn?.backgroundResource = R.mipmap.default_hicon

            hdlay?.backgroundResource = R.drawable.file_prgrs_bkg
            mprt?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        tlay?.layoutParams = prtly

        val prgrs = hdlay?.findViewById(R.id.msg_send_lay_msgls_itm_attaprgrs) as ProgressBar?
        val atttxt = hdlay?.findViewById(R.id.msg_send_lay_msgls_itm_attatxt) as TextView?
        if(itm?.mtype == 1 || itm?.mtype == 2 ){
            hdlay?.visibility = View.VISIBLE
            atttxt?.textResource = R.string.file
            atttxt?.textColor = COLOR_ORANGE
        }else if(itm?.mtype == 3 ){
            hdlay?.visibility = View.VISIBLE
            atttxt?.textResource = R.string.complete
            atttxt?.textColor = COLOR_LIGHTGREEN
        }else if(itm?.mtype == 4){
            hdlay?.visibility = View.VISIBLE
            atttxt?.textResource = R.string.timeout
            atttxt?.textColor = COLOR_RED
        }else if(itm?.mtype == 5){
            hdlay?.visibility = View.VISIBLE
            atttxt?.textResource = R.string.error
            atttxt?.textColor = COLOR_RED
        }else{
            hdlay?.visibility = View.GONE
        }
        prgrs?.visibility = View.GONE
        tlay?.setOnClickListener(BtnListener(position))

        return convertView
    }

    internal inner class BtnListener(private val pos: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            val vid = v.id
            val itm = list?.get(pos)
            itm?:return
            when (vid) {
                R.id.msg_send_lay_msgls_itm_tlay -> {
                    if(itm.mtype==1) {//文件
                        val itt = Intent(AppConstants.ACTION_SER_GET_FILE)
                        itt.putExtra("ID", itm.id)
                        itt.putExtra("IP", itm.ip)
                        itt.putExtra("PNO", itm.pno)
                        itt.putExtra("FNO", itm.path)
                        itt.putExtra("FNM", itm.fname)
                        itt.putExtra("FSZ", itm.fsize)
                        mLocalBroadcastManager.sendBroadcast(itt)
                    }
                }

            }


        }
    }

    /*更新文件进度条*/
    fun updateFileItem(id: Long?,listVw: ListView?,percent: Long?){
        listVw?:return
        percent?:return
        val fir = listVw.firstVisiblePosition
        val lst = listVw.lastVisiblePosition
        var pos = -1
        for(i in fir .. lst){
           if(id == list?.get(i)?.id){
               pos = i
           }
        }
        if(pos<0)return
        val item = list?.get(pos)
        val itm = listVw.getChildAt(pos-fir)
        val vwhd = itm.tag as VwHolder
        val tlay = vwhd.tlay
        val hdlay = tlay?.findViewById(R.id.msg_send_lay_msgls_itm_attalay) as FrameLayout?
        val prgrs = hdlay?.findViewById(R.id.msg_send_lay_msgls_itm_attaprgrs) as ProgressBar?
        val atttxt = hdlay?.findViewById(R.id.msg_send_lay_msgls_itm_attatxt) as TextView?
        prgrs?.max = 100
        prgrs?.progress = percent.toInt()
        atttxt?.text = String.format("%d%s",percent,"%")
        if(percent in 0 .. 99){
            hdlay?.visibility = View.VISIBLE
            prgrs?.visibility = View.VISIBLE
            atttxt?.textColor = COLOR_ORANGE
        }else if(percent>=100L){
            prgrs?.visibility = View.INVISIBLE
            atttxt?.textColor = COLOR_LIGHTGREEN
            atttxt?.textResource = R.string.complete
            item?.mtype = 3
        }else if(percent == -1L){
            prgrs?.visibility = View.INVISIBLE
            atttxt?.textColor = COLOR_RED
            atttxt?.textResource = R.string.timeout
            item?.mtype = 4
        }else{
            prgrs?.visibility = View.INVISIBLE
            atttxt?.textColor = COLOR_RED
            atttxt?.textResource = R.string.error
            item?.mtype = 5
        }

    }

    internal inner class VwHolder {
        var tlay: LinearLayout? = null
        var tit: TextView? = null
        var sub: TextView? = null
        var lbtn: ImageButton? = null
        var rbtn: ImageButton? = null
        var licon: ImageView? = null
        var ricon: ImageView? = null
    }

    fun createItmVw(ctx:Context): View {

        return ctx.relativeLayout {
            backgroundColor = COLOR_WHITE
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            padding = dip(8)

            imageButton {
                id = R.id.msg_send_lay_msgls_itm_liconbtn
                backgroundColor = 0xffececec.toInt()

            }.lparams{
                width=dip(40)
                height=dip(40)
                alignParentLeft()
                alignParentStart()
            }

            imageButton {
                id = R.id.msg_send_lay_msgls_itm_riconbtn
                backgroundColor = 0xffececec.toInt()

            }.lparams{
                width=dip(40)
                height=dip(40)
                alignParentRight()
                alignParentEnd()
            }

            relativeLayout {

                imageView {
                    id = R.id.msg_send_lay_msgls_itm_licon
                    backgroundColor = COLOR_LIGHTGREY1
                    backgroundResource = R.drawable.chat_triangle_l

                }.lparams{
                    width= dip(5)
                    height= dip(5)
                    alignParentLeft()
                    alignParentStart()
                    topMargin = dip(17)
                }

                imageView {
                    id = R.id.msg_send_lay_msgls_itm_ricon
                    backgroundResource = R.drawable.chat_triangle_r

                }.lparams{
                    width=dip(5)
                    height=dip(5)
                    alignParentRight()
                    alignParentEnd()
                    topMargin = dip(17)
                }

                linearLayout {
                    id = R.id.msg_send_lay_msgls_itm_tlay
                    orientation = LinearLayout.VERTICAL
                    backgroundResource = R.drawable.slkt_others

                    textView{
                        id=R.id.msg_send_lay_msgls_itm_stit
                        gravity = Gravity.END or Gravity.CENTER_VERTICAL
                        textColor = 0xffdddddd.toInt()
                        textSize =  12f
                        maxLines= 1
                        visibility = View.GONE

                    }.lparams{
                        width= matchParent
                        height= wrapContent

                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL


                        frameLayout {
                            id = R.id.msg_send_lay_msgls_itm_attalay
                            visibility = View.GONE
                            backgroundResource = R.drawable.file_prgrs_bkg

                            linearLayout{
                                orientation = LinearLayout.HORIZONTAL
                                progressBar {
                                    id = R.id.msg_send_lay_msgls_itm_attaprgrs
                                    progressDrawable = getResDrawable(ctx,R.drawable.progressbar_style)
                                    padding = 0

                                }.lparams{
                                    width=dip(38)
                                    height=dip(38)
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams {
                                width = wrapContent
                                height = matchParent
                            }

                            linearLayout{
                                orientation = LinearLayout.HORIZONTAL
                                textView{
                                    id=R.id.msg_send_lay_msgls_itm_attatxt
                                    gravity = Gravity.CENTER
                                    textColor = 0xff333333.toInt()
                                    textSize =  12f


                                }.lparams{
                                    width=dip(38)
                                    height=dip(38)
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }.lparams {
                                width = wrapContent
                                height = matchParent
                            }


                        }.lparams {
                            width = wrapContent
                            height = matchParent
                            gravity = Gravity.CENTER_VERTICAL
                           //margin = dip(3)
                        }

                        textView{
                            id=R.id.msg_send_lay_msgls_itm_mtit
                            gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                            padding = dip(10)
                            textColor = 0xff333333.toInt()
                            textSize =  16f

                        }.lparams{
                            width= wrapContent
                            height= wrapContent

                        }

//                        imageView{
//                            id = R.id.msg_send_lay_msgls_itm_status
//                            backgroundColor = 0xffececec.toInt()
//                            visibility = View.GONE
//
//                        }.lparams{
//                            width = dip(5)
//                            height = dip(5)
//                            gravity = Gravity.RIGHT
//                            rightMargin = dip(3)
//                            bottomMargin = dip(3)
//
//                        }


                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                    }


                }.lparams {
                    width = wrapContent
                    height = wrapContent
                    rightOf(R.id.msg_send_lay_msgls_itm_liconbtn)
                    leftOf(R.id.msg_send_lay_msgls_itm_riconbtn)
                    leftMargin = dip(2)
                    rightMargin = dip(2)
                }



            }.lparams{
                width= wrapContent
                height= wrapContent
                rightOf(R.id.msg_send_lay_msgls_itm_liconbtn)
                leftOf(R.id.msg_send_lay_msgls_itm_riconbtn)

            }
        }

    }

    companion object {
        //private val TAG = "MemMsgLsAdapter"
    }



}
