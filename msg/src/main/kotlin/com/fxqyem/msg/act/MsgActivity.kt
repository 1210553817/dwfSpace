package com.fxqyem.msg.act

import android.Manifest
import android.content.*
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.adp.MemLsAdapter
import com.fxqyem.msg.adp.SetLsAdapter
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ben.BackLsnHolder
import com.fxqyem.msg.ben.OnBackListener
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgLay
import com.fxqyem.msg.lay.MsgPagerLay
import com.fxqyem.msg.lay.SettingLay
import com.fxqyem.msg.ser.MsgService
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.utl.PermissionUtils
import com.fxqyem.msg.utl.SDCardUtils
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*
import java.io.File
import java.util.Stack

/**
 * Created by Dwf on 2017-12-7
 */
class MsgActivity : MsgBaseActivity() , OnBackListener {
    var msgHdLy: android.widget.RelativeLayout? = null
    /*btm*/
    var btmLy: android.widget.RelativeLayout? = null
    var btmMemImg: android.widget.ImageView? = null
    var btmMemTxt: android.widget.TextView? = null
    var btmSetImg: android.widget.ImageView? = null
    var btmSetTxt: android.widget.TextView? = null
    /*cen*/
    var cenLy: android.widget.FrameLayout? = null
    var cenhdLy: android.widget.RelativeLayout? = null
    var cenfrmLy: android.widget.FrameLayout? = null
    var cenViewPager: ViewPager? = null
    //pager
    private var memVw: View? = null
    private var setVw: View? = null
    //mem
    var memListVw: ListView? = null
    //setting
    var selfSldMn: SldMenu? = null

    /*ctn*/
    var oneCtnFrmLy: android.widget.FrameLayout? = null

    /*send lay*/
    private var msgLsFrgm: MsgSendFrgm? = null

    /*service*/
    /*datas*/
//    private var msgHandler: MsgHandler? = null
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mReceiver: BroadcastReceiver? = null

    /*params*/
    private var thisState: Int = 0

