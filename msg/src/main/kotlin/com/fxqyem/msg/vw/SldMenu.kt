package com.fxqyem.msg.vw

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.fxqyem.msg.utl.AndUtil

class SldMenu private constructor() {

    private var context: Context? = null
    private var stateCallBack: OnStateChangeListener? = null
    //View
    private var parentView: ViewGroup? = null//父容器
    var contentView: View? = null
        private set//内容
    private var hdVw: View? = null//滑动容器
    private var sldVw: View? = null//滑动体
    //Params
    private var sldVwHt: Int? = 0//滑动体高度
    var state = MENU_STATE_GONE
        private set//菜单状态
    private var showTp: Int = 0//显示类型
    private var sHeight: Int = 0//屏幕高度
    private val hdLyloc = IntArray(2)//容器位置0：x

    private var hasParent: Boolean = false//提供了父容器
    private var aniFlag: Boolean = false//动画进行时标志
    private var measureFlag: Boolean = false//测量进行时标志
    private var showFlag: Boolean = false//show()已调用标志

    /**
     * 展示Menu
     * @param showTp 0：底部；其他：顶部
     */
    fun show(showTp: Int) {
        showFlag = true
        this.showTp = showTp
        if (measureFlag) return
        hdVw!!.setOnTouchListener(MyOnTouchLsn())
        val hdoff = hdLyloc[1]
        if (showTp == SHOW_TYPE_BOTTOM) {
            doMenuAnimation(sHeight, sHeight - hdoff - sldVwHt!!, 500, true)
        } else {
            doMenuAnimation(-sldVwHt!!, 0, 500, true)
        }
    }

    /**
     * 关闭底部/顶部滑动菜单
     */
    fun cancle() {
        showFlag = false
        val params = sldVw!!.layoutParams as FrameLayout.LayoutParams
        val k = params.topMargin
        if (showTp == SHOW_TYPE_BOTTOM) {
            doMenuAnimation(k, sHeight, 400, false)
        } else {
            doMenuAnimation(k, -sldVwHt!!, 400, false)
        }
    }

