package com.fxqyem.adp

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.fxqyem.R
import com.fxqyem.act.HmFrgm
import com.fxqyem.act.MainActivity
import com.fxqyem.bean.*
import com.fxqyem.lay.FrgmHmLay
import com.fxqyem.lay.FrgmMenuLay
import com.fxqyem.lay.HomeLay
import com.fxqyem.utils.AndUtil
import com.fxqyem.utils.DbUtil
import com.fxqyem.utils.PrefUtil
import com.fxqyem.utils.SDCardUtils
import com.fxqyem.vw.SldMenu
import com.fxqyem.vw.utilSqlNull
import org.jetbrains.anko.textResource
import java.util.*

class SongLsAdapter(frgmt: Fragment, var list: List<SongInfo>?, private val dataTp: Int, private val stbnm: String?) : BaseAdapter(), OnBackListener {
    private var viewHd: ThisViewHd? = null
    private val context: Context
    private val myfrgmt: HmFrgm
    private var sldMenua: SldMenu? = null
    private var sldMenub: SldMenu? = null
    private var sldMenuc: SldMenu? = null

    var lsMode = 0//列表状态
        set(value) {
            field = value
            if(field==1) {
                ckdLs = ArrayList()
            }else{
                ckdLs = null
            }
        }
    var ckdLs: ArrayList<Int>?=null
    var morBtnPosition=-1

    private val tmpLs: ArrayList<SongInfo>?= AppContext.instance?.tmpLs

    init {
        this.myfrgmt = frgmt as HmFrgm
        this.context = frgmt.getActivity()
    }

    override fun getCount(): Int {
        return list?.size?:0
    }

    override fun getItem(arg0: Int): Any {

        return list?.get(arg0)?.name as Any
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mTextArtist: TextView?
        val mBtn: ImageButton?
        val ckBtn: Button?
        if (convertView == null) {
            convertView = FrgmHmLay().createItmView(context)
            mTextTitle = convertView?.findViewById(R.id.frgm_hm_lsitm_titTxt) as TextView?
            mTextArtist = convertView.findViewById(R.id.frgm_hm_lsitm_subtitTxt) as TextView?
            mBtn = convertView.findViewById(R.id.frgm_hm_lsitm_moreBtn) as ImageButton?
            ckBtn = convertView.findViewById(R.id.frgm_hm_lsitm_ckBtn) as Button?
            viewHd = ThisViewHd()
            viewHd?.tit = mTextTitle
            viewHd?.art = mTextArtist
            viewHd?.mor = mBtn
            viewHd?.ck = ckBtn
            convertView.tag = viewHd
        } else {
            viewHd = convertView.tag as ThisViewHd
            mTextTitle = viewHd?.tit
            mTextArtist = viewHd?.art
            mBtn = viewHd?.mor
            ckBtn = viewHd?.ck
        }
        val sitm = list?.get(position)
        sitm?:return convertView
        mTextTitle?.text = (position + 1).toString() + ". " + sitm.name
        mTextArtist?.text = sitm.artist
        ckBtn?.visibility = View.GONE
        if(sitm.dataType==12){
            mBtn?.visibility = View.GONE
        }else {
            mBtn?.visibility = View.VISIBLE
            mBtn?.setOnClickListener(MoreButtonListener(position))
            if(lsMode==1){
                ckBtn?.visibility = View.VISIBLE
                ckBtn?.setOnClickListener(MoreButtonListener(position))
                refreshCkbtns(position,ckBtn?:null)

            }
        }

        return convertView
    }

    private fun refreshCkbtns(postion: Int,ckBtn: Button?){
        ckBtn?:return
        val cmp = ckdLs
        cmp?:return
        val cpos=cmp.indexOf(postion)
        if(cpos>-1){
            ckBtn.text = (cpos+1).toString()
        }else{
            ckBtn.text = ""
        }

        //Log.d(TAG,"postion: $postion <------------------------------------------------------")

    }


