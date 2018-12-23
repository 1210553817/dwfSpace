package com.fxqyem.adp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.fxqyem.R
import com.fxqyem.bean.AppConstants
import com.fxqyem.bean.AppContext
import com.fxqyem.bean.SongInfo
import com.fxqyem.lay.TmplsLay
import com.fxqyem.vw.utilNotNull
import java.util.*

class TmpLsAdapter(private val context: Context, var list: ArrayList<SongInfo>?) : BaseAdapter() {
    private var viewHd: ThisViewHd? = null
    var curSongIndx = -1
    private var isDelNow: Boolean = false


    override fun getCount(): Int {
        return list?.size?:0
    }

    override fun getItem(indx: Int): String? {
        return list?.get(indx)?.name
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mTextArtist: TextView?
        val mBtn: ImageButton?
        if (convertView == null) {
            convertView = TmplsLay.createItmView(context)
            mTextTitle = convertView?.findViewById(R.id.tmp_ls_item_stit) as TextView?
            mTextArtist = convertView?.findViewById(R.id.tmp_ls_item_sart) as TextView?
            mBtn = convertView?.findViewById(R.id.tmp_ls_item_rmBtn) as ImageButton?
            viewHd = ThisViewHd()
            viewHd?.tit = mTextTitle
            viewHd?.art = mTextArtist
            viewHd?.rm = mBtn
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            mTextTitle = viewHd?.tit
            mTextArtist = viewHd?.art
            mBtn = viewHd?.rm
        }
        var songstr=list?.get(position)?.name
        var artstr = list?.get(position)?.artist
        songstr = if(utilNotNull(songstr)) songstr else "< unknown >"
        artstr = if(utilNotNull(artstr)) artstr else "< unknown >"
        val titstr = (position + 1).toString() + ". "+songstr

        mTextTitle?.text = titstr
        if (curSongIndx >= 0 && position == curSongIndx) {
            mTextTitle?.setTextColor(0xff049ff1.toInt())
            mTextArtist?.setTextColor(0xff049ff1.toInt())
        } else {
            mTextTitle?.setTextColor(0xff444444.toInt())
            mTextArtist?.setTextColor(0xffaaaaaa.toInt())
        }
        mTextArtist?.text = artstr
        mBtn?.setOnClickListener(MoreButtonListener(position))

        return convertView
    }

    internal inner class MoreButtonListener(private val pos: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            val vid = v.id
            when (vid) {
                R.id.tmp_ls_item_rmBtn ->
                    if (list != null && list!!.size > 0) {
                        if (isDelNow) {
                            Toast.makeText(context, "正在进行删除任务，请稍后！", Toast.LENGTH_SHORT).show()
                            return
                        }
                        val cinf = list?.get(pos)
                        val csid = cinf?.id?:return
                        list?.removeAt(pos)
                        AppContext.instance?.tmpLs=list
                        //rmOneItemFromTmpLs(csid)

                        val isp: Boolean
                        if (list?.size?:-1 > 0) {
                            if (curSongIndx > pos) {
                                curSongIndx--
                            } else if (pos == list!!.size) {
                                curSongIndx = 0
                            }
                        } else {
                            curSongIndx = -1
                        }
                        this@TmpLsAdapter.notifyDataSetChanged()

                        val bd = Bundle()
                        bd.putInt("TYPE", 2)
                        bd.putSerializable(AppConstants.PLAYER_KEY_LS, list)
                        bd.putInt("OLDINDX", pos)
                        bd.putInt("INDX", curSongIndx)
                        bd.putBoolean("PLY", false)

                        val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_INDX)
                        itt.putExtras(bd)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(itt)
                    }
            }


        }
    }

//    private fun rmOneItemFromTmpLs(sid: Int) {
//        isDelNow = true
//        doAsync {
//            val dbUtila = DbUtil()
//            dbUtila.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null, 1)
//            dbUtila.delete(AppConstants.DB_TBNAME_TMPLS, "sid=?", arrayOf(sid.toString() + ""))
//            dbUtila.close()
//
//            isDelNow = false
//            AppContext.instance?.initTmpls()
//        }
//
//    }


    internal inner class ThisViewHd {
        var tit: TextView? = null
        var art: TextView? = null
        var rm: ImageButton? = null
    }

    fun getList(): List<SongInfo>? {
        return list
    }

    fun setList(list: List<SongInfo>) {
        this.list = list as ArrayList<SongInfo>
    }

    companion object {
        private val TAG = "TmpLsAdapter"
    }
}
