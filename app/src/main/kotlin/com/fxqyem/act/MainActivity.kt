package com.fxqyem.act

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.fxqyem.R
import com.fxqyem.adp.HomeExpdLsAdapter
import com.fxqyem.adp.NetLsAdapter
import com.fxqyem.adp.TmpLsAdapter
import com.fxqyem.bean.*
import com.fxqyem.lay.HomeLay
import com.fxqyem.lay.MainLay
import com.fxqyem.lay.TmplsLay
import com.fxqyem.ser.MainService
import com.fxqyem.utils.*
import com.fxqyem.vw.*
import org.jetbrains.anko.*
import java.util.*

class MainActivity : android.app.Activity(), OnBackListener {
    //Views
    var mTabLayout: TabLayout? = null
    var mViewPager: ViewPager? = null
    var hdLy: android.widget.RelativeLayout? = null
    var cenLy: android.widget.FrameLayout? = null
    var cenOriLy: android.widget.RelativeLayout? = null
    var cenHdLy: android.widget.FrameLayout? = null
    var btmLy: android.widget.RelativeLayout? = null
    var frmLy: android.widget.FrameLayout? = null
    var frmCtnLy: android.widget.RelativeLayout? = null
    var frmCtnCvLy: android.widget.RelativeLayout? = null
    var vwCtnFrmLy: android.widget.FrameLayout? = null
    var topCtnFrmLy: android.widget.FrameLayout? = null
    var covLy: android.widget.LinearLayout? = null
    var sldLy: android.widget.RelativeLayout? = null
    var sldBarLy: android.widget.LinearLayout? = null
    val mTitleList = java.util.ArrayList<String>()//页卡标题集合
    val mViewList = java.util.ArrayList<View?>()//页卡视图集合
    var view1: View? = null
    var view2: View? = null//页卡视图
    //Views: btm
    var btmProgBar: ProgressBar? = null
    var btmImgVw: ImageView? =null
    var btmLsBtn: ImageButton? = null
    var btmNextBtn: ImageButton? = null
    var btmPlayBtn: ImageButton? = null
    var btmSongTitle: TextView? = null
    var btmSongArtist: TextView? = null
    //Views: frm
    var frmSeekBar: SeekBar? = null
    var frmPlchd: TextView? = null
    var frmBackBtn: ImageButton? = null
    var frmPrevBtn: ImageButton? = null
    var frmPlayBtn: ImageButton? = null
    var frmNextBtn: ImageButton? = null
    var frmLsBtn: ImageButton? = null
    var frmCurPosTime: TextView? = null
    var frmAllTime: TextView? = null
    var frmSongTitle: TextView? = null
    var frmSongArtist: TextView? = null
    var frmLrcVw: FrameLayout? = null
    var lrcMovView: LrcMovView? = null
    //Views: ctt
    var cttLocBtn: ImageButton? = null
    var cttFavLsvw: ExpandableListView? = null
    //Views: ctt
    var appSettingBtn: Button? = null
    var appOverBtn: Button? = null
    //Views: sldLy
    var bkgSettingBtnA:Button? = null
    var bkgSettingBtnB: Button?  = null
    var bkgBlurBtn: Button? =null
    var sldPlayModeBtn: Button? =null
    //Views: tmp
    private var tmpSldMenu: SldMenu? = null
    //Views: volume
    private var volumeSldMenu: SldMenu? = null

    //Views: netvw
    var netSelectorBtn: ImageButton? = null
    var netSerBtn: ImageButton? = null
    var netKeywordEdit: EditText? = null
    var netRstLs: ListView? = null
    var netLsAdapter: NetLsAdapter? = null

    var netSelectSldMenu: SldMenu? = null
    var netSelectLs: List<SongResult>?=null

    //Datas
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mReceiver: BroadcastReceiver? = null
    private var mHandler: Handler? = null
    private var tmpLsAdapter: TmpLsAdapter? = null
    //Params
    private var thisState: Int = 0
    private var isLrcMovViewAdd: Boolean = false
    private var frmLyAniRun = false//frmLy动画执行中，防止动画执行时再次执行触摸事件
    private var sldLyAniRun = false
    private var frmLyStartMv = false
    private var frmLyState = 0
    private var sldLyState = 0
    private var wHeight: Int = 0
    private var wWidth: Int = 0
    private var statusH: Int = 0
    private var frmLrcVwHeight: Int = 0
    private var frmLrcVwTchd: Boolean = false

    private var curSongIndx = -1
    //Keys
    private var backLsnStack: Stack<BackLsnHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainLay().setContentView(this)
        mHandler = MyHandler()
        initParams()
        initViews()
        //withService
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        mReceiver = PlayRevwBroadcastReceiver()
        val startIntent = Intent(this, MainService::class.java)
        startService(startIntent)
        //fragMents
        fragmentManager.addOnBackStackChangedListener {
            val cnum = cenHdLy?.childCount?:0
            if (cnum == 0) {
                cenHdLy?.visibility = View.GONE
                cenHdLy?.setWillNotDraw(true)
                cenOriLy?.visibility = View.VISIBLE
                cenOriLy?.setWillNotDraw(false)
            } else if (cnum == 1) {
                cenHdLy?.visibility = View.VISIBLE
                cenHdLy?.setWillNotDraw(false)
                cenOriLy?.visibility = View.GONE
                cenOriLy?.setWillNotDraw(true)
            }
            if (cnum > 0) {
                for (i in 0..cnum - 1) {
                    if (i == cnum - 1) {
                        cenHdLy?.getChildAt(i)?.visibility = View.VISIBLE
                        cenHdLy?.getChildAt(i)?.setWillNotDraw(false)
                    } else {
                        cenHdLy?.getChildAt(i)?.visibility = View.GONE
                        cenHdLy?.getChildAt(i)?.setWillNotDraw(true)
                    }
                }
            }
        }
        val bkStkNum = fragmentManager.backStackEntryCount
        for (k in 0..bkStkNum - 1) {
            fragmentManager.popBackStackImmediate()
        }

