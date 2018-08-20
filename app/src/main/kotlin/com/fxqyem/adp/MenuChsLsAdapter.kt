package com.fxqyem.adp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.fxqyem.R
import com.fxqyem.bean.SongGrpInfo
import com.fxqyem.lay.FrgmMenuLay

class MenuChsLsAdapter(private val context: Context, var list: List<SongGrpInfo>?) : BaseAdapter() {
    private var viewHd: ThisViewHd? = null

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(arg0: Int): Any {
        return list!![arg0].name as Any
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mTextArtist: TextView?
        if (convertView == null) {
            convertView = FrgmMenuLay().chsFavItm(context)
            mTextTitle = convertView!!.findViewById(R.id.frgm_hm_mn_chsfav_item_tit) as TextView
            mTextArtist = convertView.findViewById(R.id.frgm_hm_mn_chsfav_item_sub) as TextView
            viewHd = ThisViewHd()
            viewHd!!.tit = mTextTitle
            viewHd!!.art = mTextArtist
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            mTextTitle = viewHd!!.tit
            mTextArtist = viewHd!!.art
        }

        mTextTitle?.text = (position + 1).toString() + ". " + list!![position].name
        mTextArtist?.text = list!![position].subname

        return convertView
    }

    internal inner class ThisViewHd {
        var tit: TextView? = null
        var art: TextView? = null

    }

    companion object {
        private val TAG = "MenuChsLsAdapter"
    }


}
