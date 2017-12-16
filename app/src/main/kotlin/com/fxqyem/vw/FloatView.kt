package com.fxqyem.vw

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter

class FloatView : AdapterView<BaseAdapter> {
    private var mAdapter: BaseAdapter? = null
    private var adjLeft: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)   //获取ViewGroup宽度
        val widthMod = View.MeasureSpec.getMode(widthMeasureSpec)
        setMeasuredDimension(width, getMeHeight(width, widthMod))
    }


    private fun getMeHeight(mWt: Int, wMod: Int): Int {
        var px = 0
        var py = 0
        var maxHt = 0
        var maxMag = 0

        val childCount = childCount
        for (i in 0 until childCount) {
            val cVw = getChildAt(i)
            val cWt = cVw.measuredWidth
            val cHt = cVw.measuredHeight

            val margins = cVw.layoutParams as FloatView.LayoutParams
            if (px + cWt + margins.leftMargin + margins.rightMargin >= mWt) {
                val offr = mWt - px
                val tmpadhleft = (offr + margins.rightMargin - margins.leftMargin) / 2
                if (tmpadhleft != 0) adjLeft = tmpadhleft
                px = 0
                py += maxHt + maxMag
                maxHt = 0
                maxMag = 0
            }
            px += cWt + margins.leftMargin + margins.rightMargin
            if (maxHt < cHt) maxHt = cHt
            if (maxMag < margins.topMargin + margins.bottomMargin)
                maxMag = margins.topMargin + margins.bottomMargin
        }
        val pht = py + maxHt + maxMag
        return pht
    }


    override fun onLayout(chg: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // Log.d(TAG, "changed = "+chg+", left = "+left+", top = "+top+", right = "+right+", botom = "+bottom);
        val mWt = measuredWidth
        val mHt = measuredHeight
        var px = left + adjLeft
        var py = top
        var maxHt = 0
        var maxMag = 0

        val childCount = childCount
        //Log.d(TAG, "mWt = "+mWt+", childCount= "+childCount);
        for (i in 0 until childCount) {
            val cVw = getChildAt(i)
            val cWt = cVw.measuredWidth
            val cHt = cVw.measuredHeight

            val margins = cVw.layoutParams as FloatView.LayoutParams
            if (px + cWt + margins.leftMargin + margins.rightMargin >= mWt) {
                //Log.d(TAG, "adjLeft = "+adjLeft);
                px = left + adjLeft
                py += maxHt + maxMag
                maxHt = 0
                maxMag = 0
            }

            val posx = px + margins.leftMargin
            val posy = py + margins.topMargin
            cVw.layout(posx, posy, posx + cWt, posy + cHt)

            px += cWt + margins.leftMargin + margins.rightMargin
            if (maxHt < cHt) maxHt = cHt
            if (maxMag < margins.topMargin + margins.bottomMargin)
                maxMag = margins.topMargin + margins.bottomMargin
        }
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {
        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {}

        constructor(width: Int, height: Int) : super(width, height) {}
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return FloatView.LayoutParams(context, attrs)
    }


    override fun getAdapter(): BaseAdapter? {
        return mAdapter
    }

    override fun setAdapter(adapter: BaseAdapter?) {
        adapter?:return
        removeViewsInLayout(0, childCount)
        this.mAdapter = adapter
        for (i in 0 until adapter.count) {
            val child = mAdapter!!.getView(i, null, this)
            addViewInLayout(child, i, child.layoutParams)
        }
        requestLayout()
    }

    override fun getSelectedView(): View? {
        return null
    }

    override fun setSelection(position: Int) {}

    companion object {
        private val TAG = "FloatView"
    }


}
