package com.fxqyem.act

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.widget.*
import com.fxqyem.R
import com.fxqyem.adp.SongLsAdapter
import com.fxqyem.bean.*
import com.fxqyem.lay.FrgmHmLay
import com.fxqyem.utils.*
import com.fxqyem.vw.SldMenu
import org.jetbrains.anko.onClick
import android.os.CountDownTimer
import android.view.*
import kotlin.collections.ArrayList


class HmFrgm : Fragment() , OnBackListener {
    //Views
    private var coreLsView: ListView? = null
    private var lshdTitly: RelativeLayout? = null
    private var lshdBackBtn: ImageButton? = null
    private var lshdOptBtn: ImageButton? = null
    private var lshdTitTextVw: TextView? = null
    //Datas
    private var curSongLs: ArrayList<SongInfo>? = null
    private var songLsAdapter: SongLsAdapter? = null
    //Params
    private var oldLsHash: Int = 0
    private var dataTp: Int = 0
    private var stbnm: String? = null
    private var lsTit: String? = null

    private var locOptSelectSldMenu: SldMenu? = null
    private var locLsLoadTp=1
    private var curDataType=11
    private var curFirstLsItem=0

    var lsMode = 0//列表状态

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lsMode = 0
        return initViews()
    }

    private fun initViews(): View {
        val hdvw = FrgmHmLay().createView(activity)
        lshdTitly = hdvw.findViewById(R.id.frgm_hm_lshd_titly) as RelativeLayout
        coreLsView = hdvw.findViewById(R.id.frgm_hm_lshd_lsvw) as ListView
        lshdBackBtn = hdvw.findViewById(R.id.frgm_hm_lshd_backBtn) as ImageButton
        lshdOptBtn = hdvw.findViewById(R.id.frgm_hm_lshd_optBtn) as ImageButton
        lshdTitTextVw = hdvw.findViewById(R.id.frgm_hm_lshd_lsTit) as TextView
        initDatas()
        songLsAdapter = SongLsAdapter(this, curSongLs, dataTp, stbnm)
        coreLsView?.adapter = songLsAdapter
        //Listener
        lshdBackBtn?.setOnClickListener(BtnOnClickLsn())
        lshdOptBtn?.setOnClickListener(BtnOnClickLsn())
        coreLsView?.onItemClickListener = AdapterView.OnItemClickListener { arg0, view, position, id ->
            val sitm=curSongLs?.get(position)
            if(sitm!=null) {
                when (sitm.dataType) {
                    12 -> {
                        lshdTitTextVw?.text = sitm.name
                        initLocLs(MediaStore.Audio.Media.DATA,sitm.data)
                        if (null != songLsAdapter) {
                            songLsAdapter!!.list = curSongLs
                            songLsAdapter!!.notifyDataSetChanged()
                            coreLsView?.setSelectionFromTop(curFirstLsItem,0)
                        }
                    }
                    else -> {
                        val bd = Bundle()
                        bd.putInt("TYPE", 1)
                        bd.putInt("INDX", position)
                        bd.putBoolean("PLY", true)
                        if (curSongLs != null && oldLsHash != curSongLs?.hashCode()) {
                            bd.putSerializable(AppConstants.PLAYER_KEY_LS, curSongLs)
                            AppContext.instance?.updateTmpls(curSongLs)

                            oldLsHash = curSongLs?.hashCode() ?: 0
                        }

                        val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_INDX)
                        itt.putExtras(bd)
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(itt)
                    }
                }
            }

        }
        return hdvw
    }

    fun initDatas() {
        locLsLoadTp = PrefUtil.get(activity,AppConstants.PREF_KEY_FRGMHM_LOCOPT,1, AppConstants.PREF_NAME_DATAS) as Int
        lsTit = arguments.getString("tit")
        dataTp = arguments.getInt("tp")
        lshdTitTextVw?.text = lsTit
        when (dataTp) {
            1 -> {
                when(locLsLoadTp){
                    1 -> {
                        initLocLs(null,null)
                    }
                    2 -> {
                        curSongLs=initSongDirGrp()
                    }
                }

            }
            2 -> {
                stbnm = arguments.getString("tbnm")
                initFavSongLs(stbnm)
            }
            else -> {
            }
        }
        if (null != songLsAdapter) {
            lsMode =0
            songLsAdapter?.lsMode = lsMode
            songLsAdapter?.list = curSongLs
            songLsAdapter?.notifyDataSetChanged()
            coreLsView?.setSelectionFromTop(curFirstLsItem,0)
            object : CountDownTimer(500, 500) {
                override fun onFinish() {
                    coreLsView?.setSelectionFromTop(curFirstLsItem,0)
                }
                override fun onTick(millisUntilFinished: Long) {}
            }.start()
        }

    }

    private fun initLocLs(whek: String?,whev: String?) {
        curFirstLsItem=0
        when(curDataType){
            12 -> {
                curDataType = 121
                val act = activity as MainActivity
                var fvp = coreLsView?.firstVisiblePosition?:0
                act?.push2BackLsnStack(BackLsnHolder(121,fvp, this@HmFrgm))
            }
            else ->{
                curDataType = 11
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionUtils.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, MainActivity.SONGLS_REQUEST_CODE)) {
                curSongLs = initSongLs(whek,whev)
            } else {
                curSongLs = ArrayList<SongInfo>()
                Toast.makeText(activity, "没有‘存储空间’权限，无法获取本地音乐", Toast.LENGTH_SHORT).show()
            }
        } else {
            curSongLs = initSongLs(whek,whev)

        }
    }

    private fun initSongLs(whek: String?,whev: String?): ArrayList<SongInfo> {
        val mResolver = activity.applicationContext.contentResolver
        var grps: String?=null
        var selargs:Array<String>?=null
        if(whek!=null&&whev!=null){
            grps="$whek like ? and $whek not like ?"
            selargs= arrayOf(whev+"%",whev+"%/%")
        }

        val AUDIO_KEYS = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA)
        val c = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_KEYS, grps, selargs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
        val lst = ArrayList<SongInfo>()
        c?:return lst
        while (c.moveToNext()) {
            val info = SongInfo()
            info.oid = c.getInt(c.getColumnIndex(MediaStore.Audio.Media._ID))
            info.name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE))
            info.artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            info.album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            info.disName = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
            info.duration = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.DURATION))
            info.size = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.SIZE))
            info.data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))
            info.dataType=curDataType
            lst.add(info)
        }
        c?.close()
        return lst
    }

    private fun initSongDirGrp() : ArrayList<SongInfo>{
        curDataType=12
        val mResolver = activity.applicationContext.contentResolver
        val grps="${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) GROUP BY (replace(${MediaStore.Files.FileColumns.DATA},${MediaStore.Files.FileColumns.DISPLAY_NAME},'')"

        val F_KEYS = arrayOf("count(*) as FXQY_CT","replace(${MediaStore.Files.FileColumns.DATA},${MediaStore.Files.FileColumns.DISPLAY_NAME},'') as FXQY_NM")
        val F_VALS = arrayOf("${MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO}")
        val c = mResolver.query(MediaStore.Files.getContentUri("external"), F_KEYS, grps, F_VALS, MediaStore.Files.FileColumns.PARENT)
        val lst = ArrayList<SongInfo>()
        while (c != null && c.moveToNext()) {
            val info = SongInfo()
            val dirnm = c.getString(c.getColumnIndex("FXQY_NM"))
            dirnm?:continue
            val dirs: List<String> = dirnm.split("/")
            if(dirs.size>2){
                info.name=dirs[dirs.size-2]
            }else {
                info.name = c.getString(c.getColumnIndex("FXQY_NM"))
            }
            val ctsz = c.getInt(c.getColumnIndex("FXQY_CT"))
            info.artist = "$ctsz 首 "+dirnm
            info.data = dirnm
            info.dataType=curDataType
            lst.add(info)
        }
        c?.close()
        return lst
    }

    private fun initFavSongLs(tbnm: String?) {
        curDataType=21
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, activity, null)
        curSongLs = dbUtil.queryFavSongLs(tbnm,curDataType)
        dbUtil.close()
    }

    override fun onResume() {
        super.onResume()
        AndUtil.initStatus(activity, lshdTitly as View)
    }

    private inner class BtnOnClickLsn : View.OnClickListener {
        override fun onClick(v: View) {
            val act= activity as MainActivity
            val vid = v.id
            when (vid) {
                R.id.frgm_hm_lshd_backBtn -> {
                    if(curDataType==121){
                        val bkhd = act?.popBackLsnStack(121, this@HmFrgm)
                        curFirstLsItem = bkhd?.requestValue?:0
                        initDatas()
                    }else {
                        this@HmFrgm.fragmentManager.popBackStack()
                    }
                }
                R.id.frgm_hm_lshd_optBtn -> {
                  createLocOptMenu()
                }
                else -> return
            }

        }
    }
    private fun createLocOptMenu(){
        val act= activity as MainActivity
        val cttVw = FrgmHmLay().createLocOptSelectMenu(activity)
        val parentView = act?.findViewById(R.id.main_vwctn_frmly) as ViewGroup
        locOptSelectSldMenu = SldMenu.create(act, cttVw, parentView)
        locOptSelectSldMenu?.setOnStateChangeListener(object:SldMenu.OnStateChangeListener{
            override fun onStateChange(state: Int) {
                if (state == SldMenu.MENU_STATE_GONE_START) {
                    act?.popBackLsnStack(1, this@HmFrgm)
                } else if (state == SldMenu.MENU_STATE_SHOW) {
                    act?.push2BackLsnStack(BackLsnHolder(1, this@HmFrgm))
                } else if (state == SldMenu.MENU_STATE_GONE) {
                    locOptSelectSldMenu = null
                }
            }
        })
        locOptSelectSldMenu?.show(1)
        val tophd = cttVw.findViewById(R.id.frgm_hm_loc_optmn_selector_tophd) as LinearLayout
        AndUtil.initStatus(act, tophd)
        val allBtn = cttVw.findViewById(R.id.frgm_hm_loc_optmn_selector_all) as LinearLayout
        val folderBtn = cttVw.findViewById(R.id.frgm_hm_loc_optmn_selector_folder) as LinearLayout
        val mulckBtn = cttVw.findViewById(R.id.frgm_hm_songls_optmn_selector_multck) as LinearLayout
        val delBtn = cttVw.findViewById(R.id.frgm_hm_songls_optmn_selector_del) as LinearLayout
        val add2fBtn = cttVw.findViewById(R.id.frgm_hm_songls_optmn_selector_add2f) as LinearLayout
        val add2pBtn = cttVw.findViewById(R.id.frgm_hm_songls_optmn_selector_add2p) as LinearLayout
        val sortsvBtn = cttVw.findViewById(R.id.frgm_hm_songls_optmn_selector_sortsv) as LinearLayout
        when(curDataType){
            11 -> {
                allBtn.visibility = View.VISIBLE
                folderBtn.visibility = View.VISIBLE
                delBtn.visibility = View.GONE
                sortsvBtn.visibility = View.GONE
                if(lsMode == 1) {
                    add2fBtn.visibility = View.VISIBLE
                    add2pBtn.visibility = View.VISIBLE
                    mulckBtn.visibility = View.GONE
                }else{
                    mulckBtn.visibility = View.VISIBLE
                    add2fBtn.visibility = View.GONE
                    add2pBtn.visibility = View.GONE
                }
            }
            12 ->{
                allBtn.visibility = View.VISIBLE
                folderBtn.visibility = View.VISIBLE
                mulckBtn.visibility = View.GONE
                delBtn.visibility = View.GONE
                add2fBtn.visibility = View.GONE
                add2pBtn.visibility = View.GONE
                sortsvBtn.visibility = View.GONE

            }
            121 ->{
                allBtn.visibility = View.VISIBLE
                folderBtn.visibility = View.VISIBLE
                delBtn.visibility = View.GONE
                sortsvBtn.visibility = View.GONE
                if(lsMode == 1) {
                    add2fBtn.visibility = View.VISIBLE
                    add2pBtn.visibility = View.VISIBLE
                    mulckBtn.visibility = View.GONE
                }else{
                    mulckBtn.visibility = View.VISIBLE
                    add2fBtn.visibility = View.GONE
                    add2pBtn.visibility = View.GONE
                }
            }
            21 ->{
                allBtn.visibility = View.GONE
                folderBtn.visibility = View.GONE
                if(lsMode == 1) {
                    mulckBtn.visibility = View.GONE
                    delBtn.visibility = View.VISIBLE
                    add2fBtn.visibility = View.VISIBLE
                    add2pBtn.visibility = View.VISIBLE
                    sortsvBtn.visibility = View.VISIBLE

                }else{
                    mulckBtn.visibility = View.VISIBLE
                    delBtn.visibility = View.GONE
                    add2fBtn.visibility = View.GONE
                    add2pBtn.visibility = View.GONE
                    sortsvBtn.visibility = View.GONE
                }
            }
        }
        allBtn.onClick {
            locLsLoadTp = 1
            locOptSelectSldMenu?.cancle()
            PrefUtil.put(act, AppConstants.PREF_KEY_FRGMHM_LOCOPT, locLsLoadTp, AppConstants.PREF_NAME_DATAS)
            initDatas()

        }
        folderBtn.onClick {
            locLsLoadTp = 2
            locOptSelectSldMenu?.cancle()
            PrefUtil.put(act, AppConstants.PREF_KEY_FRGMHM_LOCOPT, locLsLoadTp, AppConstants.PREF_NAME_DATAS)
            initDatas()

        }
        mulckBtn.onClick {
            doLsMode(1)
        }
        delBtn.onClick {
            val csls = getCkdSongLs(0)
            if(csls!=null&&csls.size>0) {
                val ids = ArrayList<Int>()
                for (citm in csls) {
                    ids.add(citm.id)
                }
                songLsAdapter?.deleteFavLsItem(stbnm,ids.toTypedArray())
                Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show()
                initFavSongLs(stbnm)
                songLsAdapter?.list = curSongLs
                doLsMode(0)
            }else{
                Toast.makeText(activity,"没有要删除的数据！",Toast.LENGTH_SHORT).show()
            }

        }
        add2fBtn.onClick {
            val csls = getCkdSongLs(0)
            if(csls!=null&&csls.size>0) {
                songLsAdapter?.initChsFavMenu(csls)
                doLsMode(0)
            }else{
                Toast.makeText(activity,"没有要添加的数据！",Toast.LENGTH_SHORT).show()
            }
        }
        add2pBtn.onClick {
            val csls = getCkdSongLs(0)
            val tmpLs = AppContext.instance?.tmpLs
            if(csls!=null&&csls.size>0) {
                tmpLs?.addAll(csls)
                AppContext.instance?.updateCurrentTmpls2Db()
                Toast.makeText(activity,"添加成功！",Toast.LENGTH_SHORT).show()
                doLsMode(0)
            }else{
                Toast.makeText(activity,"没有要添加的数据！",Toast.LENGTH_SHORT).show()
            }


        }
        sortsvBtn.onClick {
            val csls = getCkdSongLs(1)
            if(csls!=null&&csls.size>0) {
                songLsAdapter?.truncatFavLsTable(stbnm)
                songLsAdapter?.insertLs2Fv(stbnm,csls)
                Toast.makeText(activity,"排序成功！",Toast.LENGTH_SHORT).show()
                initFavSongLs(stbnm)
                songLsAdapter?.list = curSongLs
                doLsMode(0)
            }else{
                Toast.makeText(activity,"没有要排序的数据！",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCkdSongLs(type: Int): ArrayList<SongInfo>?{
        val ckdLs =songLsAdapter?.ckdLs
        ckdLs?:return null
        if(ckdLs.size<1) return null
        val csls = songLsAdapter?.list
        csls?:return null
        val list =  ArrayList<SongInfo>()
        for(itm in ckdLs){
            list.add(csls.get(itm))
        }
        if(0==type)return list
        for(indx in csls.indices){
            val ita = csls[indx]
            if(ckdLs.indexOf(indx)<0){
                list.add(ita)
            }
        }

        return list
    }

    private fun doLsMode(md: Int){
        lsMode = md
        songLsAdapter?.lsMode = lsMode
        songLsAdapter?.notifyDataSetChanged()
        locOptSelectSldMenu?.cancle()
    }

//    private fun scanFiles(paths: ArrayList<String>,mimeTypes: Array<String>,callback: MediaScannerConnection.OnScanCompletedListener) {
//        val fpArr = ArrayList<String>()
//        paths.map {path ->
//            val pf=File(path)
//            if (pf.isDirectory){
//                val files = pf.listFiles()
//                if (files != null){
//                    files.map{f ->
//                        if (!f.isDirectory){
//                            fpArr.add(f.absolutePath)
//                        }
//                    }
//                }
//            }
//        }
//        MediaScannerConnection.scanFile(activity,fpArr.toTypedArray(),mimeTypes,callback)
//        callback.onScanCompleted("ALL_FINISHED",null)
//    }


    companion object {
        private val TAG = "HmFrgm"
    }

    override fun onBackKeyUp(reqVal: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            1 -> locOptSelectSldMenu?.cancle()
            121 -> {
                curFirstLsItem=reqVal
                initDatas()
            }
        }
        return true
    }

}
