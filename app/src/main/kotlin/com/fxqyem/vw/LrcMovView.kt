package com.fxqyem.vw

import android.content.Context
import android.graphics.Matrix
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import com.fxqyem.R
import com.fxqyem.act.MainActivity
import com.fxqyem.adp.LrcLsAdapter
import com.fxqyem.utils.AndUtil
import org.jetbrains.anko.dip


class LrcMovView : ListView {
    private var ctx: Context
    //datas
    private var lrcTim: IntArray? = null
    private var lrcMsg: Array<String>? = null
    //params
    private var curIndx: Int = 0
    private var vHeight: Int = 0
    private var isThisShow: Boolean = false
    //draws
    private var ny = 0
    private var ry = 0
    private var mvf = false
    private var adjRun = false
    private var focusIndx: Int = 0
    private var mAct: MainActivity? = null
    private var indexChgCallBack: OnIndexChangeListener? = null

    private var thizW: Int = 0
    private val myLyloc = IntArray(2)

    constructor(ctx: Context) : super(ctx) {
        this.ctx = ctx
        setThisProp()
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        this.ctx = ctx
        setThisProp()
    }

    private fun setThisProp(){
        this.dividerHeight = 0
        this.cacheColorHint = COLOR_TRANS
        this.isVerticalScrollBarEnabled = false
        isFocusable = false
        isFocusableInTouchMode = false
        selector=getResDrawable(ctx,R.drawable.main_frm_lrcitm)

    }

    fun initViews(lrcTim: IntArray?, lrcMsg: Array<String>?, height: Int, isThisShow: Boolean) {
        this.lrcTim = lrcTim
        this.lrcMsg = lrcMsg
        vHeight = height
        this.isThisShow = isThisShow
        curIndx = 0
        if (lrcMsg == null || lrcTim == null || lrcMsg.isEmpty() || lrcTim.isEmpty()) {
            return
        }
        var thisAdpter = this.adapter as LrcLsAdapter?
        if(null==thisAdpter) {
            thisAdpter = LrcLsAdapter(ctx)
            this.adapter = thisAdpter
            this.setOnItemClickListener { parent, view, position, id ->
                adjRun = false
                indexChgCallBack?.onIndexChange(position, this.lrcTim?.get(position)?:0)

            }
        }
        thisAdpter.lrcMsg = lrcMsg

    }

    fun moveTo(indx: Int) {
        curIndx = indx
        val adp = this.adapter as LrcLsAdapter?
        adp?.curIndx = indx
        adp?.notifyDataSetChanged()
        if(!adjRun) {
            val curp = vHeight/2 - dip(40)
            this.smoothScrollToPositionFromTop(indx, curp,400)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val rx = event.rawX
            this.getLocationOnScreen(myLyloc)
            thizW = this.measuredWidth
            val xa = myLyloc[0] + AndUtil.dp2Px(ctx, 60f)
            val xb = myLyloc[0] + thizW - AndUtil.dp2Px(ctx, 60f)
            if (rx > xa && rx < xb) {
                mAct = ctx as MainActivity?
                mAct?.setFrmLrcVwTchd(true)
            }
        }
        return false
    }

    fun onMyTouchEvent(v: View, event: MotionEvent): Boolean {
        ry = event.rawY.toInt()
        if (event.action == MotionEvent.ACTION_DOWN) {
            mvf = false
            focusIndx = curIndx
            this.getLocationOnScreen(myLyloc)
        } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            if (mvf) {
                mvf = false
                afterSetAdjRun()
            }
        } else if (event.action == MotionEvent.ACTION_MOVE && Math.abs(ry - ny) > 3) {
            mvf = true
            adjRun = true
        }
        ny = ry

        val mtx = Matrix()
        mtx.setTranslate(-myLyloc[0].toFloat(),-myLyloc[1].toFloat())
        event.transform(mtx)
        super.onTouchEvent(event)
        return false
    }


    interface OnIndexChangeListener {
        fun onIndexChange(indx: Int, pos: Int)
    }

    private fun afterSetAdjRun() {
        object : CountDownTimer(1500, 1500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (!mvf){
                    adjRun = false
                }
            }
        }.start()
    }

    fun setOnIndexChangeListener(indexChgCallBack: OnIndexChangeListener) {
        this.indexChgCallBack = indexChgCallBack
    }

    companion object {
        private val TAG = "LrcMovView"
    }
}
