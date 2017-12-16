package com.fxqyem.adp

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fxqyem.R
import org.jetbrains.anko.*

class LrcLsAdapter: BaseAdapter{
    private val context: Context
    var lrcMsg: Array<String>?=null
    var curIndx=-1
    var fcsIndx=-1

    constructor(ctx: Context) {
        context =ctx
    }

    override fun getCount(): Int {
        return lrcMsg?.size?:0
    }

    override fun getItem(indx: Int): String? {
        return lrcMsg?.get(indx)
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, cvertVw: View?, arg2: ViewGroup): View {
        var convertView = cvertVw
        val mTextTitle: TextView?
        if (convertView == null) {
            convertView = createLrcItemView(context)
            mTextTitle = convertView?.findViewById(R.id.main_frm_lrcitm) as TextView?
            convertView.tag = mTextTitle
        } else {
            mTextTitle = convertView.tag as TextView
        }
        mTextTitle?.text = lrcMsg?.get(position)
        if(curIndx==position){
            mTextTitle?.textColor = curColor
        }else if(curIndx-1==position||curIndx+1==position){
            mTextTitle?.textColor = subFcsColor
        }else{
            mTextTitle?.textColor = unFcsColor
        }
        if(fcsIndx==position){
            mTextTitle?.textColor = focusColor
        }
        return convertView
    }


    private fun createLrcItemView(ctx:Context): View {
        return ctx.linearLayout {
            textView{
                id=R.id.main_frm_lrcitm
                gravity = Gravity.CENTER
                textColor = unFcsColor
                textSize =  16f
                setPadding(0,dip(20),0,dip(20))

            }.lparams{
                width= matchParent
                height= wrapContent

            }
        }
    }

    companion object {
        private val TAG = "LrcLsAdapter"
        //colors
        private val curColor = 0xffffffff.toInt()
        private val focusColor = 0xff049ff1.toInt()
        private val subFcsColor = 0xc5cccccc.toInt()
        private val unFcsColor = 0xaaaaaaaa.toInt()
    }
}