package com.fxqyem.bean

import android.app.Application
import android.content.Context
import com.fxqyem.utils.DbUtil
import com.fxqyem.utils.PrefUtil
import com.fxqyem.utils.SDCardUtils
import com.fxqyem.vw.utilSqlNull
import org.jetbrains.anko.doAsync
import java.util.*


class AppContext : Application() {

    companion object{
        var instance: AppContext?=null
    }

    override fun onCreate() {
        super.onCreate()
        AppContext.instance = this

        initCurIndex()
        initPlayMod()
        initTmpls()

    }

    var curIndex=0
    var playMode=0
    var appVolume=0
    var tmpLs: ArrayList<SongInfo>? = null

    fun initCurIndex(){
        curIndex = PrefUtil.get(this,AppConstants.PREF_KEY_TMPLS_INDX,0, AppConstants.PREF_NAME_DATAS) as Int
    }

    fun updateCurIndex(nindx: Int){
        curIndex = nindx
        PrefUtil.put(this, AppConstants.PREF_KEY_TMPLS_INDX, nindx, AppConstants.PREF_NAME_DATAS)
    }

    fun initPlayMod(){
        playMode = PrefUtil.get(this,AppConstants.PREF_KEY_BKG_SLD_PLAYMODE,0, AppConstants.PREF_NAME_PARAMS) as Int
    }
    fun setPlayMod(ctx: Context, pm: Int){
        playMode=pm
        PrefUtil.put(ctx, AppConstants.PREF_KEY_BKG_SLD_PLAYMODE,pm, AppConstants.PREF_NAME_PARAMS)
    }

    fun initTmpls(){
        tmpLs = ArrayList<SongInfo>()
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, this, null)
        val c = dbUtil.query(AppConstants.DB_TBNAME_TMPLS,
                arrayOf("sid", "snm", "sart", "salb", "lrcnm", "lrcurl", "picnm", "picurl", "datanm", "dataurl", "oid", "sdur", "ssz", "sindx","songid","type","qmid"),
                null, null, null, null,"sindx")
        while (c != null && c.moveToNext()) {
            val info = SongInfo()
            info.id = c.getInt(c.getColumnIndex("sid"))
            info.name = c.getString(c.getColumnIndex("snm"))
            info.artist = c.getString(c.getColumnIndex("sart"))
            info.album = c.getString(c.getColumnIndex("salb"))
            info.lrcNm = c.getString(c.getColumnIndex("lrcnm"))
            info.lrcUrl = c.getString(c.getColumnIndex("lrcurl"))
            info.picNm = c.getString(c.getColumnIndex("picnm"))
            info.picUrl = c.getString(c.getColumnIndex("picurl"))
            info.data = c.getString(c.getColumnIndex("datanm"))
            info.dataUrl = c.getString(c.getColumnIndex("dataurl"))
            info.oid = c.getInt(c.getColumnIndex("oid"))
            info.duration = c.getInt(c.getColumnIndex("sdur"))
            info.size = c.getInt(c.getColumnIndex("ssz"))
            info.indx = c.getInt(c.getColumnIndex("sindx"))

            info.songId = c.getString(c.getColumnIndex("songid"))
            info.type = c.getString(c.getColumnIndex("type"))
            info.qmid = c.getString(c.getColumnIndex("qmid"))
            tmpLs?.add(info)
        }
        c?.close()
        dbUtil.close()
    }


    fun updateTmpls(csls: ArrayList<SongInfo>?) {
        if(null==csls) return
        tmpLs = csls
        doAsync {
            val dbUtila = DbUtil()
            dbUtila.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, this@AppContext, null)
            dbUtila.doForSql("DELETE FROM " + AppConstants.DB_TBNAME_TMPLS)
            dbUtila.doForSql("UPDATE sqlite_sequence SET seq = 0 WHERE name ='" + AppConstants.DB_TBNAME_TMPLS + "'")
            dbUtila.close()
            for (cif in csls) {
                insert2Tmpls(cif)
            }
        }
    }
    fun updateCurrentTmpls2Db() {
        updateTmpls(tmpLs)
    }

    fun add2Tmpls(sinfo: SongInfo){
        doAsync {
            insert2Tmpls(sinfo)
            tmpLs?.add(sinfo)
        }
    }

    private fun insert2Tmpls(sinfo: SongInfo) {
        val cv: MutableMap<String, String>//favLs
        val dbUtila = DbUtil()
        dbUtila.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, this, null)
        cv = HashMap<String, String>()
        cv.put("snm", utilSqlNull(sinfo.name))
        cv.put("sart", utilSqlNull(sinfo.artist))
        cv.put("salb", utilSqlNull(sinfo.album))
        cv.put("lrcnm", utilSqlNull(sinfo.lrcNm))
        cv.put("lrcurl",utilSqlNull(sinfo.lrcUrl))
        cv.put("picnm", utilSqlNull(sinfo.picNm))
        cv.put("picurl",utilSqlNull(sinfo.picUrl))
        cv.put("datanm",utilSqlNull(sinfo.data))
        cv.put("dataurl",utilSqlNull(sinfo.dataUrl))
        cv.put("oid", sinfo.oid.toString() + "")
        cv.put("sdur", sinfo.duration.toString() + "")
        cv.put("ssz", sinfo.size.toString() + "")
        cv.put("sindx", "ifnull((select max(sindx)+1 from " + AppConstants.DB_TBNAME_TMPLS + "),1)")

        cv.put("songid", utilSqlNull(sinfo.songId))
        cv.put("type", utilSqlNull(sinfo.type))
        cv.put("qmid",utilSqlNull(sinfo.qmid))
        dbUtila.insertUseMap(AppConstants.DB_TBNAME_TMPLS, cv)
        dbUtila.close()
    }


}