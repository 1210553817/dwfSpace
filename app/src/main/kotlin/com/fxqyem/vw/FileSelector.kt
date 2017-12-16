package com.fxqyem.vw

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.*
import android.widget.*

import com.fxqyem.R
import com.fxqyem.act.MainActivity
import com.fxqyem.bean.AppConstants
import com.fxqyem.bean.BackLsnHolder
import com.fxqyem.bean.OnBackListener
import com.fxqyem.utils.AndUtil
import com.fxqyem.utils.BitMapUtil
import com.fxqyem.utils.SDCardUtils
import org.jetbrains.anko.*


import java.io.File
import java.util.*

class FileSelector(private val context: Context) : OnBackListener {
    private var tmpSldMenua: SldMenu? = null
    private var floatView: FloatView? = null
    private var contentView: View? = null
    private var hdView: ScrollView? = null

    private var onSelectOkListener: OnSelectOkListener? = null
    private var mAdapter: FileSelectorAdapter? = null
    private var list: List<File>? = null


    private var basePath: String=""
    private var curPath: String=""
    private var curFile: String? = null
    private var fixed: Array<String>? = null
    private val scrollStack: Stack<Int> = Stack()

    fun open(basePath: String, fixed: Array<String>) {
        this.basePath = basePath
        this.fixed = fixed
        contentView = genFileSelectorLay()
        val upLvBtn = contentView!!.findViewById(R.id.file_slector_holder_uplvBtn) as Button
        upLvBtn.setOnClickListener(MoreButtonListener(-1))
        val okBtn = contentView!!.findViewById(R.id.file_slector_holder_okBtn) as Button
        okBtn.setOnClickListener(MoreButtonListener(-1))
        hdView = contentView!!.findViewById(R.id.file_slector_holder_scrVw) as ScrollView
        floatView = FloatView(context)
        val fltLyPrmsa = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        floatView!!.layoutParams = fltLyPrmsa
        curPath = basePath
        list = getFileLs(curPath)
        mAdapter = FileSelectorAdapter(list)
        floatView?.adapter = mAdapter
        hdView?.addView(floatView)
        this.tmpSldMenua = SldMenu.create(context, contentView, null)
        this.tmpSldMenua?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
            override fun onStateChange(state: Int) {
                 val mact = context as MainActivity
                if (state == SldMenu.MENU_STATE_GONE_START) {
                    mact.popBackLsnStack(1, this@FileSelector)
                } else if (state == SldMenu.MENU_STATE_SHOW) {
                    mact.push2BackLsnStack(BackLsnHolder(1, this@FileSelector))
                } else if (state == SldMenu.MENU_STATE_GONE) {
                    tmpSldMenua = null
                }
            }
        })


        tmpSldMenua!!.show(SldMenu.SHOW_TYPE_BOTTOM)
        setTitPath(basePath)
    }

    fun cancle() {
        tmpSldMenua?.cancle()
    }

    private fun setTitName(tit: String) {
        val titTv = contentView?.findViewById(R.id.file_slector_holder_tit) as TextView
        titTv.text = tit
    }

    private fun setTitPath(tit: String) {
        val titTv = contentView?.findViewById(R.id.file_slector_holder_path) as TextView
        titTv.text = tit
    }

    private fun getFileLs(fpath: String): List<File>? {
        val fss = File(fpath).listFiles() ?: return null
        val fls = mutableListOf<File>()
        val dls = mutableListOf<File>()
        for (f in fss) {
            if (f.isDirectory) {
                dls.add(f)
            } else {
                var b = false
                fixed?.map{
                    if (f.name.endsWith(it)) {
                        b = true
                    }
                }
                if (b) {
                    fls.add(f)
                }
            }
        }
        dls.sortBy { it.name.toLowerCase() }
        fls.sortBy { it.name.toLowerCase() }
        dls.addAll(fls)
        return dls
    }


    inner class FileSelectorAdapter(private var fsLs: List<File>?) : BaseAdapter() {
        private var viewHd: ThisViewHd? = null

        override fun getCount(): Int {
            if (fsLs == null) return 0
            return fsLs!!.size
        }

        override fun getItem(arg0: Int): Any? {
            if (fsLs == null) return null
            return fsLs!![arg0]
        }

        override fun getItemId(arg0: Int): Long {
            return arg0.toLong()
        }

        override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
            var convertView = convertView
            val fIcon: ImageView?
            val fName: TextView?
            if (convertView == null) {
                convertView = genFileSelectorItemLay()
                fIcon = convertView?.findViewById(R.id.file_slector_item_ico) as ImageView?
                fName = convertView?.findViewById(R.id.file_slector_item_fname) as TextView?
                viewHd = ThisViewHd()
                viewHd!!.ficon = fIcon
                viewHd!!.fname = fName
                convertView.tag = viewHd
            } else {
                viewHd = convertView.tag as ThisViewHd
                fIcon = viewHd!!.ficon
                fName = viewHd!!.fname
            }

            val cf = fsLs!![position]
            fName?.text = cf.name
            if (cf.isDirectory) {
                fIcon?.setImageResource(R.mipmap.folder_default)
            } else {
                val dpw = context.dip(40)
                val dph = context.dip(30)
                val fIconprms = fIcon?.layoutParams as LinearLayout.LayoutParams
                fIconprms.width = dpw
                fIconprms.height = dph
                val fNameprms = fName?.layoutParams as LinearLayout.LayoutParams
                fNameprms.width = dpw
                //fIcon?.setImageResource(R.mipmap.pic_default)

                doAsync {

                    val md5fnm = AndUtil.MD5(cf.absolutePath)
                    val tmppth = SDCardUtils.sdCardPath + "/" + AppConstants.APP_TMP_PIC_PATH
                    val pfl = File(tmppth + "/" + md5fnm)
                    val bitmp: Bitmap?
                    if (pfl.exists()) {
                        bitmp = BitMapUtil.getBitmapFromFile(pfl.absolutePath)
                    } else {
                        bitmp = BitMapUtil.convertToBitmap(cf.absolutePath, dpw, dph)
                        BitMapUtil.saveBitmap(bitmp, tmppth, md5fnm)
                    }

                    uiThread {
                        fIcon?.setImageBitmap(bitmp)
                    }
                }

            }
            fIcon?.setOnClickListener(MoreButtonListener(position))

            val lyprms = FloatView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val mag = AndUtil.dp2Px(context, 8f)
            lyprms.setMargins(mag, mag, mag, mag)
            convertView.layoutParams = lyprms
            return convertView
        }

        fun setFsLs(fsLs: List<File>) {
            this.fsLs = fsLs
        }
    }

    internal inner class ThisViewHd {
        var ficon: ImageView? = null
        var fname: TextView? = null
    }

    interface OnSelectOkListener {
        fun onSelectOk(fpath: String?)
    }

    internal inner class MoreButtonListener(private val pos: Int) : View.OnClickListener {

        override fun onClick(v: View) {
            val vid = v.id
            when (vid) {
                R.id.file_slector_item_ico -> {
                    val cf = list!![pos]
                    if (cf.isDirectory) {
                        scrollStack.add(hdView?.scrollY)
                        curPath = cf.absolutePath
                        val tmplist = getFileLs(curPath) ?: return
                        list = tmplist
                        floatView?.adapter = FileSelectorAdapter(list)
                        setTitPath(curPath)
                        setTitName(getResString(context,R.string.file_slector_holder_chsFile))
                        curFile = null
                        hdView?.onLayoutChange { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                            v?.scrollTo(0,0)
                        }
                    } else {
                        setTitName(cf.name)
                        curFile = cf.absolutePath
                    }
                }
                R.id.file_slector_holder_uplvBtn -> {
                    val df = File(curPath!!)
                    if ("/" == df.absolutePath) {
                        return
                    }
                    curPath = df.parentFile.absolutePath
                    val tmplist = getFileLs(curPath) ?: return
                    list = tmplist
                    floatView?.adapter = FileSelectorAdapter(list)
                    setTitPath(curPath)
                    setTitName(getResString(context,R.string.file_slector_holder_chsFile))
                    curFile = null
                    val scry=if(scrollStack.size>0)scrollStack.pop()else 0
                    hdView?.onLayoutChange { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                        v?.scrollTo(0,scry)
                    }
                }
                R.id.file_slector_holder_okBtn -> {

                    if (utilIsEmpty(curFile)) {
                        Toast.makeText(context, getResString(context,R.string.file_slector_holder_nofileseleted), Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (onSelectOkListener != null) {
                        onSelectOkListener?.onSelectOk(curFile)
                    }
                    cancle()
                }
            }
        }
    }


    override fun onBackKeyUp(keyCode: Int, event: KeyEvent, reqCod: Int): Boolean {
        tmpSldMenua!!.cancle()
        return false
    }

    fun setOnSelectOkListener(onSelectOkListener: OnSelectOkListener) {
        this.onSelectOkListener = onSelectOkListener
    }

    private fun genFileSelectorLay(): View{
        val ankoCtx = context.UI{

            linearLayout {
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(R.drawable.hm_song_opt_menu_ctn)

                textView {
                    id=R.id.file_slector_holder_tit
                    backgroundResource = R.drawable.hm_song_opt_menu_tit
                    maxLines = 1
                    textColor = 0xff049ff1.toInt()
                    textSize = 16f
                    text = getResString(context,R.string.file_slector_holder_chsFile)
                    gravity = Gravity.CENTER

                }.lparams {
                    width= matchParent
                    height=dip(40)

                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    padding = dip(7)

                    textView {
                        maxLines = 1
                        textColor = 0xff888888.toInt()
                        textSize = 14f
                        text = getResString(context,R.string.file_slector_holder_curPath)

                    }.lparams{
                        width= wrapContent
                        height= wrapContent
                        setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
                        setVerticalGravity(Gravity.CENTER_VERTICAL)
                    }

                    textView {
                        id=R.id.file_slector_holder_path
                        maxLines = 2
                        textColor = 0xff666666.toInt()
                        textSize = 12f

                    }.lparams{
                        width= wrapContent
                        height= wrapContent
                        setHorizontalGravity(Gravity.LEFT)
                        setVerticalGravity(Gravity.CENTER_VERTICAL)
                    }

                }.lparams{
                    width= matchParent
                    height=dip(30)


                }

                scrollView {
                    id=R.id.file_slector_holder_scrVw


                }.lparams{
                    width= wrapContent
                    height=dip(220)
                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    setGravity(Gravity.CENTER)
                    padding=dip(7)

                    button{
                        id=R.id.file_slector_holder_uplvBtn
                        backgroundResource = R.drawable.hm_song_opt_menu_mem_btn
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        textColor = 0xaa049ff1.toInt()
                        textSize = 16f
                        text = getResString(context,R.string.file_slector_holder_uplevel)
                        padding = 0

                    }.lparams{
                        width = dip(120)
                        height=dip(30)


                    }

                    button{
                        id=R.id.file_slector_holder_okBtn
                        backgroundResource = R.drawable.hm_song_opt_menu_mem_btn
                        textColor = 0xaa049ff1.toInt()
                        textSize = 16f
                        text = getResString(context,R.string.file_slector_holder_confirm)
                        padding = 0

                    }.lparams{
                        width = dip(120)
                        height=dip(30)
                        leftMargin=dip(10)


                    }

                }.lparams{
                    width= matchParent
                    height= wrapContent

                }

            }
        }
        val reVw = ankoCtx.view
        return reVw
    }
    private fun genFileSelectorItemLay(): View{
        val ankoCtx = context.UI{
            linearLayout {
                orientation = LinearLayout.VERTICAL
                padding = dip(5)

                imageView {
                    id=R.id.file_slector_item_ico
                    backgroundColor = 0xffffffff.toInt()

                }.lparams{
                    width = dip(40)
                    height = dip(30)

                }

                textView{
                    id=R.id.file_slector_item_fname
                    maxLines = 2
                    textColor = 0xff444444.toInt()
                    textSize = 12f
                    gravity = Gravity.CENTER

                }.lparams{
                    width = dip(40)
                    height = wrapContent
                }

            }
        }
        val reVw = ankoCtx.view
        return reVw
    }
}
