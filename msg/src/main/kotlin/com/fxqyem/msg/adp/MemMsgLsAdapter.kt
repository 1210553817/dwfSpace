package com.fxqyem.msg.adp

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgPagerLay
import com.fxqyem.msg.utl.StrUtil
import com.fxqyem.msg.vw.COLOR_LIGHTGREEN
import com.fxqyem.msg.vw.COLOR_LIGHTGREY1
import org.jetbrains.anko.*

class MemMsgLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){
    var load2Btm = 1
    private var viewHd: ThisViewHd? = null
    private var mLocalBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun getCount(): Int {
        return list?.size?:0
    }

    override fun getItem(indx: Int): String? {
        return list?.get(indx)?.tit
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
        var convertView = convertView
        val tlay: LinearLayout?
        val tit: TextView?
        val sub: TextView?
        val lbtn: ImageButton?
        val rbtn: ImageButton?
        val licon: ImageView?
        val ricon: ImageView?
        if (convertView == null) {
            convertView = createItmVw(context)
            tlay = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_tlay) as LinearLayout?
            tit = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_mtit) as TextView?
            sub = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_stit) as TextView?
            lbtn = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_liconbtn) as ImageButton?
            rbtn = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_riconbtn) as ImageButton?
            licon = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_licon) as ImageView?
            ricon = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_ricon) as ImageView?
            viewHd = ThisViewHd()
            //viewHd?.unm = unm
            viewHd?.tlay = tlay
            viewHd?.tit = tit
            viewHd?.sub = sub
            viewHd?.lbtn = lbtn
            viewHd?.rbtn = rbtn
            viewHd?.licon = licon
            viewHd?.ricon = ricon
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            tlay = viewHd?.tlay
            tit = viewHd?.tit
            sub = viewHd?.sub
            lbtn = viewHd?.lbtn
            rbtn = viewHd?.rbtn
            licon = viewHd?.licon
            ricon = viewHd?.ricon
        }
        val itm =list?.get(position)
        tit?.text = itm?.add
        val prtly = tlay?.layoutParams as RelativeLayout.LayoutParams
        if(itm?.type==1) {
            prtly.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
            prtly.removeRule(RelativeLayout.ALIGN_PARENT_START)
            prtly.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            prtly.addRule(RelativeLayout.ALIGN_PARENT_END)
            lbtn?.visibility = View.INVISIBLE
            rbtn?.visibility = View.VISIBLE
            licon?.visibility = View.INVISIBLE
            ricon?.visibility = View.VISIBLE
            tlay?.backgroundResource = R.drawable.slkt_me
        }else{
            prtly.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            prtly.removeRule(RelativeLayout.ALIGN_PARENT_END)
            prtly.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            prtly.addRule(RelativeLayout.ALIGN_PARENT_START)
            rbtn?.visibility = View.INVISIBLE
            lbtn?.visibility = View.VISIBLE
            ricon?.visibility = View.INVISIBLE
            licon?.visibility = View.VISIBLE
            tlay?.backgroundResource = R.drawable.slkt_others
        }
        tlay.layoutParams = prtly
        //sub?.text = itm?.tit+StrUtil.formatDatea(java.util.Date())
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
                        itt.putExtra("IP", itm.ip)
                        itt.putExtra("PNO", itm.pno)
                        itt.putExtra("FNO", itm.path)
                        itt.putExtra("FNM", itm.fname)
                        itt.putExtra("FSZ", itm.fsize)
                        mLocalBroadcastManager?.sendBroadcast(itt)
                    }
                }

            }


        }
    }

    internal inner class ThisViewHd {
        var tlay: LinearLayout? = null
        var tit: TextView? = null
        var sub: TextView? = null
        var lbtn: ImageButton? = null
        var rbtn: ImageButton? = null
        var licon: ImageView? = null
        var ricon: ImageView? = null
    }

    fun createItmVw(ctx:Context): View {

        val itmVw = ctx.relativeLayout {
            backgroundResource = R.drawable.empty_itm_bkg
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
                        gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
                        textColor = 0xffdddddd.toInt()
                        textSize =  12f
                        maxLines= 1
                        visibility = View.GONE

                    }.lparams{
                        width= matchParent
                        height= wrapContent

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
                    imageView{
                        id = R.id.msg_send_lay_msgls_itm_status
                        backgroundColor = 0xffececec.toInt()
                        visibility = View.GONE

                    }.lparams{
                        width = dip(5)
                        height = dip(5)
                        gravity = Gravity.RIGHT
                        rightMargin = dip(3)
                        bottomMargin = dip(3)

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

        return itmVw
    }

    companion object {
        private val TAG = "MemMsgLsAdapter"
    }



}