    internal inner class MoreButtonListener(private val pos: Int) : View.OnClickListener {
        private var mAct: MainActivity? = null

        override fun onClick(v: View) {
            val vid = v.id
            mAct = context as MainActivity
            val cttVw: View?
            val parentView: ViewGroup?
            when (vid) {
                R.id.frgm_hm_lsitm_moreBtn -> {
                    morBtnPosition = pos
                    cttVw = FrgmMenuLay().createMenu(context)
                    parentView = mAct?.findViewById(R.id.main_vwctn_frmly) as ViewGroup?
                    sldMenua = SldMenu.create(mAct, cttVw, parentView)
                    sldMenua?.setOnStateChangeListener (object:SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct?.popBackLsnStack(BK_KEY_A, this@SongLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct?.push2BackLsnStack(BackLsnHolder(BK_KEY_A, this@SongLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenua = null
                            }
                        }
                    })
                    sldMenua?.show(0)

                    val addBtn = cttVw.findViewById(R.id.frgm_hm_mn_add2fav) as LinearLayout
                    val add2pBtn = cttVw.findViewById(R.id.frgm_hm_mn_add2p) as LinearLayout
                    val rmBtn = cttVw.findViewById(R.id.frgm_hm_mn_rm) as LinearLayout
                    val infoBtn = cttVw.findViewById(R.id.frgm_hm_mn_info) as LinearLayout
                    val nextPlayBtn = cttVw.findViewById(R.id.frgm_hm_mn_next2play) as LinearLayout

                    addBtn.setOnClickListener(MoreButtonListener(pos))
                    add2pBtn.setOnClickListener(MoreButtonListener(pos))
                    rmBtn.setOnClickListener(MoreButtonListener(pos))
                    infoBtn.setOnClickListener(MoreButtonListener(pos))
                    nextPlayBtn.setOnClickListener(MoreButtonListener(pos))
                    if (1 == dataTp) {
                        rmBtn.visibility = View.GONE
                    }
                }
                R.id.frgm_hm_mn_add2fav -> {
                    val sls=ArrayList<SongInfo>()
                    val titm = list?.get(pos)?:return
                    sls.add(titm)
                    initChsFavMenu(sls)
                }
                R.id.frgm_hm_mn_chsfav_new -> {
                    cttVw = HomeLay().createFavOptEdit(context)
                    parentView = mAct?.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenuc = SldMenu.create(mAct, cttVw, parentView)
                    sldMenuc?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct?.popBackLsnStack(BK_KEY_C, this@SongLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct?.push2BackLsnStack(BackLsnHolder(BK_KEY_C, this@SongLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenuc = null
                            }
                        }
                    })
                    sldMenuc?.show(1)
                    sldMenub?.cancle()
                    val titTxtew = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    AndUtil.initStatus(mAct, cttVw)
                    val crenewOkBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_edtOkBtn) as Button
                    crenewOkBtn.textResource=R.string.hm_fav_mn_favsv
                    crenewOkBtn.id = R.id.hm_song_opt_menu_crefav_creOkBtn
                    crenewOkBtn.setOnClickListener(MoreButtonListener(pos))

                    titTxtew.requestFocus()
                    val imm = titTxtew.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)


                }
                R.id.hm_song_opt_menu_crefav_creOkBtn -> {
                    cttVw = sldMenuc?.contentView?:return
                    val titTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    val stitTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_subtitEtxt) as EditText
                    val tStr = titTxt.text.toString()
                    val stStr = stitTxt.text.toString()
                    val sls=ArrayList<SongInfo>()
                    val titm = list?.get(pos)?:return
                    sls.add(titm)
                    insert2NewFavInfo(tStr, stStr,sls)
                    context.initHmFavLs()
                    sldMenuc?.cancle()
                    Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show()
                }
                R.id.frgm_hm_mn_add2p -> {
                    val tmpIfo = list?.get(pos)?:return
                    AppContext.instance?.add2Tmpls(tmpIfo)
                    sldMenua?.cancle()
                    Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show()
                }
                R.id.frgm_hm_mn_rm -> {
                    val sInfo = list?.get(pos)?:return
                    val sid = sInfo.id
                    val dbUtil = DbUtil()
                    dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
                    dbUtil.delete(stbnm, "sid=?", arrayOf("" + sid))
                    dbUtil.close()
                    Toast.makeText(context, "移除成功！", Toast.LENGTH_SHORT).show()
                    myfrgmt.initDatas()
                    sldMenua?.cancle()
                }
                R.id.frgm_hm_mn_info -> {
                    cttVw = FrgmMenuLay().createFrgmInfo(context)
                    parentView = mAct!!.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenub = SldMenu.create(mAct, cttVw, parentView)
                    sldMenub!!.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            val mAct = context
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct.popBackLsnStack(BK_KEY_B, this@SongLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct.push2BackLsnStack(BackLsnHolder(BK_KEY_B, this@SongLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenub = null
                            }
                        }
                    })
                    sldMenua?.cancle()
                    sldMenub?.show(0)
                    val sInf = list!![pos]
                    val tittxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_titShtxt) as TextView
                    tittxt.text = sInf.name
                    val arttxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_artistShtxxt) as TextView
                    arttxt.text = sInf.artist
                    val albtxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_albShtxt) as TextView
                    albtxt.text = sInf.album
                    val durtxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_durShtxt) as TextView
                    durtxt.text = AndUtil.timeFormate(sInf.duration.toLong())
                    val sztxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_szShtxt) as TextView
                    sztxt.text = sInf.size.toString() + " Byte"
                    val datatxt = cttVw.findViewById(R.id.hm_song_opt_menu_info_dataShtxt) as TextView
                    datatxt.text = sInf.data
                }
                R.id.frgm_hm_mn_next2play -> {
                    val sInfn = list!![pos]
                    var csongindx: Int = -1
                    val ogjj = PrefUtil.get(context, AppConstants.PREF_KEY_TMPLS_INDX, csongindx, AppConstants.PREF_NAME_DATAS)
                    if (ogjj != null) {
                        csongindx = ogjj as Int
                    }
                    var csongLs: ArrayList<SongInfo>? = tmpLs
                    if (csongindx < 0 || csongLs == null || csongLs.size == 0) {
                        csongLs = ArrayList<SongInfo>()
                        csongLs.add(sInfn)
                    } else {
                        if (csongindx < csongLs.size - 1) {
                            csongLs.add(csongindx!! + 1, sInfn)
                        } else {
                            csongLs.add(sInfn)
                        }
                    }
                    AppContext.instance?.updateTmpls(csongLs)
                    val bdn = Bundle()
                    bdn.putSerializable(AppConstants.PLAYER_KEY_LS, csongLs)
                    val ittn = Intent(AppConstants.PLAYER_CTRL_ACTION_INDX)
                    ittn.putExtras(bdn)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(ittn)
                    sldMenua!!.cancle()
                    Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show()
                }
                R.id.frgm_hm_lsitm_ckBtn -> {
                   // Log.d(TAG,"$pos <---------------------multck onlclicked---------------------------------")
                    if(ckdLs?.indexOf(pos)?:-1>-1) {
                        ckdLs?.remove(pos)
                    }else{
                        ckdLs?.add(pos)
                    }
                    this@SongLsAdapter.notifyDataSetChanged()
                }
                else -> {
                }
            }


        }
    }
    /**
     * views area start
     */
    fun initChsFavMenu(sls: ArrayList<SongInfo>?){
        val mAct = context as MainActivity
        val cttVw = FrgmMenuLay().chsFav(context)
        val parentView = mAct?.findViewById(R.id.main_vwctn_frmly) as ViewGroup
        sldMenub = SldMenu.create(mAct, cttVw, parentView)
        sldMenub?.setOnStateChangeListener(object:SldMenu.OnStateChangeListener{
            override fun onStateChange(state: Int) {
                if (state == SldMenu.MENU_STATE_GONE_START) {
                    mAct?.popBackLsnStack(BK_KEY_B, this@SongLsAdapter)
                } else if (state == SldMenu.MENU_STATE_SHOW) {
                    mAct?.push2BackLsnStack(BackLsnHolder(BK_KEY_B, this@SongLsAdapter))
                } else if (state == SldMenu.MENU_STATE_GONE) {
                    sldMenub = null
                }
            }
        })
        sldMenub?.show(0)
        sldMenua?.cancle()

        val newFavLsBtn = cttVw.findViewById(R.id.frgm_hm_mn_chsfav_new) as LinearLayout
        newFavLsBtn.setOnClickListener(MoreButtonListener(morBtnPosition))
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
        val chsLs = dbUtil.queryFavLs()
        dbUtil.close()
        val lsVw = cttVw.findViewById(R.id.frgm_hm_mn_chsfav_ls) as ListView
        lsVw.adapter = MenuChsLsAdapter(context, chsLs)
        lsVw.onItemClickListener = FavChsItemLsn(chsLs,sls)
    }


    /**
     * data area start
     */
    fun insert2NewFavInfo(tStr: String, stStr: String,sls: ArrayList<SongInfo>?) {
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
        val ftbnm = dbUtil.creatFavLsTable()

        val cv = HashMap<String, String>()
        cv.put("fnm", utilSqlNull(tStr))
        cv.put("fsubnm",utilSqlNull(stStr))
        cv.put("ftbnm", utilSqlNull(ftbnm))
        cv.put("picnm", utilSqlNull("test_picName"))
        cv.put("picurl",utilSqlNull("test_picUrl"))
        cv.put("indx", "ifnull((select max(indx)+1 from " + AppConstants.DB_TBNAME_FAVINFO + "),1)")
        dbUtil.insertUseMap(AppConstants.DB_TBNAME_FAVINFO, cv)
        dbUtil.close()
        insertLs2Fv(ftbnm,sls)
    }

    fun insertLs2Fv(ftbnm: String?, sls: ArrayList<SongInfo>?) {
        ftbnm?:return
        sls?:return
        var cv: MutableMap<String, String>
        val dbUtila = DbUtil()
        dbUtila.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
        for(sinfo in sls) {
            cv = HashMap<String, String>()
            cv.put("snm", utilSqlNull(sinfo.name))
            cv.put("sart", utilSqlNull(sinfo.artist))
            cv.put("salb", utilSqlNull(sinfo.album))
            cv.put("lrcnm", utilSqlNull(sinfo.lrcNm))
            cv.put("lrcurl",utilSqlNull(sinfo.lrcUrl))
            cv.put("picnm",utilSqlNull(sinfo.picNm))
            cv.put("picurl",utilSqlNull(sinfo.picUrl))
            cv.put("datanm",utilSqlNull(sinfo.data))
            cv.put("dataurl",utilSqlNull(sinfo.dataUrl))
            cv.put("oid", sinfo.oid.toString() + "")
            cv.put("sdur", sinfo.duration.toString() + "")
            cv.put("ssz", sinfo.size.toString() + "")
            cv.put("sindx", "ifnull((select max(sindx)+1 from $ftbnm),1)")
            dbUtila.insertUseMap(ftbnm, cv)
        }
        dbUtila.close()
    }


    fun deleteFavLsItem(tbnm: String?,ids: Array<Int>?){
        tbnm?:return
        ids?:return
        val idsk = StringBuilder()
        val idsV = ArrayList<String>()
        for(dinx in ids.indices){
            val did = ids[dinx]
            if(dinx>0)idsk.append(",")
            idsk.append("?")
            idsV.add(did.toString())
        }
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
        dbUtil.delete(tbnm, "sid in ($idsk)", idsV.toTypedArray())
        dbUtil.close()
    }

    fun truncatFavLsTable(tbnm: String?){
        tbnm?:return
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, context, null)
        dbUtil.delete(tbnm, null, null)
        dbUtil.close()
    }

    internal inner class ThisViewHd {
       var tit: TextView?=null
        var art: TextView?=null
        var mor: ImageButton?=null
        var ck: Button?=null
    }

    private inner class FavChsItemLsn internal
        constructor(
            private val favInfos: List<SongGrpInfo>,
            private val sls: ArrayList<SongInfo>?
    ) : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val tbName = favInfos[position].tbName
            //Log.w(TAG,">>>>>>>>>>>>>>>"+favlsTbName+",  "+sinfo.getName());
            insertLs2Fv(tbName,sls)
            sldMenub?.cancle()
            Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackKeyUp(keyCode: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            BK_KEY_A -> sldMenua?.cancle()
            BK_KEY_B -> sldMenub?.cancle()
            BK_KEY_C -> sldMenuc?.cancle()
        }
        return true
    }

    companion object {
        private val TAG = "SongLsAdapter"
        private val BK_KEY_A = 1
        private val BK_KEY_B = 2
        private val BK_KEY_C = 3
    }

}
