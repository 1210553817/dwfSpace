package com.fxqyem.adp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*

import com.fxqyem.R
import com.fxqyem.act.MainActivity
import com.fxqyem.bean.*
import com.fxqyem.vw.SldMenu
import com.fxqyem.lay.HomeLay
import com.fxqyem.utils.*
import com.fxqyem.vw.utilSqlNull
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textResource

import java.util.ArrayList


class HomeExpdLsAdapter(private val hdAct: Activity) : BaseExpandableListAdapter(), OnBackListener {
    private val mInflater: LayoutInflater
    private var grpVwhd: GrpViewHd? = null
    private var chdVwhd: ChdViewHd? = null
    var grpLs: List<SongGrpInfo>? = ArrayList()
    var chdLs: List<List<SongGrpInfo>>? = ArrayList()
    private var sldMenua: SldMenu? = null
    private var sldMenub: SldMenu? = null
    private var sldMenuc: SldMenu? = null

    init {
        mInflater = hdAct.layoutInflater
    }

    override fun getGroupCount(): Int {
        if (grpLs == null) return 0
        return grpLs?.size?:0
    }

    override fun getChildrenCount(grpPos: Int): Int {
        if (chdLs == null) return 0
        return chdLs?.get(grpPos)?.size?:0
    }

    override fun getGroup(grpPos: Int): Any {
        return grpLs!!.get(grpPos)
    }

    override fun getChild(grpPos: Int, chdPos: Int): Any {
        return chdLs!![grpPos][chdPos]
    }

    override fun getGroupId(grpPos: Int): Long {
        return grpPos.toLong()
    }