        //keys
        backLsnStack = Stack<BackLsnHolder>()
    }

    private fun initViews() {
        frmPlayBtn?.setBackgroundResource(R.drawable.main_frm_play_btn_style)
        var showH = wHeight - statusH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            showH = wHeight
        }
        val frmCtnCvLyp = frmCtnCvLy?.layoutParams as RelativeLayout.LayoutParams
        frmCtnCvLyp.height = showH
        frmCtnCvLy?.layoutParams = frmCtnCvLyp
        val frmpLyp = frmPlchd?.layoutParams as RelativeLayout.LayoutParams
        frmpLyp.topMargin = wHeight - AndUtil.dp2Px(this@MainActivity, 150f)
        frmPlchd?.layoutParams = frmpLyp
        val frmLrcVwp = frmLrcVw?.layoutParams as RelativeLayout.LayoutParams
        frmLrcVwp.height = wHeight - AndUtil.dp2Px(this@MainActivity, 200f)
        frmLrcVwHeight = frmLrcVwp.height
        frmLrcVw?.layoutParams = frmLrcVwp

        //Listener
        btmLy?.setOnTouchListener(BtmLyTouchListener())
        frmLy?.setOnTouchListener(FrmLyTouchListener())
        sldBarLy?.setOnTouchListener(SldBarLyTouchListener())
        covLy?.setOnTouchListener(CovLyTouchListener())
        sldLy?.setOnTouchListener(SldLyTouchListener())
        //--btm
        btmLsBtn?.setOnClickListener(BtnOnClickLsn())
        btmNextBtn?.setOnClickListener(BtnOnClickLsn())
        btmPlayBtn?.setOnClickListener(BtnOnClickLsn())
        //--sld
        appSettingBtn?.setOnClickListener(BtnOnClickLsn())
        appOverBtn?.setOnClickListener(BtnOnClickLsn())
        bkgSettingBtnA?.setOnClickListener(BtnOnClickLsn())
        bkgSettingBtnB?.setOnClickListener(BtnOnClickLsn())
        bkgBlurBtn?.setOnClickListener(BtnOnClickLsn())
        sldPlayModeBtn?.setOnClickListener(BtnOnClickLsn())
        appSettingBtn?.onClick {
            val intent = Intent()
            intent.setClass(this,SubActivity::class.java)
            startActivity(intent)
        }
        //--frm
        frmSeekBar?.setOnSeekBarChangeListener(FrmSeekBarChgLsn())
        frmBackBtn?.setOnClickListener(BtnOnClickLsn())
        frmPlayBtn?.setOnClickListener(BtnOnClickLsn())
        frmPrevBtn?.setOnClickListener(BtnOnClickLsn())
        frmNextBtn?.setOnClickListener(BtnOnClickLsn())
        frmLsBtn?.setOnClickListener(BtnOnClickLsn())

        //other Views
        initMainPagerViews()
        //init pictures
        initBackgroundImg()
    }
    private fun initBackgroundImg(){
        val bkgpth = SDCardUtils.sdCardPath + "/" + AppConstants.APP_BKGPIC_PATH
        var lsBkgFnm = String()
        lsBkgFnm = PrefUtil.get(this, AppConstants.PREF_KEY_APP_LS_BKG, lsBkgFnm, AppConstants.PREF_NAME_PARAMS) as String
        if (utilNotNull(lsBkgFnm)) {
            var d: Bitmap? = null
            try {
                d = BitMapUtil.getBitmapFromFile(bkgpth + "/" + lsBkgFnm)
            } catch (e: Exception) {
                Log.d(TAG, "cant find lsBkg pic file!")
            }

            if (d == null) {
                hdLy?.backgroundDrawable = getResDrawable(this,R.mipmap.bkg1)
            } else {
                hdLy?.backgroundDrawable = BitmapDrawable(null,d)
            }
        }
        var ctrlBkgFnm = String()
        ctrlBkgFnm = PrefUtil.get(this, AppConstants.PREF_KEY_APP_CTRL_BKG, ctrlBkgFnm, AppConstants.PREF_NAME_PARAMS) as String
        if (utilNotNull(ctrlBkgFnm)) {
            var d: Bitmap? = null
            try {
                d = BitMapUtil.getBitmapFromFile(bkgpth + "/" + ctrlBkgFnm)
            } catch (e: Exception) {
                Log.d(TAG, "cant find ctrlBkg pic file!")
            }

            if (d == null) {
                frmCtnLy?.backgroundDrawable = getResDrawable(this,R.mipmap.bkg1)
            } else {
                frmCtnLy?.backgroundDrawable = BitmapDrawable(null,d)
            }
        }
    }

    private fun initMainPagerViews() {
        //ViewPager
        view1 = HomeLay().createView(this)
        view2 = HomeLay().createNetView(this)
        //添加页卡视图
        mViewList.add(view1)
        mViewList.add(view2)
        //添加页卡标题
        mTitleList.add("我的音乐")
        mTitleList.add("网络音乐")

        mTabLayout?.tabMode = if (mTitleList.size > 3) TabLayout.MODE_SCROLLABLE else TabLayout.MODE_FIXED//设置tab模式
        for (i in mTitleList.indices) {
            mTabLayout?.addTab(mTabLayout?.newTab()!!.setText(mTitleList[i]))
        }

        val mAdapter = MyPagerAdapter(mViewList!! as List<View>)
        mViewPager?.adapter = mAdapter//给ViewPager设置适配器


//        mTabLayout?.setupWithViewPager(mViewPager!!)//将TabLayout和ViewPager关联起来。
//        mTabLayout?.setTabsFromPagerAdapter(mAdapter)//给Tabs设置适配器

        mTabLayout?.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(p0: TabLayout.Tab?) {
                mViewPager?.currentItem=p0?.position?:0
            }
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
        })
        mViewPager?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                mTabLayout?.getTabAt(position)?.select()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        //locHm
        cttLocBtn = view1?.findViewById(R.id.home_ex_localBtn) as ImageButton
        cttFavLsvw = view1?.findViewById(R.id.home_ex_expdls) as ExpandableListView
        //Listener
        cttLocBtn?.setOnClickListener(BtnOnClickLsn())

        //HmFav Datas
        cttFavLsvw?.setGroupIndicator(null)
        cttFavLsvw?.setAdapter(HomeExpdLsAdapter(this))
        cttFavLsvw?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val adp = parent.expandableListAdapter as HomeExpdLsAdapter
            val chdLs = adp.chdLs
            val sinfo = chdLs!![groupPosition][childPosition]
            val favnm = sinfo.name
            val favTbNm = sinfo.tbName
            val args = Bundle()
            args.putInt("tp", 2)
            args.putString("tit", favnm)
            args.putString("tbnm", favTbNm)
            val newFragment = HmFrgm()
            newFragment.arguments = args
            val transaction = fragmentManager.beginTransaction()
            transaction.add(R.id.main_cen_hd_ly, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()

            false
        }
        initHmFavLs()
        initPageNetVw(view2)

    }

    private fun initParams() {
//        val wmer = applicationContext.getSystemService(
//                Context.WINDOW_SERVICE) as WindowManager
        val disM = resources.displayMetrics
        wHeight = disM.heightPixels
        wWidth = disM.widthPixels
        statusH = getStatusBarHeight(this)
    }

    fun initHmFavLs() {
        doAsync {
            val msg = Message()
            msg.what = WHAT_GET_FAV_LS
            msg.data = hmFavLs
            if(null!=msg.data)mHandler?.sendMessage(msg)
        }
    }

    val hmFavLs: Bundle?
        get() {
            val grpls = ArrayList<SongGrpInfo>()
            grpls.add(SongGrpInfo(1, "我的歌单"))
            val chdls = ArrayList<List<SongGrpInfo>>()
            if (Build.VERSION.SDK_INT >= 23) {
                if (!PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, DBLS_REQUEST_CODE)) {
                    Toast.makeText(this, "没有‘存储空间’权限，无法访问SD卡数据库", Toast.LENGTH_SHORT).show()
                    return null
                }
            }
            val dbUtil = DbUtil()
            dbUtil.OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_DATAS, this, null)
            val chdlsa=dbUtil.queryFavLs()
            dbUtil.close()
            chdls.add(chdlsa)

            val bd = Bundle()
            bd.putSerializable("grpls", grpls)
            bd.putSerializable("chdls", chdls)

            return bd
        }


    internal inner class MyPagerAdapter(private val mViewList: List<View>) : PagerAdapter() {

        override fun getCount(): Int {
            return mViewList.size//页卡数
        }

        override fun isViewFromObject(view: View, oobject: Any): Boolean {
            return view === oobject//官方推荐写法
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mViewList[position])//添加页卡
            return mViewList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mViewList[position])//删除页卡
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mTitleList[position]//页卡标题
        }

    }

    private inner class SldBarLyTouchListener : View.OnTouchListener {
        private var nx: Float = 0.toFloat()
        private var nx1: Float = 0.toFloat()
        private var magLft = -1
        private var params: RelativeLayout.LayoutParams? = null
        private var sldLyW: Int = 0
        private var aniFlg = false

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (sldLyAniRun) {
                aniFlg = true
                return aniFlg
            }
            if (aniFlg) {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    aniFlg = false
                }
                return true
            }
            val cx = event.x
            //Log.d(ACTIVITY_TAG,"###################### nx: "+nx+",nx1: "+nx1);
            if (event.action == MotionEvent.ACTION_DOWN) {
                covLy?.visibility = View.VISIBLE
                covLy?.setWillNotDraw(false)
                sldLy?.visibility = View.VISIBLE
                sldLy?.setWillNotDraw(false)
                params = sldLy?.layoutParams as RelativeLayout.LayoutParams
                sldLyW = AndUtil.dp2Px(this@MainActivity, SLD_MENU_WIDTH.toFloat())
                magLft = -sldLyW
                params?.leftMargin = magLft
                sldLy?.layoutParams = params
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                var b = false
                if (nx >= nx1) {
                    b = true
                }
                doSldLyAnimation(magLft, if (b) 0 else -sldLyW, 200, b)
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                if (cx > sldLyW) {
                    magLft = 0
                } else {
                    magLft = cx.toInt() - sldLyW
                }
                if (params != null) {
                    params?.leftMargin = magLft
                    sldLy?.layoutParams = params
                }

            }
            nx1 = nx
            nx = cx
            return true
        }
    }

    private var addNetItemFlag=true
    private fun initPageNetVw(netvw: View?){

        netSelectorBtn = netvw?.findViewById(R.id.main_net_ser_selector) as ImageButton
        netSelectorBtn?.tag="wy"
        netSerBtn = netvw?.findViewById(R.id.main_net_ser_serbtn) as ImageButton
        netKeywordEdit = netvw?.findViewById(R.id.main_net_ser_keyword) as EditText
        netRstLs = netvw?.findViewById(R.id.main_net_ser_rstls) as ListView
        netLsAdapter = NetLsAdapter(this@MainActivity, null)
        netRstLs?.adapter = netLsAdapter
        netRstLs?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(NetUtils.isConnected(this@MainActivity)) {
                var sitm = netLsAdapter?.getList()?.get(position)
                if (addNetItemFlag) {
                    if (null != sitm) {
                        addNetItemFlag = false
                        val curSongLs: ArrayList<SongInfo>? = AppContext.instance?.tmpLs
                        val svname = sitm.songName + ".mp3"
                        var songItem = SongInfo()
                        songItem.name = if (utilNotNull(sitm.songName)) sitm.songName else "< unknown >"
                        songItem.artist = if (utilNotNull(sitm.artistName)) sitm.artistName else "< unknown >"
                        songItem.picUrl = sitm.picUrl
                        songItem.lrcUrl = sitm.lrcUrl

                        songItem.songId = sitm.songId
                        songItem.qmid = sitm.qqmid
                        songItem.type = sitm.type
                        doAsync {
                            songItem.data = MusicProvider.getPlayUrl(sitm)
                            uiThread {
                                if (songItem.data != null) {
                                    curSongLs?.add(songItem)
                                    Toast.makeText(this@MainActivity, "已添加${svname}到播放列表", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "没有找到${svname}的播放地址!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            addNetItemFlag = true
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "其它歌曲正在添加，请稍后添加...", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@MainActivity,"没有网络连接哦！",Toast.LENGTH_SHORT).show()
            }
        }
        netSelectorBtn?.onClick {
            val cttVw = HomeLay().createNetSelectMenu(this@MainActivity)
            netSelectSldMenu = SldMenu.create(this@MainActivity, cttVw, vwCtnFrmLy)
            netSelectSldMenu?.setOnStateChangeListener(object : SldMenu.OnStateChangeListener {
                override fun onStateChange(state: Int) {
                    if (state == SldMenu.MENU_STATE_GONE_START) {
                        this@MainActivity?.popBackLsnStack(BK_KEY_netSelectSldMenu, this@MainActivity)
                    } else if (state == SldMenu.MENU_STATE_SHOW) {
                        this@MainActivity?.push2BackLsnStack(BackLsnHolder(BK_KEY_netSelectSldMenu, this@MainActivity))
                    } else if (state == SldMenu.MENU_STATE_GONE) {
                        netSelectSldMenu = null
                    }
                }
            })
            netSelectSldMenu?.show(1)
            val tophd = cttVw.findViewById(R.id.main_net_ser_selector_tophd) as LinearLayout
            AndUtil.initStatus(this@MainActivity, tophd)
            val wyBtn = cttVw.findViewById(R.id.main_net_ser_selector_wy) as LinearLayout
            val xmBtn = cttVw.findViewById(R.id.main_net_ser_selector_xm) as LinearLayout
            val kgBtn = cttVw.findViewById(R.id.main_net_ser_selector_kg) as LinearLayout
            val qqBtn = cttVw.findViewById(R.id.main_net_ser_selector_qq) as LinearLayout
            val bdBtn = cttVw.findViewById(R.id.main_net_ser_selector_bd) as LinearLayout
            val mgBtn = cttVw.findViewById(R.id.main_net_ser_selector_mg) as LinearLayout
            val wsBtn = cttVw.findViewById(R.id.main_net_ser_selector_ws) as LinearLayout
            wyBtn.onClick {
                netSelectorBtn?.tag = "wy"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_wy
                netSelectSldMenu?.cancle()
            }
            xmBtn.onClick {
                netSelectorBtn?.tag = "xm"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_xm
                netSelectSldMenu?.cancle()
            }
            kgBtn.onClick {
                netSelectorBtn?.tag = "kg"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_kg
                netSelectSldMenu?.cancle()
            }
            qqBtn.onClick {
                netSelectorBtn?.tag = "qq"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_qq
                netSelectSldMenu?.cancle()
            }
            bdBtn.onClick {
                netSelectorBtn?.tag = "bd"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_bd
                netSelectSldMenu?.cancle()
            }
            mgBtn.onClick {
                netSelectorBtn?.tag = "mg"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_mg
                netSelectSldMenu?.cancle()
            }
            wsBtn.onClick {
                netSelectorBtn?.tag = "ws"
                netSelectorBtn?.backgroundResource = R.mipmap.icon_ws
                netSelectSldMenu?.cancle()
            }



        }
        var searchingNetLsFlag=true
        netSerBtn?.onClick {
            if(NetUtils.isConnected(this@MainActivity)) {
                if(searchingNetLsFlag) {
                    searchingNetLsFlag = false
                    val qtp = netSelectorBtn?.tag as String?
                    val keywd = netKeywordEdit?.text?.toString() ?: ""
                    doAsync {
                        when (qtp) {
                            "wy" -> netSelectLs = MusicProvider.getWyLs(keywd, 1, 30)
                            "xm" -> netSelectLs = MusicProvider.getXmLs(keywd, 1, 30)
                            "kg" -> netSelectLs = MusicProvider.getKgLs(keywd, 1, 30)
                            "qq" -> netSelectLs = MusicProvider.getQqLs(keywd, 1, 30)
                            "bd" -> netSelectLs = MusicProvider.getBdLs(keywd, 1, 30)
                            "mg" -> netSelectLs = MusicProvider.getMgLs(keywd, 1, 30)
                            "ws" -> netSelectLs = MusicProvider.getWsLs(keywd, 1, 30)
                        }
                        uiThread {
                            netLsAdapter?.setList(netSelectLs ?: ArrayList<SongResult>())
                            netLsAdapter?.notifyDataSetChanged()
                            searchingNetLsFlag = true
                        }
                    }
                }else{
                    Toast.makeText(this@MainActivity,"已有搜索进行中，请稍后...",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@MainActivity,"没有网络连接哦！",Toast.LENGTH_SHORT).show()
            }
        }


    }



    private inner class SldLyTouchListener : View.OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return false
        }
    }

    private inner class CovLyTouchListener : View.OnTouchListener {
        private var nx: Float = 0.toFloat()
        private var nx1: Float = 0.toFloat()
        private var rx: Float = 0.toFloat()
        private var magLft: Int = 0
        private var mvf = false
        private var params: RelativeLayout.LayoutParams? = null
        private var sldLyW: Int = 0
        private var aniFlg = false

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (sldLyAniRun) {
                aniFlg = true
                return aniFlg
            }
            if (aniFlg) {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    aniFlg = false
                }
                return true
            }
            rx = event.rawX.toInt().toFloat()
            if (event.action == MotionEvent.ACTION_DOWN) {
                mvf = false
                magLft = 0
                sldLyW = AndUtil.dp2Px(this@MainActivity, SLD_MENU_WIDTH.toFloat())
                params = sldLy?.layoutParams as RelativeLayout.LayoutParams
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                if (mvf) {
                    var b = false
                    if (nx >= nx1) {
                        b = true
                    }
                    doSldLyAnimation(magLft, if (b) 0 else -sldLyW, 250, b)
                } else {
                    doSldLyAnimation(0, -sldLyW, 200, false)
                }
            } else if (event.action == MotionEvent.ACTION_MOVE && Math.abs(rx - nx) > 5) {
                mvf = true
                magLft += (rx - nx).toInt()
                if (magLft > 0) magLft = 0
                if (params != null) {
                    params?.leftMargin = magLft
                    sldLy?.layoutParams = params
                }
            }
            nx1 = nx
            nx = rx
            return true
        }
    }

    private inner class BtmLyTouchListener : View.OnTouchListener {
        private var ny: Float = 0.toFloat()
        private var ny1: Float = 0.toFloat()
        private var magTop: Int = 0
        private var mvf = false
        private val btmLyloc = IntArray(2)
        private val hdLyloc = IntArray(2)
        private var params: FrameLayout.LayoutParams? = null
        private var aniFlg = false

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (frmLyAniRun) {
                aniFlg = true
                return aniFlg
            }
            if (aniFlg) {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    aniFlg = false
                }
                return true
            }
            val cy = event.y
            //Log.d(ACTIVITY_TAG,"###################### ny: "+ny+",ny1: "+ny1);
            if (event.action == MotionEvent.ACTION_DOWN) {
                mvf = false
                params = frmCtnLy?.layoutParams as FrameLayout.LayoutParams
                btmLy?.getLocationOnScreen(btmLyloc)
                hdLy?.getLocationOnScreen(hdLyloc)
                magTop = wHeight - hdLyloc[1]
                params?.topMargin = magTop
                frmLy?.visibility = View.VISIBLE
                frmLy?.setWillNotDraw(false)
                frmCtnLy?.layoutParams = params
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                if (mvf) {
                    var b = false
                    if (ny1 >= ny) {
                        b = true
                    }
                    doFrmLyAnimation(magTop, if (b) 0 else wHeight - hdLyloc[1], 300, b)
                } else {
                    doFrmLyAnimation(magTop, 0, 400, true)
                }

            } else if (event.action == MotionEvent.ACTION_MOVE && Math.abs(cy - ny) > 5) {
                mvf = true
                magTop = btmLyloc[1] + cy.toInt()
                if (params != null) {
                    params?.topMargin = magTop
                    frmCtnLy?.layoutParams = params
                }
            }
            ny1 = ny
            ny = cy
            return true
        }
    }

    private inner class FrmLyTouchListener : View.OnTouchListener {
        private var ny: Float = 0.toFloat()
        private var ny1: Float = 0.toFloat()
        private var ry: Float = 0.toFloat()
        private var roy: Float = 0.toFloat()
        private var magTop: Int = 0
        private var mvf = false
        private val btmLyloc = IntArray(2)
        private val hdLyloc = IntArray(2)
        private var params: FrameLayout.LayoutParams? = null
        private var aniFlg = false
        private val frmLyloc: IntArray? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (frmLrcVwTchd) {
                lrcMovView?.onMyTouchEvent(v, event)
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    frmLrcVwTchd = false
                }
                return true
            }
            if (frmLyAniRun) {
                aniFlg = true
                return aniFlg
            }
            if (aniFlg) {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    aniFlg = false
                    //Log.w(TAG, "nomeaning touch stoped!");
                }
                return true
            }
            ry = event.rawY.toInt().toFloat()
            if (event.action == MotionEvent.ACTION_DOWN) {
                roy = event.rawY.toInt().toFloat()
                mvf = false
                magTop = 0
                params = frmCtnLy?.layoutParams as FrameLayout.LayoutParams
                btmLy?.getLocationOnScreen(btmLyloc)
                hdLy?.getLocationOnScreen(hdLyloc)
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                if (mvf) {
                    var b = false
                    if (ny1 >= ny) {
                        b = true
                    }
                    doFrmLyAnimation(magTop, if (b) 0 else wHeight - hdLyloc[1], 300, b)
                } else {
                    //Log.v(TAG, "its looks loke FrmLy clicked!")
                    val hdvw = MainLay().createVolumeView(this@MainActivity)
                    volumeSldMenu = SldMenu.create(this@MainActivity, hdvw, vwCtnFrmLy)
                    volumeSldMenu?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
                        override fun onStateChange(state: Int) {
                            if (state == SldMenu.MENU_STATE_GONE_START) {
                                popBackLsnStack(BK_KEY_VOLUME, this@MainActivity)
                            } else if (state == SldMenu.MENU_STATE_SHOW) {
                                push2BackLsnStack(BackLsnHolder(BK_KEY_VOLUME, this@MainActivity))
                            } else if (state == SldMenu.MENU_STATE_GONE) {
                                volumeSldMenu = null
                            }
                        }
                    })
                    volumeSldMenu?.show(SldMenu.SHOW_TYPE_BOTTOM)
                }

            } else if (event.action == MotionEvent.ACTION_MOVE && Math.abs(ry - ny) > 5) {
                frmLyStartMv = true
                if (0 == magTop) {
                    btmLy?.visibility = View.VISIBLE
                    btmLy?.setWillNotDraw(false)
                    cenLy?.visibility = View.VISIBLE
                    cenLy?.setWillNotDraw(false)
                }
                mvf = true
                magTop += (ry - ny).toInt()
                if (magTop < 0) magTop = 0
                if (params != null) {
                    params?.topMargin = magTop
                    frmCtnLy?.layoutParams = params
                }
            }
            ny1 = ny
            ny = ry
            return true
        }
    }


    private fun doFrmLyAnimation(begin: Int, end: Int, duration: Int, b: Boolean) {
        val params = frmCtnLy?.layoutParams as FrameLayout.LayoutParams
        if (!b && begin == 0) {
            btmLy?.visibility = View.VISIBLE
            btmLy?.setWillNotDraw(false)
            cenLy?.visibility = View.VISIBLE
            cenLy?.setWillNotDraw(false)
        }
        frmLyAniRun = true
        val mAnimator = ValueAnimator.ofInt(begin, end)
        mAnimator.addUpdateListener { animation ->
            val aniVal = animation.animatedValue as Int
            params.topMargin = aniVal
            frmCtnLy?.layoutParams = params
        }
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (!b && frmLyState == AppConstants.VW_STATE_SHOW) {
                    frmLyState = AppConstants.VW_STATE_GONE_START
                    OnFrmLyStateChg(frmLyState)
                } else if (b && frmLyState == AppConstants.VW_STATE_GONE) {
                    frmLyState = AppConstants.VW_STATE_SHOW_START
                    OnFrmLyStateChg(frmLyState)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                params.topMargin = end
                frmCtnLy?.layoutParams = params

                if (!b) {
                    frmLy?.visibility = View.GONE
                    frmLy?.setWillNotDraw(true)

                    if (frmLyState == AppConstants.VW_STATE_GONE_START) {
                        frmLyState = AppConstants.VW_STATE_GONE
                        OnFrmLyStateChg(frmLyState)
                    }
                } else if (b) {
                    frmLyStartMv = false
                    btmLy?.visibility = View.GONE
                    btmLy?.setWillNotDraw(true)
                    cenLy?.visibility = View.GONE
                    cenLy?.setWillNotDraw(true)

                    if (frmLyState == AppConstants.VW_STATE_SHOW_START) {
                        frmLyState = AppConstants.VW_STATE_SHOW
                        OnFrmLyStateChg(frmLyState)
                    }
                }

                frmLyAniRun = false
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.duration = duration.toLong()
        mAnimator.start()
    }
/**
    private fun doFrmLyAnimationA(begin: Int, end: Int, duration: Int, b: Boolean) {
        if (!b && begin == 0) {
            btmLy?.visibility = View.VISIBLE
            btmLy?.setWillNotDraw(false)
            cenLy?.visibility = View.VISIBLE
            cenLy?.setWillNotDraw(false)
        }
        frmLyAniRun = true
        if (!b && frmLyState == AppConstants.VW_STATE_SHOW) {
            frmLyState = AppConstants.VW_STATE_GONE_START
            OnFrmLyStateChg(frmLyState)
        } else if (b && frmLyState == AppConstants.VW_STATE_GONE) {
            frmLyState = AppConstants.VW_STATE_SHOW_START
            OnFrmLyStateChg(frmLyState)
        }
        object : CountDownTimer(duration.toLong(), 15) {
            internal var params = frmCtnLy?.layoutParams as FrameLayout.LayoutParams

            override fun onTick(millisUntilFinished: Long) {
                val a = (duration - millisUntilFinished).toDouble()
                val b = duration.toDouble()
                val aniVal = (a / b * (end - begin) + begin).toInt()
                setVal(aniVal)
            }

            override fun onFinish() {
                setVal(end)

                if (!b) {
                    frmLy?.visibility = View.GONE
                    frmLy?.setWillNotDraw(true)

                    if (frmLyState == AppConstants.VW_STATE_GONE_START) {
                        frmLyState = AppConstants.VW_STATE_GONE
                        OnFrmLyStateChg(frmLyState)
                    }
                } else if (b) {
                    frmLyStartMv = false
                    btmLy?.visibility = View.GONE
                    btmLy?.setWillNotDraw(true)
                    cenLy?.visibility = View.GONE
                    cenLy?.setWillNotDraw(true)

                    if (frmLyState == AppConstants.VW_STATE_SHOW_START) {
                        frmLyState = AppConstants.VW_STATE_SHOW
                        OnFrmLyStateChg(frmLyState)
                    }
                }

                frmLyAniRun = false
            }

            private fun setVal(av: Int) {
                params.topMargin = av
                frmCtnLy?.layoutParams = params
            }
        }.start()

    }
    */

    private fun doSldLyAnimation(begin: Int, end: Int, duration: Int, b: Boolean) {
        val params = sldLy?.layoutParams as RelativeLayout.LayoutParams
        if (b && begin < 0) {
            covLy?.visibility = View.VISIBLE
            covLy?.setWillNotDraw(false)
            sldLy?.visibility = View.VISIBLE
            covLy?.setWillNotDraw(false)
        }
        //Log.d(ACTIVITY_TAG,"--------------"+begin+"-----------"+end);
        sldLyAniRun = true
        val mAnimator = ValueAnimator.ofInt(begin, end)
        mAnimator.addUpdateListener { animation ->
            val aniVal = animation.animatedValue as Int
            params.leftMargin = aniVal
            sldLy?.layoutParams = params
        }
        mAnimator.addListener(object : Animator.AnimatorListener {
            internal var params = sldLy?.layoutParams as RelativeLayout.LayoutParams

            override fun onAnimationStart(animation: Animator) {
                if (!b && sldLyState == AppConstants.VW_STATE_SHOW) {
                    sldLyState = AppConstants.VW_STATE_GONE_START
                    OnSldLyStateChg(sldLyState)
                } else if (b && sldLyState == AppConstants.VW_STATE_GONE) {
                    sldLyState = AppConstants.VW_STATE_SHOW_START
                    OnSldLyStateChg(sldLyState)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                setVal(end)

                if (!b) {
                    covLy?.visibility = View.GONE
                    covLy?.setWillNotDraw(true)
                    sldLy?.visibility = View.GONE
                    sldLy?.setWillNotDraw(true)
                    if (sldLyState == AppConstants.VW_STATE_GONE_START) {
                        sldLyState = AppConstants.VW_STATE_GONE
                        OnSldLyStateChg(sldLyState)
                    }
                } else if (b && sldLyState == AppConstants.VW_STATE_SHOW_START) {
                    sldLyState = AppConstants.VW_STATE_SHOW
                    OnSldLyStateChg(sldLyState)


                }

                sldLyAniRun = false
            }

            private fun setVal(av: Int) {
                params.leftMargin = av
                sldLy?.layoutParams = params
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.duration = duration.toLong()
        mAnimator.start()
    }
/**
    private fun doSldLyAnimationA(begin: Int, end: Int, duration: Int, b: Boolean) {
        if (b && begin < 0) {
            covLy?.visibility = View.VISIBLE
            covLy?.setWillNotDraw(false)
            sldLy?.visibility = View.VISIBLE
            covLy?.setWillNotDraw(false)
        }
        sldLyAniRun = true
        if (!b && sldLyState == AppConstants.VW_STATE_SHOW) {
            sldLyState = AppConstants.VW_STATE_GONE_START
            OnSldLyStateChg(sldLyState)
        } else if (b && sldLyState == AppConstants.VW_STATE_GONE) {
            sldLyState = AppConstants.VW_STATE_SHOW_START
            OnSldLyStateChg(sldLyState)
        }
        object : CountDownTimer(duration.toLong(), 15) {
            internal var params = sldLy?.layoutParams as RelativeLayout.LayoutParams

            override fun onTick(millisUntilFinished: Long) {
                val a = (duration - millisUntilFinished).toDouble()
                val b = duration.toDouble()
                val aniVal = (a / b * (end - begin) + begin).toInt()
                setVal(aniVal)
            }

            override fun onFinish() {
                setVal(end)

                if (!b) {
                    covLy?.visibility = View.GONE
                    covLy?.setWillNotDraw(true)
                    sldLy?.visibility = View.GONE
                    sldLy?.setWillNotDraw(true)
                    if (sldLyState == AppConstants.VW_STATE_GONE_START) {
                        sldLyState = AppConstants.VW_STATE_GONE
                        OnSldLyStateChg(sldLyState)
                    }
                } else if (b && sldLyState == AppConstants.VW_STATE_SHOW_START) {
                    sldLyState = AppConstants.VW_STATE_SHOW
                    OnSldLyStateChg(sldLyState)


                }
                sldLyAniRun = false
            }

            private fun setVal(av: Int) {
                params.leftMargin = av
                sldLy?.layoutParams = params
            }
        }.start()

    }
*/
    private fun OnSldLyStateChg(state: Int) {
        when (state) {
            AppConstants.VW_STATE_SHOW -> push2BackLsnStack(BackLsnHolder(BK_KEY_SLDLY, this@MainActivity))
            AppConstants.VW_STATE_SHOW_START -> {
                var picblur = PrefUtil.get(this@MainActivity, AppConstants.PREF_KEY_BKG_PIC_ISBLUR, 0, AppConstants.PREF_NAME_PARAMS) as Int
                val mapp = mapOf(0 to R.string.sld_ls_ctrlbkgnormal,
                        1 to R.string.sld_ls_ctrlbkgblur1,
                        2 to R.string.sld_ls_ctrlbkgblur2,
                        3 to R.string.sld_ls_ctrlbkgblur3,
                        4 to R.string.sld_ls_ctrlbkgblur4)
                bkgBlurBtn?.text= getResString(this@MainActivity,mapp.get(picblur)?:0)
                //playMode
                var pm = AppContext.instance?.playMode?:0
                val mapq = mapOf(0 to R.string.sld_ls_ctrlPlayMode0,
                        1 to R.string.sld_ls_ctrlPlayMode1,
                        2 to R.string.sld_ls_ctrlPlayMode2,
                        3 to R.string.sld_ls_ctrlPlayMode3)
                sldPlayModeBtn?.text= getResString(this@MainActivity,mapq.get(pm)?:0)
            }
            AppConstants.VW_STATE_GONE -> {}
            AppConstants.VW_STATE_GONE_START -> popBackLsnStack(BK_KEY_SLDLY, this@MainActivity)
        }

    }

    private fun OnFrmLyStateChg(state: Int) {
        when (state) {
            AppConstants.VW_STATE_SHOW -> {

                push2BackLsnStack(BackLsnHolder(BK_KEY_FRMLY, this@MainActivity))

                val bd = Bundle()
                bd.putBoolean("FLG", true)
                val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_SHOWMODECHG)
                itt.putExtras(bd)
                mLocalBroadcastManager?.sendBroadcast(itt)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                addLrcMovVwAfter()
            }
            AppConstants.VW_STATE_GONE -> {
                val bda = Bundle()
                bda.putBoolean("FLG", false)
                val itta = Intent(AppConstants.PLAYER_CTRL_ACTION_SHOWMODECHG)
                itta.putExtras(bda)
                mLocalBroadcastManager?.sendBroadcast(itta)
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            AppConstants.VW_STATE_GONE_START -> popBackLsnStack(BK_KEY_FRMLY, this@MainActivity)
        }

    }

    private inner class BtnOnClickLsn : View.OnClickListener {
        override fun onClick(v: View) {
            val vid = v.id
            val itt: Intent
            val bd: Bundle
            when (vid) {
                R.id.btmNextBtn, R.id.main_frm_nextBtn -> {
                    itt = Intent(AppConstants.PLAYER_CTRL_ACTION_NEXT)
                    mLocalBroadcastManager?.sendBroadcast(itt)
                }
                R.id.btmPlayBtn, R.id.main_frm_playBtn -> {
                    itt = Intent(AppConstants.PLAYER_CTRL_ACTION_PLAY)
                    mLocalBroadcastManager?.sendBroadcast(itt)
                }
                R.id.main_frm_prevBtn -> {
                    itt = Intent(AppConstants.PLAYER_CTRL_ACTION_PREV)
                    mLocalBroadcastManager?.sendBroadcast(itt)
                }
                R.id.main_frm_backBtn -> {
                    val hdLyloc = IntArray(2)
                    hdLy?.getLocationOnScreen(hdLyloc)
                    doFrmLyAnimation(0, wHeight - hdLyloc[1], 300, false)
                }
                R.id.home_ex_localBtn -> {
                    val args = Bundle()
                    args.putInt("tp", 1)
                    args.putString("tit", "本地音乐")
                    val newFragment = HmFrgm()
                    newFragment.arguments = args
                    val transaction = fragmentManager.beginTransaction()
                    transaction.add(R.id.main_cen_hd_ly, newFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.btmLsBtn, R.id.main_frm_tmplsBtn -> genTmpLsVw()
                R.id.main_sldLy_appOverBtn -> android.os.Process.killProcess(android.os.Process.myPid())
                R.id.main_sldLy_bkgSettingBtnA -> {
                    val fileSelector = FileSelector(this@MainActivity)
                    val sdpth = SDCardUtils.sdCardPath
                    fileSelector.open(if (utilIsEmpty(sdpth)) "/" else sdpth, arrayOf(".jpg"))
                    fileSelector.setOnSelectOkListener(object: FileSelector.OnSelectOkListener{
                        override fun onSelectOk(fpath: String?) {
                            if(fpath!=null) {
                                val bd = getBkgDrawable(fpath,AppConstants.PREF_KEY_APP_LS_BKG)
                                hdLy?.backgroundDrawable = bd
                            }
                        }
                    })
                }
                R.id.main_sldLy_bkgSettingBtnB -> {
                    val fileSelectorb = FileSelector(this@MainActivity)
                    val sdptha = SDCardUtils.sdCardPath
                    fileSelectorb.open(if (utilIsEmpty(sdptha)) "/" else sdptha, arrayOf(".jpg"))
                    fileSelectorb.setOnSelectOkListener(object: FileSelector.OnSelectOkListener{
                        override fun onSelectOk(fpath: String?) {
                            if(fpath!=null) {
                                val bd = getBkgDrawable(fpath,AppConstants.PREF_KEY_APP_CTRL_BKG)
                                frmCtnLy?.backgroundDrawable = bd
                            }
                        }
                    })
                }
                R.id.main_sldLy_bkgblurBtn -> {
                    var picblur = PrefUtil.get(this@MainActivity, AppConstants.PREF_KEY_BKG_PIC_ISBLUR, 0, AppConstants.PREF_NAME_PARAMS) as Int
                    picblur++
                    if(picblur>4)picblur=0
                    val mapp = mapOf(0 to R.string.sld_ls_ctrlbkgnormal,
                            1 to R.string.sld_ls_ctrlbkgblur1,
                            2 to R.string.sld_ls_ctrlbkgblur2,
                            3 to R.string.sld_ls_ctrlbkgblur3,
                            4 to R.string.sld_ls_ctrlbkgblur4)
                    bkgBlurBtn?.text= getResString(this@MainActivity,mapp.get(picblur)?:0)
                    PrefUtil.put(this@MainActivity, AppConstants.PREF_KEY_BKG_PIC_ISBLUR,picblur, AppConstants.PREF_NAME_PARAMS)

                }
                R.id.main_sldLy_playModeBtn -> {
                    var pm = AppContext.instance?.playMode?:0
                    pm++
                    if(pm>3)pm=0
                    val mapq = mapOf(0 to R.string.sld_ls_ctrlPlayMode0,
                            1 to R.string.sld_ls_ctrlPlayMode1,
                            2 to R.string.sld_ls_ctrlPlayMode2,
                            3 to R.string.sld_ls_ctrlPlayMode3)
                    sldPlayModeBtn?.text= getResString(this@MainActivity,mapq.get(pm)?:0)
                    AppContext.instance?.setPlayMod(this@MainActivity,pm)

                }
                else -> return
            }

        }
    }

    private fun getBkgDrawable(fpath: String,prefnm: String): Drawable {
        val bitmp = BitMapUtil.getBitmapFromFile(fpath)
        val bitmpa = BitMapUtil.bitmapCrop(bitmp, AndUtil.getScreenWidth(this@MainActivity), AndUtil.getScreenHeight(this@MainActivity))
        val bkgpth = SDCardUtils.sdCardPath + "/" + AppConstants.APP_BKGPIC_PATH
        val md5fnm = AndUtil.MD5(fpath)
        var picblur = 0
        picblur = PrefUtil.get(this@MainActivity, AppConstants.PREF_KEY_BKG_PIC_ISBLUR, picblur, AppConstants.PREF_NAME_PARAMS) as Int
        var bitmpb = bitmpa
        when(picblur) {
            1 -> {
                bitmpb = BitMapUtil.blurBitmap(this@MainActivity, bitmpa, 2f)
            }
            2 -> {
                bitmpb = BitMapUtil.blurBitmap(this@MainActivity, bitmpa, 10f)
            }
            3 -> {
                bitmpb = BitMapUtil.blurBitmap(this@MainActivity, bitmpa, 18f)
            }
            4 -> {
                bitmpb = BitMapUtil.blurBitmap(this@MainActivity, bitmpb, 25f)
            }
        }

        BitMapUtil.saveBitmap(bitmpb, bkgpth, md5fnm)
        PrefUtil.put(this@MainActivity, prefnm, md5fnm, AppConstants.PREF_NAME_PARAMS)
        val bd = BitmapDrawable(null, bitmpb)
        return bd
    }

    private fun genTmpLsVw() {
        val hdvw = TmplsLay().createView(this)
        val tmpLstit = hdvw.findViewById(R.id.tmp_lshd_lsTit) as TextView
        tmpLstit.text = "播放列表 - 0首"
        val tmpLsView = hdvw.findViewById(R.id.tmp_lshd_ls) as ListView
        val synBtn = hdvw.findViewById(R.id.tmp_lshd_optBtn) as ImageButton
        tmpLsAdapter = TmpLsAdapter(this@MainActivity, null)
        tmpLsView.adapter = tmpLsAdapter
        tmpLsView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val bd = Bundle()
            bd.putInt("TYPE", 1)
            bd.putInt("INDX", position)
            bd.putBoolean("PLY", true)
            val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_INDX)
            itt.putExtras(bd)
            LocalBroadcastManager.getInstance(this@MainActivity).sendBroadcast(itt)

            val tadp = parent.adapter as TmpLsAdapter
            tadp.curSongIndx = position
            tadp.notifyDataSetChanged()
        }
        synBtn.onClick {
            AppContext.instance?.updateCurrentTmpls2Db()
        }
        tmpSldMenu = SldMenu.create(this, hdvw, vwCtnFrmLy)
        tmpSldMenu?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
            override fun onStateChange(state: Int) {
                if (state == SldMenu.MENU_STATE_GONE_START) {
                    popBackLsnStack(BK_KEY_TMPLS, this@MainActivity)
                } else if (state == SldMenu.MENU_STATE_SHOW) {
                    push2BackLsnStack(BackLsnHolder(BK_KEY_TMPLS, this@MainActivity))
                } else if (state == SldMenu.MENU_STATE_GONE) {
                    tmpSldMenu = null
                }
            }
        })
        tmpSldMenu?.show(SldMenu.SHOW_TYPE_BOTTOM)
        initTmpLs()
    }

    fun initTmpLs() {
        if (tmpLsAdapter == null) return
        val tmpls = AppContext.instance?.tmpLs?:return
        tmpLsAdapter?.list = tmpls
        curSongIndx = AppContext.instance?.curIndex?:0
        tmpLsAdapter?.curSongIndx = curSongIndx
        tmpLsAdapter?.notifyDataSetChanged()
        val tmpLsVw = tmpSldMenu?.contentView?.findViewById(R.id.tmp_lshd_ls) as ListView
        tmpLsVw.setSelectionFromTop(curSongIndx, AndUtil.dp2Px(this@MainActivity, 100f))
        val tmpLstit = tmpSldMenu?.contentView?.findViewById(R.id.tmp_lshd_lsTit) as TextView
        tmpLstit.text = "播放列表 - " + tmpLsAdapter?.count + "首"
    }

    private inner class PlayRevwBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Log.w(ACTIVITY_TAG, "****PlayRevwBroadcastReceiver: " + intent.getAction());
            val bd: Bundle
            bd = intent.extras
            if (AppConstants.PLAYER_REVW_ACTION_PROG == intent.action) {
                val prog = bd.getInt("PROG")
                if (frmLyState == AppConstants.VW_STATE_SHOW && !frmLyStartMv) {
                    frmSeekBar?.progress = prog
                    frmCurPosTime?.text = AndUtil.timeFormate(prog.toLong())
                    val lrcPos = bd.getInt("LRCCPOS", -1)
                    if (lrcPos > -1 && lrcMovView != null) {
                        lrcMovView?.moveTo(lrcPos)
                    }
                } else {
                    btmProgBar?.progress = prog
                }
            } else if (AppConstants.PLAYER_REVW_ACTION_PLAY == intent.action) {
                curSongIndx = bd.getInt("INDX", -1)
                val tit = bd.getString("TITLE")
                val ati = bd.getString("ARTIST")
                val dur = bd.getInt("DURATION")
                val isp = bd.getBoolean("ISPLAY")
                val isn = bd.getBoolean("ISNEW")
                val lrcTim = bd.get("LRCTIM") as IntArray?
                val lrcMsg = bd.get("LRCMSG") as Array<String>?
                //btm
                btmSongTitle?.text = if(utilNotNull(tit)) tit else "< unknown >"
                btmSongArtist?.text = if(utilNotNull(ati)) ati else "< unknown >"
                btmProgBar?.max = dur
                //frm
                frmSeekBar?.max = dur
                frmSongTitle?.text = tit
                frmSongArtist?.text = ati
                frmAllTime?.text = AndUtil.timeFormate(dur.toLong())
                if (isn) {
                    frmLrcVw?.removeAllViews()
                    isLrcMovViewAdd = false
                    if (lrcMsg != null) {
                        if (lrcMovView == null) {
                            lrcMovView = LrcMovView(this@MainActivity)
                            lrcMovView?.setOnIndexChangeListener(object: LrcMovView.OnIndexChangeListener{
                                override fun onIndexChange(indx: Int, pos: Int) {
                                    val bd = Bundle()
                                    bd.putInt("SKPOS", pos)
                                    val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_SEEK)
                                    itt.putExtras(bd)
                                    mLocalBroadcastManager?.sendBroadcast(itt)
                                }
                            })
                        }
                        val lmvf = frmLyState == AppConstants.VW_STATE_SHOW && thisState == AppConstants.VW_STATE_SHOW
                        lrcMovView?.initViews(lrcTim, lrcMsg, frmLrcVwHeight, lmvf)
                        if (lmvf) {
                            frmLrcVw?.addView(lrcMovView)
                            isLrcMovViewAdd = true
                        }
                    }else{
                        lrcMovView = null
                    }
                    btmImgVw?.backgroundDrawable = BitMapUtil.getMatrixDrawableByRid(this@MainActivity,R.mipmap.default_music_icon, utilGetRandomColorArray())
                }

                //icon
                val drid: Int
                val frid: Int
                if (!isp) {
                    drid = R.drawable.main_btm_play
                    frid = R.drawable.main_frm_play_btn_style
                } else {
                    drid = R.drawable.main_btm_pause
                    frid = R.drawable.main_frm_pause_btn_style
                }
                btmPlayBtn?.setBackgroundResource(drid)
                frmPlayBtn?.setBackgroundResource(frid)

            }
        }
    }

    private fun addLrcMovVwAfter() {
        val lmvf = frmLyState == AppConstants.VW_STATE_SHOW && thisState == AppConstants.VW_STATE_SHOW
        if (lmvf && !isLrcMovViewAdd && lrcMovView != null) {
            frmLrcVw?.removeAllViews()
            frmLrcVw?.addView(lrcMovView)
            isLrcMovViewAdd = true
        }
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val bd = msg.data
            when (msg.what) {
                WHAT_GET_FAV_LS//更新主页面歌单列表
                -> {
                    val grpls = bd.getSerializable("grpls") as List<SongGrpInfo>?
                    val chdls = bd.getSerializable("chdls") as List<List<SongGrpInfo>>?
                    val adp = cttFavLsvw?.expandableListAdapter as HomeExpdLsAdapter?
                    adp?.grpLs = grpls
                    adp?.chdLs = chdls
                    adp?.notifyDataSetChanged()
                    if(adp!=null) {
                        for (i in 0..adp.groupCount - 1) {
                            cttFavLsvw?.expandGroup(i)
                        }
                    }
                }
            }
            super.handleMessage(msg)
        }
    }

    private inner class FrmSeekBarChgLsn : SeekBar.OnSeekBarChangeListener {
        private var opos: Int = 0

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            opos = seekBar.progress
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            val cpos = seekBar.progress
            if (cpos != opos) {
                val bd = Bundle()
                bd.putInt("SKPOS", cpos)
                val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_SEEK)
                itt.putExtras(bd)
                mLocalBroadcastManager?.sendBroadcast(itt)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult requestCode: " + requestCode)
        if (PermissionUtils.verifyPermissions(grantResults)) {//权限申请成功后根据请求码更新相关界面
            when (requestCode) {
                SONGLS_REQUEST_CODE -> Log.w(TAG, "onRequestPermissionsResult    SONGLS_REQUEST_CODE............")
                DBLS_REQUEST_CODE -> initHmFavLs()
            }
        } else {
            // Permission Denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PermissionUtils.PERMISSION_SETTING_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //do action
                } else {
                    Toast.makeText(this, "not has setting permission", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!(backLsnStack?.isEmpty()?:true)) {
                val bkhd = backLsnStack?.pop()
                val obkLsn = bkhd?.onBackListener
                return obkLsn?.onBackKeyUp(bkhd.requestValue, event, bkhd.requestCode)?:false
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (sldLyAniRun) return true
            val sldLyW = AndUtil.dp2Px(this, SLD_MENU_WIDTH.toFloat())
            if (sldLyState == 1) {
                doSldLyAnimation(0, -sldLyW, 200, false)
            } else if (sldLyState == 0) {
                doSldLyAnimation(-sldLyW, 0, 200, true)
            }
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onBackKeyUp(keyCode: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            BK_KEY_SLDLY -> {
                if (sldLyAniRun) return true
                val sldLyW = AndUtil.dp2Px(this, SLD_MENU_WIDTH.toFloat())
                doSldLyAnimation(0, -sldLyW, 200, false)
            }
            BK_KEY_FRMLY -> {
                if (frmLyAniRun) return true
                val hdLyloc = IntArray(2)
                hdLy?.getLocationOnScreen(hdLyloc)
                doFrmLyAnimation(0, wHeight - hdLyloc[1], 300, false)
            }
            BK_KEY_TMPLS -> tmpSldMenu?.cancle()
            BK_KEY_netSelectSldMenu -> netSelectSldMenu?.cancle()
            BK_KEY_VOLUME -> volumeSldMenu?.cancle()
        }
        return true
    }

    private fun initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

            val statusHeight = getStatusBarHeight(this)
            mTabLayout?.setPadding(0, statusHeight, 0, 0)
            frmCtnCvLy?.setPadding(0, statusHeight, 0, 0)
            //sldLy.setPadding(0, statusHeight, 0, 0);
        }
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onResume() {
        thisState = AppConstants.VW_STATE_SHOW
        val filter = IntentFilter()
        filter.addAction(AppConstants.PLAYER_REVW_ACTION_PLAY)
        filter.addAction(AppConstants.PLAYER_REVW_ACTION_PROG)
        filter.addAction(AppConstants.PLAYER_REVW_ACTION_SENDLS)
        mLocalBroadcastManager?.registerReceiver(mReceiver, filter)

        val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_RESUME)
        mLocalBroadcastManager?.sendBroadcast(itt)

        initState()

        if (frmLyState == 1) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        super.onResume()
    }


    override fun onStop() {
        thisState = AppConstants.VW_STATE_GONE
        mLocalBroadcastManager?.unregisterReceiver(mReceiver)
        val itt = Intent(AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_STOP)
        mLocalBroadcastManager?.sendBroadcast(itt)

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun push2BackLsnStack(obkLsnHd: BackLsnHolder) {
        backLsnStack?.push(obkLsnHd)
    }

    fun popBackLsnStack(reqCod: Int, bkLsn: OnBackListener):BackLsnHolder?{
        if (!(backLsnStack?.isEmpty()?:true)) {
            val bkhd = backLsnStack?.peek()
            if (bkhd != null && bkhd.onBackListener === bkLsn && bkhd.requestCode == reqCod)
                return backLsnStack?.pop()
        }
        return null
    }

    fun setFrmLrcVwTchd(frmLrcVwTchd: Boolean) {
        this.frmLrcVwTchd = frmLrcVwTchd
    }

    companion object {
        private val TAG = "MainActivity"
        private val WHAT_GET_FAV_LS = 1
        private val WHAT_GET_TMP_LS = 2
        private val BK_KEY_SLDLY = 1
        private val BK_KEY_FRMLY = 2
        private val BK_KEY_TMPLS = 3
        private val BK_KEY_netSelectSldMenu = 4
        private val BK_KEY_VOLUME = 5

        val SONGLS_REQUEST_CODE = 0XA
        val DBLS_REQUEST_CODE = 0XB
        private val SLD_MENU_WIDTH = 230
    }
}