    //Keys
    private var backLsnStack: Stack<BackLsnHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MsgLay().setContentView(this)
        //req permissions
        requestPermissions()
        //keys
        backLsnStack = Stack()
        //views
        initViews()
        initFragments()
        //init service
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        mReceiver = MsgBroadcastReceiver()
        startService(Intent(this, MsgService::class.java))
    }

    private fun initFragments(){
        //fragMents
        fragmentManager.addOnBackStackChangedListener {
            val cnum = cenfrmLy?.childCount?:0
            if (cnum == 0) {
                cenfrmLy?.visibility = View.GONE
                cenfrmLy?.setWillNotDraw(true)
                cenLy?.visibility = View.VISIBLE
                cenLy?.setWillNotDraw(false)
                btmLy?.visibility = View.VISIBLE
                btmLy?.setWillNotDraw(false)
            } else if (cnum == 1) {
                cenfrmLy?.visibility = View.VISIBLE
                cenfrmLy?.setWillNotDraw(false)
                cenLy?.visibility = View.VISIBLE
                cenLy?.setWillNotDraw(false)
                btmLy?.visibility = View.VISIBLE
                btmLy?.setWillNotDraw(false)
            }
            if (cnum > 0) {
                for (i in 0 until cnum) {
                    if (i == cnum - 1) {
                        cenfrmLy?.getChildAt(i)?.visibility = View.VISIBLE
                        cenfrmLy?.getChildAt(i)?.setWillNotDraw(false)
                    } else {
                        cenfrmLy?.getChildAt(i)?.visibility = View.GONE
                        cenfrmLy?.getChildAt(i)?.setWillNotDraw(true)
                    }
                }
            }
        }
        val bkStkNum = fragmentManager.backStackEntryCount
        for (k in 0 until bkStkNum) {
            fragmentManager.popBackStackImmediate()
        }
    }
    
    private fun initViews() {
        //btm
        val btmMemBtnPly =  btmMemImg?.parent as LinearLayout
        val btmSetBtnPly =  btmSetImg?.parent as LinearLayout
        btmMemBtnPly.onClick {
            chooseBtmItem(0)
            cenViewPager?.currentItem=0
        }
        btmSetBtnPly.onClick {
            chooseBtmItem(2)
            cenViewPager?.currentItem=2
        }
        //pager
        initMsgPager()
    }
    private fun chooseBtmItem(indx: Int){
        val btnArr = arrayOf(btmMemImg,btmSetImg)
        val txtArr = arrayOf(btmMemTxt,btmSetTxt)
        val iconArr = arrayOf(R.mipmap.red_thm_29,R.mipmap.red_thm_15)
        for(i in 0 until btnArr.size){
            val btn = btnArr[i]
            val txt = txtArr[i]
            val icon = iconArr[i]
            if(i==indx){
                btn?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(this@MsgActivity, icon, appColorArrayLightGreen)
                txt?.textColor = COLOR_LIGHTGREEN
            }else{
                btn?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(this@MsgActivity, icon, appColorArrayLightGrey)
                txt?.textColor = COLOR_LIGHTGREY
            }
        }
    }

    private fun initMsgPager(){
        val msgPagerLay = MsgPagerLay()
        val memVw = msgPagerLay.createMemVw(this)
        val setVw = SettingLay().createSetVw(this)

        this.memVw= memVw
        this.setVw= setVw
        val pagerVws = listOf(memVw,setVw)
        val mAdapter = MyPagerAdapter(pagerVws, listOf(
                getResString(this,R.string.memls),
                getResString(this,R.string.setting)))
        cenViewPager?.adapter = mAdapter
        cenViewPager?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageSelected(pos: Int) {
                chooseBtmItem(pos)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        initPagerMemViews(memVw)
        initPagerSetViews(setVw)


    }
    /*mem pager*/
    private fun initPagerMemViews(memVw: View){
        memListVw = memVw.find(R.id.msg_lay_cen_mempg_lsvw)
        val refreshBtn = memVw.find<ImageButton>(R.id.msg_lay_cen_mempg_refresh_btn)
        val memAdapter = MemLsAdapter(this,null)
        memListVw?.adapter = memAdapter
        memListVw?.onItemClickListener = AdapterView.OnItemClickListener { pvw, _, position, _ ->
            val lsVw = pvw as ListView
            val adp = lsVw.adapter as MemLsAdapter?
            val itm = adp?.list?.get(position)
            /*fragment*/
            val bun = Bundle()
            //bun.putInt("tp", 0)
            bun.putSerializable("ITEM",itm)
            msgLsFrgm = MsgSendFrgm()
            msgLsFrgm?.arguments = bun
            val transaction = fragmentManager.beginTransaction()
            //transaction.setCustomAnimations(R.animator.right_sld_in,R.animator.right_sld_out)
            transaction.add(R.id.msg_lay_core_cenfrm_ly, msgLsFrgm)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        refreshBtn.onClick {
            reloadMemList()
        }

    }

    private fun reloadMemList(){
        val memAdp = memListVw?.adapter as MemLsAdapter?
        memAdp?.list?.clear()
        val itt = Intent(AppConstants.ACTION_SER_LOAD_MEM_ITEMS)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }
    /*setting pager*/
    private fun initPagerSetViews(setVw: View){
        val setListVw = setVw.find<ListView>(R.id.setting_pglay_lsvw)
        val hiconBtn = setVw.find<ImageButton>(R.id.setting_pglay_hdicon_btn)
        val setAdp = SetLsAdapter(this, arrayListOf(
                MsgEnt(getResString(this,R.string.setting_pglay_svpath),"",R.mipmap.folder_vtcl),
                MsgEnt(getResString(this,R.string.setting_pglay_slfdo),"",R.mipmap.cus),
                MsgEnt(getResString(this,R.string.about),"",R.mipmap.about),
                MsgEnt(getResString(this,R.string.exit),"",R.mipmap.exit)
        ))
        setListVw.adapter = setAdp
        setListVw.onItemClickListener = AdapterView.OnItemClickListener { pvw, _, position, _ ->
            val lsVw = pvw as ListView
            val adp = lsVw.adapter as SetLsAdapter?
            val itm = adp?.list?.get(position)
            when(itm?.cmd){
                R.mipmap.folder_vtcl -> {
                    val pathSelector = FileSelector(this@MsgActivity,FileSelector.TYPE_DIR or FileSelector.TYPE_NO_STARTP)
                    val sdpth = SDCardUtils.sdCardPath
                    pathSelector.open(sdpth, null)
                    pathSelector.setOnSelectOkListener(object: FileSelector.OnSelectOkListener{
                        override fun onSelectOk(fpath: String?,file: File?) {
                            if(fpath!=null) {
                                AppContext.instance?.setRcvPath(fpath)
                            }
                        }
                    })
                }
                R.mipmap.cus -> {
                    val hdvw = SettingLay().createSelfVw(this@MsgActivity)
                    val unm = hdvw.find<EditText>(R.id.setting_pglay_slfdo_unm)
                    val tit = hdvw.find<EditText>(R.id.setting_pglay_slfdo_tit)
                    val sub = hdvw.find<EditText>(R.id.setting_pglay_slfdo_sub)
                    val cbtn = hdvw.find<Button>(R.id.setting_pglay_slfdo_clbtn)
                    val obtn = hdvw.find<Button>(R.id.setting_pglay_slfdo_okbtn)
                    unm.setText(AppContext.instance?.uname,TextView.BufferType.NORMAL)
                    tit.setText(AppContext.instance?.utit,TextView.BufferType.NORMAL)
                    sub.setText(AppContext.instance?.usub,TextView.BufferType.NORMAL)
                    cbtn.onClick {
                        selfSldMn?.cancle()
                    }
                    obtn.onClick {
                        AppContext.instance?.setSelfInfo(unm.text.toString(),tit.text.toString(),sub.text.toString())
                        selfSldMn?.cancle()
                    }

                    selfSldMn = SldMenu.create(this, hdvw, oneCtnFrmLy)
                    selfSldMn?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            when(state){
                                SldMenu.MENU_STATE_GONE_START -> {
                                    popBackLsnStack(BK_KEY_SELF_MN, this@MsgActivity)
                                }
                                SldMenu.MENU_STATE_SHOW -> {
                                    push2BackLsnStack(BackLsnHolder(BK_KEY_SELF_MN, this@MsgActivity))
                                }
                                SldMenu.MENU_STATE_GONE -> {
                                    selfSldMn = null
                                }
                            }
                        }
                    })
                    selfSldMn?.show(SldMenu.SHOW_TYPE_TOP)

                }
                R.mipmap.about -> {
                    startActivity(Intent(this@MsgActivity,AboutActivity::class.java))
                }
                R.mipmap.exit -> {
                    android.os.Process.killProcess(android.os.Process.myPid())
                }

            }


        }
        hiconBtn.onClick {

        }

    }

    internal inner class MyPagerAdapter(private val mViewList: List<View>,private val mViewTitles: List<String>) : PagerAdapter() {

        override fun getCount() = mViewList.size

        override fun isViewFromObject(view: View, oobject: Any) = view === oobject


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mViewList[position])//添加页卡
            return mViewList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mViewList[position])//删除页卡
        }

        override fun getPageTitle(position: Int) = mViewTitles[position]

    }