    override fun getChildId(grpPos: Int, chdPos: Int): Long {
        return (grpPos * 1000 + chdPos).toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(grpPos: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mBtn: ImageButton?
        if (convertView == null) {
            convertView = inflateGrpVw()
        }
        grpVwhd = convertView.tag as GrpViewHd
        mTextTitle = grpVwhd?.tit
        mBtn = grpVwhd?.mor

        mTextTitle?.text = grpLs!![grpPos].name

        convertView.tag = grpVwhd
        mBtn?.setOnClickListener(BtnOnClickLsn(grpPos,-1))
        return convertView
    }

    private fun inflateGrpVw(): View {
        val gView: View
        val grpVwhd: GrpViewHd
        val mTextTitle: TextView
        val mBtn: ImageButton
        gView = HomeLay().createGrpView(hdAct)
        mTextTitle = gView.findViewById(R.id.home_ex_grp_titTxt) as TextView
        mBtn = gView.findViewById(R.id.home_ex_grp_moreBtn) as ImageButton
        grpVwhd = GrpViewHd()
        grpVwhd.tit = mTextTitle
        grpVwhd.mor = mBtn
        gView.tag = grpVwhd
        return gView
    }

    override fun getChildView(grpPos: Int, chdPos: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val mTextTitle: TextView?
        val mTextArtist: TextView?
        val mBtn: ImageButton?
        if (convertView == null) {
            convertView = inflateChdVw()
        }
        chdVwhd = convertView.tag as ChdViewHd
        mTextTitle = chdVwhd!!.tit
        mTextArtist = chdVwhd!!.art
        mBtn = chdVwhd!!.mor

        mTextTitle?.text = (chdPos + 1).toString() + ". " + chdLs!![grpPos][chdPos].name
        mTextArtist?.text = chdLs!![grpPos][chdPos].subname
        mBtn?.setOnClickListener(BtnOnClickLsn(grpPos, chdPos))
        convertView.tag = chdVwhd
        return convertView
    }

    private fun inflateChdVw(): View {
        val cView: View
        val chdVwhd: ChdViewHd
        val mTextTitle: TextView
        val mTextArtist: TextView
        val mBtn: ImageButton
        cView = HomeLay().createChdView(hdAct)
        mTextTitle = cView.findViewById(R.id.home_ex_chd_titTxt) as TextView
        mTextArtist = cView.findViewById(R.id.home_ex_chd_subtitTxt) as TextView
        mBtn = cView.findViewById(R.id.home_ex_chd_moreBtn) as ImageButton
        chdVwhd = ChdViewHd()
        chdVwhd.tit = mTextTitle
        chdVwhd.art = mTextArtist
        chdVwhd.mor = mBtn
        cView.tag = chdVwhd
        return cView
    }

    private inner class GrpViewHd {
        var tit: TextView? = null
        var mor: ImageButton? = null
    }

    private inner class ChdViewHd {
        var tit: TextView? = null
        var art: TextView? = null
        var mor: ImageButton? = null
    }

    private inner class BtnOnClickLsn internal constructor(private val gpos: Int, private val cpos: Int) : View.OnClickListener {

        override fun onClick(v: View) {
            val vid = v.id
            val cttVw: View?
            val parentView: ViewGroup
            when (vid) {
                R.id.home_ex_chd_moreBtn -> {
                    cttVw = HomeLay().createFavOptMenu(hdAct)
                    parentView = hdAct.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenua = SldMenu.create(hdAct, cttVw, parentView)
                    sldMenua?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            val mAct = hdAct as MainActivity
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct.popBackLsnStack(BK_KEY_FAVOP, this@HomeExpdLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct.push2BackLsnStack(BackLsnHolder(BK_KEY_FAVOP, this@HomeExpdLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenua = null
                            }
                        }
                    })
                    sldMenua?.show(0)
                    val edtBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_edtBtn) as LinearLayout
                    val rmBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_rmBtn) as LinearLayout
                    val infoBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_infoBtn) as LinearLayout
                    edtBtn.setOnClickListener(BtnOnClickLsn(gpos, cpos))
                    rmBtn.setOnClickListener(BtnOnClickLsn(gpos, cpos))
                    infoBtn.setOnClickListener(BtnOnClickLsn(gpos, cpos))
                }
                R.id.hm_fav_opt_menu_edtBtn -> {
                    cttVw = HomeLay().createFavOptEdit(hdAct)
                    parentView = hdAct.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenub = SldMenu.create(hdAct, cttVw, parentView)
                    sldMenub?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            val mAct = hdAct as MainActivity
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct.popBackLsnStack(BK_KEY_FAVOP_EDIT, this@HomeExpdLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct.push2BackLsnStack(BackLsnHolder(BK_KEY_FAVOP_EDIT, this@HomeExpdLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenub = null
                            }
                        }
                    })
                    sldMenua?.cancle()
                    sldMenub?.show(1)
                    val sgInfoo = chdLs!![gpos][cpos]
                    AndUtil.initStatus(hdAct, cttVw)
                    val titTxto = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    titTxto.setText(sgInfoo.name)
                    val stitTxto = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_subtitEtxt) as EditText
                    stitTxto.setText(sgInfoo.subname)
                    val crenewOkBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_edtOkBtn) as Button
                    crenewOkBtn.textResource=R.string.hm_fav_mn_favedtok
                    crenewOkBtn.setOnClickListener(BtnOnClickLsn(gpos, cpos))

                    titTxto.requestFocus()
                    val imm = titTxto.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
                }
                R.id.hm_fav_opt_menu_edit_edtOkBtn -> {
                    cttVw = sldMenub!!.contentView
                    val titTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    val stitTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_subtitEtxt) as EditText
                    val tStr = titTxt.text.toString()
                    val stStr = stitTxt.text.toString()
                    val sgInfoa = chdLs!![gpos][cpos]
                    val fida = sgInfoa.id
                    updateFavInfo(tStr, stStr, fida)
                    sldMenub!!.cancle()
                    val macti = hdAct as MainActivity
                    macti.initHmFavLs()
                    Toast.makeText(hdAct, "修改成功！", Toast.LENGTH_SHORT).show()
                }
                R.id.hm_fav_opt_menu_rmBtn -> {
                    val sgInfo = chdLs!![gpos][cpos]
                    val fid = sgInfo.id
                    val ftbnm = sgInfo.tbName
                    val dbUtil = DbUtil()
                    dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, hdAct, null)
                    dbUtil.doForSql("DROP TABLE IF EXISTS " + ftbnm)
                    dbUtil.delete(AppConstants.DB_TBNAME_FAVINFO, "fid=?", arrayOf("" + fid))
                    dbUtil.close()
                    val acti = hdAct as MainActivity
                    acti.initHmFavLs()
                    sldMenua!!.cancle()
                    Toast.makeText(hdAct, "移除成功！", Toast.LENGTH_SHORT).show()
                }
                R.id.hm_fav_opt_menu_infoBtn -> {
                    cttVw = HomeLay().createFavOptInfo(hdAct)
                    parentView = hdAct.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenub = SldMenu.create(hdAct, cttVw, parentView)
                    sldMenub?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            val mAct = hdAct as MainActivity
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct.popBackLsnStack(BK_KEY_FAVOP_EDIT, this@HomeExpdLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct.push2BackLsnStack(BackLsnHolder(BK_KEY_FAVOP_EDIT, this@HomeExpdLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenub = null
                            }
                        }
                    })
                    sldMenua?.cancle()
                    sldMenub?.show(0)
                    val sgInfoi = chdLs!![gpos][cpos]
                    val titTxtvw = cttVw.findViewById(R.id.hm_fav_opt_menu_info_titShtxt) as TextView
                    titTxtvw.text = sgInfoi.name
                    val stitTxtvw = cttVw.findViewById(R.id.hm_fav_opt_menu_info_subTitShtxt) as TextView
                    stitTxtvw.text = sgInfoi.subname
                }
                R.id.home_ex_grp_moreBtn -> {
                    val mAct = hdAct as MainActivity
                    cttVw = HomeLay().createFavOptEdit(hdAct)
                    parentView = hdAct?.findViewById(R.id.main_vwctn_frmly) as ViewGroup
                    sldMenuc = SldMenu.create(mAct, cttVw, parentView)
                    sldMenuc?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                mAct?.popBackLsnStack(BK_KEY_FAVOP_CREFAV, this@HomeExpdLsAdapter)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                mAct?.push2BackLsnStack(BackLsnHolder(BK_KEY_FAVOP_CREFAV, this@HomeExpdLsAdapter))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                sldMenuc = null
                            }
                        }
                    })
                    sldMenuc?.show(1)
                    val titTxtew = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    AndUtil.initStatus(mAct, cttVw)
                    val crenewOkBtn = cttVw.findViewById(R.id.hm_fav_opt_menu_edit_edtOkBtn) as Button
                    crenewOkBtn.textResource=R.string.hm_fav_mn_favsv
                    crenewOkBtn.id = R.id.hm_song_opt_menu_crefav_creOkBtn
                    crenewOkBtn.setOnClickListener(BtnOnClickLsn(gpos, cpos))

                    titTxtew.requestFocus()
                    val imm = titTxtew.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)

                }
                R.id.hm_song_opt_menu_crefav_creOkBtn -> {
                    val mAct = hdAct as MainActivity
                    cttVw = sldMenuc?.contentView?:return
                    val titTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_titEtxt) as EditText
                    val stitTxt = cttVw?.findViewById(R.id.hm_fav_opt_menu_edit_subtitEtxt) as EditText
                    val tStr = titTxt?.text.toString()
                    val stStr = stitTxt?.text.toString()

                    val dbUtil = DbUtil()
                    dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, hdAct, null)

                    val ftbnm =dbUtil.creatFavLsTable()

                    val cv = HashMap<String, String>()
                    cv.put("fnm", utilSqlNull(tStr))
                    cv.put("fsubnm", utilSqlNull(stStr))
                    cv.put("ftbnm", utilSqlNull(ftbnm))
                    cv.put("picnm", utilSqlNull("test_picName"))
                    cv.put("picurl", utilSqlNull("test_picUrl"))
                    cv.put("indx", "ifnull((select max(indx)+1 from " + AppConstants.DB_TBNAME_FAVINFO + "),1)")
                    dbUtil.insertUseMap(AppConstants.DB_TBNAME_FAVINFO, cv)

                    dbUtil.close()
                    mAct.initHmFavLs()
                    sldMenuc?.cancle()
                    Toast.makeText(hdAct, "添加成功！", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun updateFavInfo(tStr: String, stStr: String, fid: Int) {
        val cv = ContentValues()
        cv.put("fnm", tStr)
        cv.put("fsubnm", stStr)
        //        cv.put("picnm", "");
        //        cv.put("picurl", "");
        val dbUtil = DbUtil()
        dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, hdAct, null)
        dbUtil.update(AppConstants.DB_TBNAME_FAVINFO, cv, "fid=?", arrayOf(fid.toString() + ""))
        dbUtil.close()
    }

    override fun onBackKeyUp(keyCode: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            BK_KEY_FAVOP -> sldMenua?.cancle()
            BK_KEY_FAVOP_EDIT -> sldMenub?.cancle()
            BK_KEY_FAVOP_CREFAV -> sldMenuc?.cancle()
        }
        return true
    }

    companion object {
        private val BK_KEY_FAVOP = 1
        private val BK_KEY_FAVOP_EDIT = 2
        private val BK_KEY_FAVOP_CREFAV = 3
    }
}
