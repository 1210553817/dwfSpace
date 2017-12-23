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
import com.fxqyem.msg.utl.DbUtil
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import com.fxqyem.msg.utl.BitMapUtil


class MemMsgLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){
    var load2Btm = 1
    private var mLocalBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)

    override fun getCount() = list?.size?:0

    override fun getItem(indx: Int) = list?.get(indx)?.tit

    override fun getItemId(indx: Int) = indx.toLong()

    override fun getView(position: Int, convert: View?, arg2: ViewGroup): View {
        var convertView = convert
        val vwHolder: VwHolder
        val hd: LinearLayout?
        val tlay: LinearLayout?
        val tit: TextView?
        val lbtn: ImageButton?
        val licon: ImageView?
        val attalay: RelativeLayout?
        val attaprgrs: ProgressBar?
        val attatxt: TextView?
        if (convertView == null) {
            convertView = createItmVw(context)
            hd = convertView.findViewById(R.id.msg_send_lay_msgls_itm_hd) as LinearLayout?
            tlay = convertView.findViewById(R.id.msg_send_lay_msgls_itm_tlay) as LinearLayout?
            tit = convertView.findViewById(R.id.msg_send_lay_msgls_itm_mtit) as TextView?
            lbtn = convertView.findViewById(R.id.msg_send_lay_msgls_itm_liconbtn) as ImageButton?
            licon = convertView.findViewById(R.id.msg_send_lay_msgls_itm_licon) as ImageView?
            attalay = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_attalay) as RelativeLayout?
            attaprgrs = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_attaprgrs) as ProgressBar?
            attatxt = convertView?.findViewById(R.id.msg_send_lay_msgls_itm_attatxt) as TextView?
            vwHolder = VwHolder()
            vwHolder.hd = hd
            vwHolder.tlay = tlay
            vwHolder.tit = tit
            vwHolder.lbtn = lbtn
            vwHolder.licon = licon
            vwHolder.attalay = attalay
            vwHolder.attaprgrs = attaprgrs
            vwHolder.attatxt = attatxt
            convertView.tag = vwHolder
        } else {
            vwHolder = convertView.tag as VwHolder
            hd = vwHolder.hd
            tlay = vwHolder.tlay
            tit = vwHolder.tit
            lbtn = vwHolder.lbtn
            licon = vwHolder.licon
            attalay = vwHolder.attalay
            attaprgrs = vwHolder.attaprgrs
            attatxt = vwHolder.attatxt
        }
        val itm =list?.get(position)
        tit?.text = itm?.add
        /*in out*/
        if(itm?.type==1) {
            hd?.layoutDirection = View.LAYOUT_DIRECTION_RTL
            lbtn?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(context,R.mipmap.default_hicon, appColorArrayLightGreen)
            licon?.imageResource = R.drawable.file_prgrs_bkgr
            tlay?.backgroundResource = R.drawable.slkt_me
            tlay?.layoutDirection = View.LAYOUT_DIRECTION_RTL
            attalay?.backgroundResource = R.drawable.file_prgrs_bkgr
        }else{
            hd?.layoutDirection = View.LAYOUT_DIRECTION_LTR
            lbtn?.backgroundResource = R.mipmap.default_hicon
            licon?.imageResource = R.drawable.file_prgrs_bkg
            tlay?.backgroundResource = R.drawable.slkt_others
            tlay?.layoutDirection = View.LAYOUT_DIRECTION_LTR
            attalay?.backgroundResource = R.drawable.file_prgrs_bkg
        }
        /*atta*/
        if(itm?.mtype == 1 || itm?.mtype == 2 ){
            attalay?.visibility = View.VISIBLE
            attatxt?.textResource = R.string.file
            attatxt?.textColor = COLOR_ORANGE
        }else if(itm?.mtype == 3 ){
            attalay?.visibility = View.VISIBLE
            attatxt?.textResource = R.string.complete
            attatxt?.textColor = COLOR_LIGHTGREEN
        }else if(itm?.mtype == 4){
            attalay?.visibility = View.VISIBLE
            attatxt?.textResource = R.string.timeout
            attatxt?.textColor = COLOR_ORANGE
        }else if(itm?.mtype == 5){
            attalay?.visibility = View.VISIBLE
            attatxt?.textResource = R.string.error
            attatxt?.textColor = COLOR_RED
        }else{
            attalay?.visibility = View.GONE
        }
        attaprgrs?.visibility = View.GONE
        tlay?.setOnClickListener(BtnListener(position))
        tlay?.setOnLongClickListener(BtnLongLsn(position))

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
                        //update status
                        itm.mtype=4
                        val db = DbUtil(context)
                        db.updMsgMtype(itm.id,itm.mtype)

                    }else{
                        val myClipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                        val myClip = ClipData.newPlainText("text", itm.add)
                        myClipboard.primaryClip = myClip
                    }
                }

            }


        }
    }

    internal  inner class BtnLongLsn(private val pos: Int): View.OnLongClickListener{
        override fun onLongClick(v: View?): Boolean {
            val itmid = list?.get(pos)?.id
            list?.removeAt(pos)
            if(itmid!=null) {
                val db = DbUtil(context)
                db.rmMsgItm(arrayOf(itmid))
            }
            notifyDataSetChanged()
            return true
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
        val attalay = vwhd.attalay
        val prgrs = vwhd.attaprgrs
        val atttxt = vwhd.attatxt
        attalay?.visibility = View.VISIBLE
        if(percent in 0 .. 99){
            prgrs?.visibility = View.VISIBLE
            atttxt?.textColor = COLOR_ORANGE
            prgrs?.max = 100
            prgrs?.progress = percent.toInt()
            atttxt?.text = String.format("%d%s",percent,"%")
        }else if(percent>=100L){
            prgrs?.visibility = View.INVISIBLE
            atttxt?.textColor = COLOR_LIGHTGREEN
            atttxt?.textResource = R.string.complete
            item?.mtype = 3
        }else if(percent == -1L){
            prgrs?.visibility = View.INVISIBLE
            atttxt?.textColor = COLOR_ORANGE
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
        var hd: LinearLayout? = null
        var tlay: LinearLayout? = null
        var tit: TextView? = null
        var lbtn: ImageButton? = null
        var licon: ImageView? = null
        var attalay: RelativeLayout? = null
        var attaprgrs: ProgressBar? = null
        var attatxt: TextView? = null
    }

    private fun createItmVw(ctx:Context): View {

        return ctx.linearLayout {
            id = R.id.msg_send_lay_msgls_itm_hd
            backgroundColor = COLOR_WHITE
            descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            setPadding(dip(2),dip(6),dip(2),dip(6))

            imageButton {
                id = R.id.msg_send_lay_msgls_itm_liconbtn
            }.lparams{
                width=dip(36)
                height=dip(36)
            }

            imageView {
                id = R.id.msg_send_lay_msgls_itm_licon
            }.lparams{
                width= dip(2)
                height= dip(4)
                topMargin = dip(17)
            }

            linearLayout {
                id = R.id.msg_send_lay_msgls_itm_tlay
                orientation = LinearLayout.HORIZONTAL

                relativeLayout{
                    id = R.id.msg_send_lay_msgls_itm_attalay
                    visibility = View.GONE
                    backgroundResource = R.drawable.file_prgrs_bkg

                    progressBar {
                        id = R.id.msg_send_lay_msgls_itm_attaprgrs
                        indeterminateDrawable = getResDrawable(context,R.drawable.circle_prgrs)
                        padding = 0

                    }.lparams{
                        width=dip(36)
                        height=dip(36)
                        centerVertically()
                    }

                    textView{
                        id=R.id.msg_send_lay_msgls_itm_attatxt
                        gravity = Gravity.CENTER
                        textColor = 0xff333333.toInt()
                        textSize =  12f


                    }.lparams{
                        width=dip(36)
                        height=dip(36)
                        centerVertically()
                    }

                }.lparams {
                    width = wrapContent
                    height = matchParent
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

            }.lparams {
                width = wrapContent
                height = wrapContent
                marginEnd = dip(38)
            }
        }

    }

    companion object {
        //private val TAG = "MemMsgLsAdapter"
    }



}
