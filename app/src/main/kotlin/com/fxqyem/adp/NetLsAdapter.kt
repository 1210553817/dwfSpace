package com.fxqyem.adp

import android.content.Context
import android.media.MediaScannerConnection
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.fxqyem.R
import com.fxqyem.bean.MusicProvider
import com.fxqyem.bean.SongResult
import com.fxqyem.lay.HomeLay
import com.fxqyem.utils.HttpUtils
import com.fxqyem.utils.SDCardUtils
import com.fxqyem.vw.utilNotNull
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*

class NetLsAdapter(private val context: Context, var list: ArrayList<SongResult>?) : BaseAdapter() {
    private var viewHd: ThisViewHd? = null

    override fun getCount(): Int {
        return list?.size?:0
    }

    override fun getItem(indx: Int): String? {
        return list?.get(indx)?.songName
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mTextArtist: TextView?
        val mBtn: ImageButton?
        val nBtn: ImageButton?
        if (convertView == null) {
            convertView = HomeLay.createNetItemView(context)
            mTextTitle = convertView?.findViewById(R.id.main_net_ser_rstls_stit) as TextView?
            mTextArtist = convertView?.findViewById(R.id.main_net_ser_rstls_sart) as TextView?
            mBtn = convertView?.findViewById(R.id.main_net_ser_rstls_dld) as ImageButton?
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
        val sitm = list?.get(position)
        var songstr=sitm?.songName
        var artstr = sitm?.artistName
        songstr = if(utilNotNull(songstr)) songstr else "< unknown >"
        artstr = if(utilNotNull(artstr)) artstr else "< unknown >"
        if(utilNotNull(sitm?.albumName)) artstr +=" ♬"+sitm?.albumName
        val titstr = (position + 1).toString() + ". "+songstr
        mTextTitle?.text = titstr
        mTextArtist?.text =artstr
        mBtn?.setOnClickListener(MoreButtonListener(position))
        //nBtn?.setOnClickListener(MoreButtonListener(position))
        return convertView
    }

    internal inner class MoreButtonListener(private val pos: Int) : View.OnClickListener {
        override fun onClick(v: View) {

            var sitm = list?.get(pos)
            sitm?:return
            val vid = v.id
            when (vid) {
                R.id.main_net_ser_rstls_dld ->{
                    val svpath = SDCardUtils.sdCardPath+"/justmusic/download"
                    val svname = sitm.songName+".mp3"
                    Toast.makeText(context,"${svname}开始下载...",Toast.LENGTH_SHORT).show()
                    doAsync {
                        val dirs = File(svpath)
                        if(!dirs.exists()) dirs.mkdirs()
                        val dnurl = MusicProvider.getPlayUrl(sitm)
                        if(dnurl!=null) {
                            HttpUtils.doDownload(dnurl,svpath,svname,
                                    object: HttpUtils.StateCall{
                                        override fun onStateChange(state: Int,value: Int,result: String?){
                                            //Log.d(TAG,"${result} downloading... ${value}%")
                                            val svFile = File(svpath,svname)
                                            if(state==2) MediaScannerConnection.scanFile(context,arrayOf(svFile.absolutePath),null,null)
                                        }
                                    })
                        }
                        val lrctxt= MusicProvider.getLrcText(sitm)
                        if(null!=lrctxt&&lrctxt.length>100) {

                            val file = File(svpath +"/"+sitm.songName + ".lrc")
                            val pw = PrintWriter(FileOutputStream(file))
                            pw.print(lrctxt)
                            pw.close()
                        }
                        uiThread {
                            Toast.makeText(context,"${svname}下载完成！",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }


        }
    }


    internal inner class ThisViewHd {
        var tit: TextView? = null
        var art: TextView? = null
        var rm: ImageButton? = null
    }

    fun getList(): List<SongResult>? {
        return list
    }

    fun setList(list: List<SongResult>) {
        this.list = list as ArrayList<SongResult>
    }

    companion object {
        private val TAG = "netLsAdapter"
    }
}