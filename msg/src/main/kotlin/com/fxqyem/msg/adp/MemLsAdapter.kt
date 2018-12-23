package com.fxqyem.msg.adp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgPagerLay
import com.fxqyem.msg.vw.utilNotNull

class MemLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){

    override fun getCount() = list?.size?:0

    override fun getItem(indx: Int) = list?.get(indx)?.tit

    override fun getItemId(indx: Int) = indx.toLong()

    override fun getView(position: Int, convert: View?, arg2: ViewGroup): View {
        var convertView = convert
        val vwHolder: VwHolder
        val tit: TextView?
        val sub: TextView?
        //val btn: ImageView?
        val btna: Button?
        if (convertView == null) {
            convertView = MsgPagerLay.createMemItmVw(context)
            tit = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_mtit) as TextView?
            sub = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_stit) as TextView?
            //btn = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_hiconbtn) as ImageView?
            btna = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_chatnumbtn) as Button?
            vwHolder = VwHolder()
            vwHolder.tit = tit
            vwHolder.sub = sub
            //vwHolder.btn = btn
            vwHolder.btna = btna
            convertView.tag = vwHolder
        } else {
            vwHolder = convertView.tag as VwHolder
            tit = vwHolder.tit
            sub = vwHolder.sub
            //btn = vwHolder.btn
            btna = vwHolder.btna
        }
        val itm =list?.get(position)
        val titc=itm?.tit?:""
        val ipc = if(utilNotNull(itm?.ip))"(${itm?.ip})" else ""
        tit?.text = itm?.add?:""
        sub?.text = String.format("%s%s",titc,ipc)
        val cnum = AppContext.instance?.getChat(itm?.ip)?:0
        if(cnum>0){
            btna?.text = cnum.toString()
            btna?.visibility = View.VISIBLE
        }else{
            btna?.visibility = View.GONE
        }
        //btn?.setOnClickListener(BtnListener(position))

        return convertView
    }

//    internal inner class BtnListener(private val pos: Int) : View.OnClickListener {
//        override fun onClick(v: View) {
//            val vid = v.id
//            when (vid) {
//
//            }
//
//
//        }
//    }

    internal inner class VwHolder {
        var tit: TextView? = null
        var sub: TextView? = null
        //var btn: ImageView? = null
        var btna: Button? = null
    }

    companion object {
        //private val TAG = "MemLsAdapter"
    }

}
