package com.fxqyem.msg.adp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgPagerLay
import org.jetbrains.anko.find

class MemLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){
    private var viewHd: ThisViewHd? = null

    override fun getCount(): Int {
        return list?.size?:0
    }

    override fun getItem(indx: Int): String? {
        return list?.get(indx)?.tit
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convert: View?, arg2: ViewGroup): View {
        var convertView = convert
        val tit: TextView?
        val sub: TextView?
        val btn: ImageView?
        val btna: Button?
        if (convertView == null) {
            convertView = MsgPagerLay().createMemItmVw(context)
            tit = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_mtit) as TextView?
            sub = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_stit) as TextView?
            btn = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_hiconbtn) as ImageView?
            btna = convertView.findViewById(R.id.msg_lay_cen_mempg_itm_chatnumbtn) as Button?
            viewHd = ThisViewHd()
            viewHd?.tit = tit
            viewHd?.sub = sub
            viewHd?.btn = btn
            viewHd?.btna = btna
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            tit = viewHd?.tit
            sub = viewHd?.sub
            btn = viewHd?.btn
            btna = viewHd?.btna
        }
        val itm =list?.get(position)
        var titc=itm?.tit?:""
        var subc = itm?.sub?:""

        tit?.text = titc
        sub?.text = subc
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

    internal inner class BtnListener(private val pos: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            val vid = v.id
            when (vid) {

            }


        }
    }

    internal inner class ThisViewHd {
        var tit: TextView? = null
        var sub: TextView? = null
        var btn: ImageView? = null
        var btna: Button? = null
    }

    companion object {
        private val TAG = "MemLsAdapter"
    }

}
