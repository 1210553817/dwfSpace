package com.fxqyem.msg.act

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.*
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.adp.MemLsAdapter
import com.fxqyem.msg.adp.MemMsgLsAdapter
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ben.BackLsnHolder
import com.fxqyem.msg.ben.OnBackListener
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgLay
import com.fxqyem.msg.lay.MsgPagerLay
import com.fxqyem.msg.ser.MsgService
import com.fxqyem.msg.utl.AndUtil
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.vw.*
import org.jetbrains.anko.*
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
    var btmGrpImg: android.widget.ImageView? = null
    var btmGrpTxt: android.widget.TextView? = null
    var btmSetImg: android.widget.ImageView? = null
    var btmSetTxt: android.widget.TextView? = null
    /*cen*/
    var cenLy: android.widget.FrameLayout? = null
    var cenhdLy: android.widget.RelativeLayout? = null
    var cenfrmLy: android.widget.FrameLayout? = null
    var cenViewPager: ViewPager? = null
        //pager
    var memVw: View? = null
    var grpVw: View? = null
    var setVw: View? = null
            //mem
    var memListVw: ListView? = null

    /*sld*/
    var sldBarLy: android.widget.LinearLayout? = null
    var sldCovLy: android.widget.LinearLayout? = null
    var sldLy: android.widget.RelativeLayout? = null
    var sldLyImg: android.widget.ImageView? = null
    var msgBkgSetBtn: android.widget.Button? = null
    var pepPicSetBtn: android.widget.Button? = null
    var appAboutBtn: android.widget.Button? = null
    var appOverBtn: android.widget.Button? = null

    /*ctn*/
    var oneCtnFrmLy: android.widget.FrameLayout? = null

    /*send lay*/
    private var msgLsFrgm: MsgSendFrgm? = null

    /*service*/
    /*datas*/
