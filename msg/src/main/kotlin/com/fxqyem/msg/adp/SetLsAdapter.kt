package com.fxqyem.msg.adp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgPagerLay
import com.fxqyem.msg.lay.SettingLay
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.appColorArrayLightGreen
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.find

class SetLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){
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
        val img: ImageView?
        if (convertView == null) {
            convertView = SettingLay().createSetItmVw(context)
            tit = convertView.findViewById(R.id.setting_pglay_item_mtit) as TextView?
            img = convertView.findViewById(R.id.setting_pglay_item_himg) as ImageView?
            viewHd = ThisViewHd()
            viewHd?.tit = tit
            viewHd?.img = img
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            tit = viewHd?.tit
            img = viewHd?.img
        }
        val itm =list?.get(position)
        var titc=itm?.tit?:""
        var imgi = itm?.cmd

        tit?.text = titc
        if(imgi!=null)img?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(context,imgi, appColorArrayLightGreen)
        return convertView
    }


    internal inner class ThisViewHd {
        var tit: TextView? = null
        var img: ImageView? = null
    }

    companion object {
        private val TAG = "SetLsAdapter"
    }

}
