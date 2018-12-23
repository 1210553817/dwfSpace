package com.fxqyem.msg.adp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.SettingLay
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.appColorArrayLightGreen
import org.jetbrains.anko.backgroundDrawable

class SetLsAdapter(private val context: Context, var list: ArrayList<MsgEnt>?) : BaseAdapter(){

    override fun getCount() = list?.size?:0

    override fun getItem(indx: Int) = list?.get(indx)?.tit

    override fun getItemId(indx: Int) = indx.toLong()

    override fun getView(position: Int, convert: View?, arg2: ViewGroup): View {
        var convertView = convert
        val vwHolder: VwHolder
        val tit: TextView?
        val img: ImageView?
        if (convertView == null) {
            convertView = SettingLay.createSetItmVw(context)
            tit = convertView.findViewById(R.id.setting_pglay_item_mtit) as TextView?
            img = convertView.findViewById(R.id.setting_pglay_item_himg) as ImageView?
            vwHolder = VwHolder()
            vwHolder.tit = tit
            vwHolder.img = img
            convertView.tag = vwHolder
        } else {
            vwHolder = convertView.tag as VwHolder
            tit = vwHolder.tit
            img = vwHolder.img
        }
        val itm =list?.get(position)
        val titc=itm?.tit?:""
        val imgi = itm?.cmd

        tit?.text = titc
        if(imgi!=null)img?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(context,imgi, appColorArrayLightGreen)
        return convertView
    }


    internal inner class VwHolder {
        var tit: TextView? = null
        var img: ImageView? = null
    }

    companion object {
        //private val TAG = "SetLsAdapter"
    }

}