//    private inner class BtnClickLsn: View.OnClickListener {
//        override fun onClick(v: View) {
//            when (v.id) {
//                R.id.msg_lay_core_btm_mem_img -> {
//
//                }
//
//                else -> return
//            }
//
//        }
//    }

    override fun onBackKeyUp(reqValue: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            BK_KEY_SELF_MN -> selfSldMn?.cancle()
        }
        return true
    }

    override fun push2BackLsnStack(obkLsnHd: BackLsnHolder) {
        backLsnStack?.push(obkLsnHd)
    }

    override fun popBackLsnStack(reqCod: Int, bkLsn: OnBackListener):BackLsnHolder?{
        val bol = backLsnStack?.isEmpty()?:true
        if (!bol) {
            val bkhd = backLsnStack?.peek()
            if (bkhd != null && bkhd.onBackListener === bkLsn && bkhd.requestCode == reqCod)
                return backLsnStack?.pop()
        }
        return null
    }
    /*Handler*/
//    inner class MsgHandler: Handler(){
//        override fun handleMessage(msg: Message) {
//            val bun = msg.data
//            when (msg.what) {
//                WHAT_MEMLS_LOAD -> {
//
//                }
//            }
//        }
//    }
    private inner class MsgBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bun: Bundle? = intent.extras
            when(intent.action){
                AppConstants.ACTION_ALERT_MSG -> {
                    val msg = bun?.getString("MSG")
                    Toast.makeText(this@MsgActivity,msg,Toast.LENGTH_SHORT).show()
                }
                AppConstants.ACTION_LOAD_MEM_ITEMS -> {
                    val ent = bun?.get("MSGENT") as MsgEnt?
                    if(ent!=null) {
                        val memAdp = memListVw?.adapter as MemLsAdapter?
                        var list = memAdp?.list
                        if (null == list) list = ArrayList()
                        if (!list.contains(ent)) {
                            list.add(ent)
                        }
                        memAdp?.list = list
                        memAdp?.notifyDataSetChanged()
                    }
                }
                AppConstants.ACTION_RECEIVE_MSG -> {
                    val msg = bun?.getSerializable("MSG") as MsgEnt?
                    if(msg!=null) {
                        doReceivedMsg(msg)
                    }
                }
                AppConstants.ACTION_FILE_PRGRS_MSG -> {
                    val id = bun?.getLong("ID")
                    val ip = bun?.getString("IP")
                    val per = bun?.getLong("PER")
                    doReceivedFilePrgrs(id,ip,per)
                }

            }
        }
    }

    private fun doReceivedMsg(msg: MsgEnt){
        val ip = msg.ip?:return
        val bkStkNum = fragmentManager.backStackEntryCount
        if (bkStkNum > 0 && ip == msgLsFrgm?.citem?.ip) {
            msgLsFrgm?.loadMsgItem(msg)
        }else{
            val adp = memListVw?.adapter as MemLsAdapter?
            adp?.notifyDataSetChanged()
        }
    }
    private fun doReceivedFilePrgrs(id: Long?,ip: String?,per: Long?){
        val bkStkNum = fragmentManager.backStackEntryCount
        if (bkStkNum > 0 && ip == msgLsFrgm?.citem?.ip) {
            msgLsFrgm?.loadFilePrgrs(id,ip,per)
        }

    }

    private fun requestPermissions(): Boolean{
        if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, SDCARD_REQUEST_CODE)) {
                Toast.makeText(this, getResString(this,R.string.no_sdcard_permission), Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult requestCode: " + requestCode)
        if (PermissionUtils.verifyPermissions(grantResults)) {//权限申请成功后根据请求码更新相关界面
            when (requestCode) {
                SDCARD_REQUEST_CODE ->{
                    Log.w(TAG, "onRequestPermissionsResult  SDCARD_REQUEST return success")
                }
            }
        } else {
            when(requestCode) {
                SDCARD_REQUEST_CODE ->{
                    Toast.makeText(this, getResString(this,R.string.no_sdcard_permission), Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val bol = backLsnStack?.isEmpty()?:true
            if (!bol) {
                val bkhd = backLsnStack?.pop()
                val obkLsn = bkhd?.onBackListener
                return obkLsn?.onBackKeyUp(bkhd.requestValue, event, bkhd.requestCode)?:false
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {

            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        thisState = AppConstants.VW_STATE_SHOW

        val filter = IntentFilter()
        filter.addAction(AppConstants.ACTION_LOAD_MEM_ITEMS)
        filter.addAction(AppConstants.ACTION_RECEIVE_MSG)
        filter.addAction(AppConstants.ACTION_FILE_PRGRS_MSG)
        filter.addAction(AppConstants.ACTION_ALERT_MSG)
        mLocalBroadcastManager?.registerReceiver(mReceiver, filter)



        val itt = Intent(AppConstants.ACTION_SER_ACT_RESUME)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }
    override fun onStop() {
        super.onStop()
        thisState = AppConstants.VW_STATE_GONE
        mLocalBroadcastManager?.unregisterReceiver(mReceiver)

        val itt = Intent(AppConstants.ACTION_SER_ACT_STOP)
        mLocalBroadcastManager?.sendBroadcast(itt)

    }

    companion object {
        private val TAG = "MsgActivity"
        /*back key*/
        private val BK_KEY_SELF_MN = 1
        /*What*/
        //private val WHAT_MEMLS_LOAD = 1
        /*permisson req*/
        private val SDCARD_REQUEST_CODE = 1
    }
}