//    private var msgHandler: MsgHandler? = null
    var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mReceiver: BroadcastReceiver? = null

    /*params*/
    private var thisState: Int = 0
    private var sldLyAniRun = false
    private var sldLyState = 0

    //Keys
    private var backLsnStack: Stack<BackLsnHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MsgLay().setContentView(this)
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
        val btmGrpBtnPly =  btmGrpImg?.parent as LinearLayout
        val btmSetBtnPly =  btmSetImg?.parent as LinearLayout
        btmMemBtnPly.onClick {
            chooseBtmItem(0)
            cenViewPager?.currentItem=0
        }
        btmGrpBtnPly.onClick {
            chooseBtmItem(1)
            cenViewPager?.currentItem=1
        }
        btmSetBtnPly.onClick {
            chooseBtmItem(2)
            cenViewPager?.currentItem=2
        }
        //pager
        initMsgPager()
        //send Lay

        //sld
        sldBarLy?.setOnTouchListener(SldBarLyTouchListener())
        sldCovLy?.setOnTouchListener(CovLyTouchListener())
        sldLy?.setOnTouchListener(SldLyTouchListener())
    }

    private fun initMsgPager(){
        val msgPagerLay = MsgPagerLay()
        val memVw = msgPagerLay.createMemVw(this)
        val grpVw = msgPagerLay.createMemVw(this)
        val setVw = msgPagerLay.createMemVw(this)

        this.memVw= memVw
        this.grpVw= grpVw
        this.setVw= setVw
        val pagerVws = listOf(memVw,grpVw,setVw)
        val mAdapter = MyPagerAdapter(pagerVws, listOf(
                getResString(this,R.string.memls),
                getResString(this,R.string.grpls),
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
        /*mem views*/
        initPagerMemViews(memVw)


    }
    private fun initPagerMemViews(memVw: View){
        memListVw = memVw.find(R.id.msg_lay_cen_mempg_lsvw)
        val memAdapter = MemLsAdapter(this,null)
        memListVw?.adapter = memAdapter
        memListVw?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val adp = memListVw?.adapter as MemLsAdapter
            var itm = adp?.list?.get(position)
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

    }

    fun reloadMemList(){
        val itt = Intent(AppConstants.ACTION_SER_LOAD_MEM_ITEMS)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }
    private fun chooseBtmItem(indx: Int){
        val btnArr = arrayOf(btmMemImg,btmGrpImg,btmSetImg)
        val txtArr = arrayOf(btmMemTxt,btmGrpTxt,btmSetTxt)
        val iconArr = arrayOf(R.mipmap.red_thm_29,R.mipmap.red_thm_30,R.mipmap.red_thm_15)
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
    internal inner class MyPagerAdapter(private val mViewList: List<View>,private val mViewTitles: List<String>) : PagerAdapter() {

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
            return mViewTitles[position]//页卡标题
        }

    }

    private inner class BtnClickLsn: View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.msg_lay_core_btm_mem_img -> {

                }

                else -> return
            }

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
                sldCovLy?.visibility = View.VISIBLE
                sldCovLy?.setWillNotDraw(false)
                sldLy?.visibility = View.VISIBLE
                sldLy?.setWillNotDraw(false)
                params = sldLy?.layoutParams as RelativeLayout.LayoutParams
                sldLyW = AndUtil.dp2Px(this@MsgActivity, SLD_MENU_WIDTH.toFloat())
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
                sldLyW = AndUtil.dp2Px(this@MsgActivity, SLD_MENU_WIDTH.toFloat())
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
    private inner class SldLyTouchListener : View.OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return false
        }
    }
    private fun doSldLyAnimation(begin: Int, end: Int, duration: Int, b: Boolean) {
        val params = sldLy?.layoutParams as RelativeLayout.LayoutParams
        if (b && begin < 0) {
            sldCovLy?.visibility = View.VISIBLE
            sldCovLy?.setWillNotDraw(false)
            sldLy?.visibility = View.VISIBLE
            sldCovLy?.setWillNotDraw(false)
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
                    sldCovLy?.visibility = View.GONE
                    sldCovLy?.setWillNotDraw(true)
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

    private fun OnSldLyStateChg(state: Int) {
        when (state) {
            AppConstants.VW_STATE_SHOW -> push2BackLsnStack(BackLsnHolder(BK_KEY_SLDLY, this@MsgActivity))
            AppConstants.VW_STATE_SHOW_START -> { }
            AppConstants.VW_STATE_GONE -> {}
            AppConstants.VW_STATE_GONE_START -> popBackLsnStack(BK_KEY_SLDLY, this@MsgActivity)
        }

    }

    override fun onBackKeyUp(reqValue: Int, event: KeyEvent, reqCod: Int): Boolean {
        when (reqCod) {
            BK_KEY_SLDLY -> {
                if (sldLyAniRun) return true
                val sldLyW = AndUtil.dp2Px(this, SLD_MENU_WIDTH.toFloat())
                doSldLyAnimation(0, -sldLyW, 200, false)
            }
        }
        return true
    }

    override fun push2BackLsnStack(obkLsnHd: BackLsnHolder) {
        backLsnStack?.push(obkLsnHd)
    }

    override fun popBackLsnStack(reqCod: Int, bkLsn: OnBackListener):BackLsnHolder?{
        if (!(backLsnStack?.isEmpty()?:true)) {
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
                        if (null == list) list = ArrayList<MsgEnt>()
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
            val adp = memListVw?.adapter as MemLsAdapter
            adp?.notifyDataSetChanged()
        }
    }
    private fun doReceivedFilePrgrs(id: Long?,ip: String?,per: Long?){
        val bkStkNum = fragmentManager.backStackEntryCount
        if (bkStkNum > 0 && ip == msgLsFrgm?.citem?.ip) {
            msgLsFrgm?.loadFilePrgrs(id,ip,per)
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

    override fun onDestroy() {
        super.onDestroy()
    }
    companion object {
        private val TAG = "MsgActivity"
        private val SLD_MENU_WIDTH = 230
        /*back key*/
        private val BK_KEY_SLDLY = 1
        private val BK_KEY_MSG_SEND_SLDMENU = 2
        /*What*/
        private val WHAT_MEMLS_LOAD = 1
    }
}