    /**
     * 打开关闭底部/顶部滑动菜单动画
     * @param begin 位置开始
     * *
     * @param end 位置结束
     * *
     * @param duration 持续时间
     * *
     * @param b false：动画结束移除View
     */
    private fun doMenuAnimation(begin: Int, end: Int, duration: Int, b: Boolean) {
        aniFlag = true
        val params = sldVw?.layoutParams as FrameLayout.LayoutParams
        val mAnimator = ValueAnimator.ofInt(begin, end)
        mAnimator.addUpdateListener { animation ->
            val aniVal = animation.animatedValue as Int
            params.topMargin = aniVal
            sldVw?.layoutParams = params
        }
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                aniFlag = true
                if (!b && state == MENU_STATE_SHOW) {
                    state = MENU_STATE_GONE_START
                    if (stateCallBack != null) {
                        stateCallBack?.onStateChange(state)
                    }
                } else if (b && state == MENU_STATE_GONE) {
                    state = MENU_STATE_SHOW_START
                    if (stateCallBack != null) {
                        stateCallBack?.onStateChange(state)
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                params.topMargin = end
                sldVw?.layoutParams = params
                if (!b && state == MENU_STATE_GONE_START) {
                    removeBtmMenuVw()
                    state = MENU_STATE_GONE
                    if (stateCallBack != null) {
                        stateCallBack?.onStateChange(state)
                    }
                } else if (b && state == MENU_STATE_SHOW_START) {
                    state = MENU_STATE_SHOW
                    if (stateCallBack != null) {
                        stateCallBack?.onStateChange(state)
                    }
                }
                aniFlag = false
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.duration = duration.toLong()
        mAnimator.start()
    }

    /**
     * 移除底部/顶部滑动菜单View
     */
    private fun removeBtmMenuVw() {
        if (hasParent) {
            if (1 == parentView?.childCount) {
                parentView?.visibility = View.GONE
            }
        }
        parentView?.removeView(hdVw)
    }

    /**
     * 打开或关闭状态改变回调借口
     */
    interface OnStateChangeListener {
        fun onStateChange(state: Int)
    }

    private inner class MyOnTouchLsn : View.OnTouchListener {
        private var ny: Float = 0.toFloat()
        private var ny1: Float = 0.toFloat()
        private var ry: Float = 0.toFloat() //y坐标历史和最新y坐标
        private var magtop: Int = 0 //margin top
        private var mvf = false//move flag 移动标志
        private var params: FrameLayout.LayoutParams? = null
        private val reLHt: Int?//滑动部分高度
        private val hdoff: Int?//容器偏移
        private var myFlg = false

        init {
            this.reLHt = sldVwHt
            this.hdoff = hdLyloc[1]
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (aniFlag) {
                myFlg = true
                return myFlg
            }
            if (myFlg) {//拦截重复触发
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    myFlg = false
                    //Log.w("SldMenu$MyOnTouchLsn", "nomeaning touch stoped!");
                }
                return true
            }
            ry = event.rawY.toInt().toFloat()
            if (event.action == MotionEvent.ACTION_DOWN) {
                mvf = false
                if (showTp == 0) {
                    magtop = sHeight - hdoff!! - reLHt!!
                } else {
                    magtop = 0
                }
                params = sldVw!!.layoutParams as FrameLayout.LayoutParams
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                if (mvf) {
                    var b = false
                    if (ny > ny1) {
                        b = true
                    }
                    val endp: Int
                    if (showTp == 0) {
                        endp = if (b) sHeight else sHeight - hdoff!! - reLHt!!
                        b = !b
                    } else {
                        endp = if (b) 0 else -reLHt!!
                    }
                    doMenuAnimation(magtop, endp, 300, b)
                } else {//这里处理类似单击事件
                    val lnup = magtop + hdoff!!
                    val lndn = lnup + reLHt!!
                    if (ry > lndn || ry < lnup) {
                        cancle()
                    }
                }
            } else if (event.action == MotionEvent.ACTION_MOVE && Math.abs(ry - ny) >= 5) {
                mvf = true
                magtop += (ry - ny).toInt()
                if (showTp == 0) {
                    val mxmg = sHeight - hdoff!! - reLHt!!
                    if (magtop <= mxmg) {
                        magtop = mxmg
                    }
                } else {
                    if (magtop >= 0) {
                        magtop = 0
                    }
                }
                if (params != null) {
                    params?.topMargin = magtop
                    sldVw?.layoutParams = params
                }
            }
            ny1 = ny
            ny = ry
            return true
        }
    }

    fun setOnStateChangeListener(stateCallBack: OnStateChangeListener): SldMenu {
        this.stateCallBack = stateCallBack
        return this
    }

    companion object {
        val SHOW_TYPE_BOTTOM = 0//底部显示
        val SHOW_TYPE_TOP = 1//顶部显示

        val MENU_STATE_GONE = 0//关闭
        val MENU_STATE_SHOW = 1//显示
        val MENU_STATE_SHOW_START = 2//显示开始
        val MENU_STATE_GONE_START = 3//关闭开始
        /**
         * 打开底部/顶部滑动菜单
         * @param context 上下文 Activity
         * *
         * @param contentView 菜单内容
         * *
         * @param parentView 父容器，为null时将创建在根View中
         * *
         * @return 返回值为SldMenu
         */

        fun create(context: Context?, contentView: View?, parentView: ViewGroup?): SldMenu? {
            if (context == null || contentView == null) return null
            val thiz = SldMenu()
            thiz.context = context
            thiz.contentView = contentView
            if (parentView != null) thiz.hasParent = true
            thiz.sHeight = AndUtil.getScreenHeight(context)
            val hdVw = VerticalHolderView(context)
            hdVw.isVerticalScrollBarEnabled = false
            val reLyPrms1 = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            hdVw.layoutParams = reLyPrms1
            val frmLy = FrameLayout(context)
            val frmLyPrms = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            frmLyPrms.topMargin = thiz.sHeight
            frmLy.layoutParams = frmLyPrms
            frmLy.addView(contentView)
            hdVw.addView(frmLy)
            thiz.hdVw = hdVw
            thiz.sldVw = frmLy
            if (!thiz.hasParent) {
                val act = context as Activity?
                val vg = act?.window?.decorView as ViewGroup?
                thiz.parentView = vg
            } else {
                thiz.parentView = parentView
                parentView?.visibility = View.VISIBLE
            }

            thiz.measureFlag = true//开始测量
            val vto = thiz.parentView?.viewTreeObserver
            vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                //添加布局展开监听
                override fun onGlobalLayout() {
                    thiz.measureFlag = false
                    thiz.sldVwHt = thiz.sldVw!!.measuredHeight
                    if (0 == thiz.sldVwHt) {
                        thiz.sldVwHt = AndUtil.getViewHeight(thiz.sldVw)
                    }
                    thiz.parentView?.getLocationOnScreen(thiz.hdLyloc)
                    thiz.parentView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    if (thiz.showFlag) {
                        thiz.show(thiz.showTp)
                    }
                }
            })
            thiz.parentView?.addView(hdVw)
            return thiz
        }
    }
}
