package com.fxqyem.msg.vw

import android.content.Context
import android.graphics.Bitmap
import android.util.Pair
import android.view.*
import android.widget.*
import org.jetbrains.anko.*
import java.io.File
import java.util.Stack
import com.fxqyem.msg.R
import com.fxqyem.msg.act.MsgBaseActivity
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.BackLsnHolder
import com.fxqyem.msg.ben.OnBackListener
import com.fxqyem.msg.utl.AndUtil
import com.fxqyem.msg.utl.BitMapUtil
import com.fxqyem.msg.utl.SDCardUtils

class FileSelector: OnBackListener {
    private lateinit var context: Context
    private var tmpSldMenua: SldMenu? = null
    private var listView: ListView? = null
    private var contentView: View? = null

    private var onSelectOkListener: OnSelectOkListener? = null
    private var mAdapter: FileSelectorAdapter? = null

    private var basePath: String=""
    private var curPath: String=""
    private var curFile: String? = null
    private var curFileo: File? = null
    private var fixed: Array<String>? = null
    private val scrollStack: Stack<Pair<Int,Int>> = Stack()
    /*params*/
    private var mtype = TYPE_FILE
    private var stype = TYPE_NO_STARTP

    private constructor()

    constructor(context: Context,type: Int){
        this.context = context
        mtype = type and 0x0F
        stype = type and 0xF0
    }

    fun open(basePath: String, fixed: Array<String>?) {
        this.basePath = basePath
        this.fixed = fixed
        contentView = genFileSelectorLay()
        val upLvBtn = contentView?.findViewById(R.id.file_slector_holder_uplvBtn) as Button?
        upLvBtn?.setOnClickListener(BtnLsn(-1))
        val okBtn = contentView?.findViewById(R.id.file_slector_holder_okBtn) as Button?
        okBtn?.setOnClickListener(BtnLsn(-1))
        listView = contentView?.findViewById(R.id.file_slector_holder_scrVw) as ListView?

        curPath = basePath
        val list = getFileLs(curPath)
        mAdapter = FileSelectorAdapter(list)
        listView?.adapter = mAdapter
        this.tmpSldMenua = SldMenu.create(context, contentView, null)
        this.tmpSldMenua?.setOnStateChangeListener(object: SldMenu.OnStateChangeListener{
            override fun onStateChange(state: Int) {
                 val mact = context as MsgBaseActivity
                if (state == SldMenu.MENU_STATE_GONE_START) {
                    mact.popBackLsnStack(1, this@FileSelector)
                } else if (state == SldMenu.MENU_STATE_SHOW) {
                    mact.push2BackLsnStack(BackLsnHolder(1, this@FileSelector))
                } else if (state == SldMenu.MENU_STATE_GONE) {
                    tmpSldMenua = null
                }
            }
        })


        tmpSldMenua?.show(SldMenu.SHOW_TYPE_BOTTOM)
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
        for(f in fss) {
            if(TYPE_NO_STARTP == stype && f.name.startsWith("."))continue
            if (f.isDirectory) {
                dls.add(f)
            } else if(TYPE_DIR != mtype) {
                var b = true
                fixed?.map{
                    if (!(f.name.endsWith(it))) {
                        b = false
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


    inner class FileSelectorAdapter: BaseAdapter {
        var fsLs: List<File>?

        constructor(fsLs: List<File>?){
            this.fsLs = fsLs
        }

        private var viewHd: ThisViewHd? = null

        override fun getCount(): Int {
            return fsLs?.size?:0
        }

        override fun getItem(inx: Int): Any? {
            return fsLs?.get(inx)
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
                viewHd?.ficon = fIcon
                viewHd?.fname = fName
                convertView.tag = viewHd
            } else {
                viewHd = convertView.tag as ThisViewHd
                fIcon = viewHd?.ficon
                fName = viewHd?.fname
            }
            val cf = fsLs?.get(position)?:return convertView
            fName?.text = cf.name
            val prtly = fIcon?.parent as LinearLayout
            prtly?.setOnClickListener(BtnLsn(position))

            if (cf.isDirectory) {
                fIcon?.setImageResource(R.mipmap.folder_default)
            } else {
                val fbol = (fixed?.contains(".jpg")?:false)||(fixed?.contains(".png")?:false)
                        ||(fixed?.contains(".gif")?:false)
                if(fbol){
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
                }else{
                    fIcon?.setImageResource(R.mipmap.red_thm_42)
                }

            }

            return convertView
        }

    }

    internal inner class ThisViewHd {
        var ficon: ImageView? = null
        var fname: TextView? = null
    }

    interface OnSelectOkListener {
        fun onSelectOk(fpath: String?,file: File?)
    }

    internal inner class BtnLsn(private val pos: Int) : View.OnClickListener {

        override fun onClick(v: View) {
            val vid = v.id
            val adp = listView?.adapter as FileSelectorAdapter?
            adp?:return
            when (vid) {
                R.id.file_slector_item_hdly -> {
                    if(pos>adp.fsLs?.size?:0)return
                    val cf = adp.fsLs?.get(pos)?:return
                    if (cf.isDirectory) {
                        val indx = listView?.firstVisiblePosition?:0
                        val v = listView?.getChildAt(0)
                        val top = v?.top?:0
                        scrollStack.add(Pair(indx,top))
                        curPath = cf.absolutePath
                        adp.fsLs = getFileLs(curPath) ?: return
                        setTitPath(curPath)
                        setTitName(getResString(context,R.string.file_slector_holder_chsFile))
                        curFile = null
                        curFileo = null
                        adp?.notifyDataSetChanged()
                        listView?.setSelectionFromTop(0, 0)
                    } else {
                        setTitName(cf.name)
                        curFile = cf.absolutePath
                        curFileo = cf
                    }
                }
                R.id.file_slector_holder_uplvBtn -> {
                    val df = File(curPath!!)
                    if ("/" == df.absolutePath) {
                        return
                    }
                    curPath = df.parentFile.absolutePath
                    adp.fsLs = getFileLs(curPath) ?: return

                    setTitPath(curPath)
                    setTitName(getResString(context,R.string.file_slector_holder_chsFile))
                    curFile = null
                    curFileo = null
                    val pair=if(!scrollStack.isEmpty())scrollStack.pop() else null
                    adp?.notifyDataSetChanged()
                    listView?.setSelectionFromTop(pair?.first?:0, pair?.second?:0)
                }
                R.id.file_slector_holder_okBtn -> {
                    var cpath: String? = null
                    when(mtype) {
                        TYPE_FILE ->{
                            if (utilIsEmpty(curFile)) {
                                Toast.makeText(context, getResString(context, R.string.file_slector_holder_nofileseleted), Toast.LENGTH_SHORT).show()
                                return
                            }
                            cpath = curFile
                        }
                        TYPE_DIR -> {
                            cpath = curPath
                        }
                        TYPE_ALL -> {
                            cpath = curFile?:curPath
                        }
                    }
                    if (onSelectOkListener != null) {
                        onSelectOkListener?.onSelectOk(cpath,curFileo)
                    }
                    cancle()
                }
            }
        }
    }


    override fun onBackKeyUp(keyCode: Int, event: KeyEvent, reqCod: Int): Boolean {
        tmpSldMenua?.cancle()
        return false
    }

    fun setOnSelectOkListener(onSelectOkListener: OnSelectOkListener) {
        this.onSelectOkListener = onSelectOkListener
    }

    private fun genFileSelectorLay(): View{
        val ankoCtx = context.UI{

            linearLayout {
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(R.drawable.opt_menu_ctn)

                textView {
                    id= R.id.file_slector_holder_tit
                    backgroundResource = R.drawable.opt_menu_tit
                    maxLines = 1
                    textColor = COLOR_LIGHTGREEN
                    textSize = 14f
                    text = getResString(context,R.string.file_slector_holder_chsFile)
                    gravity = Gravity.CENTER

                }.lparams {
                    width= matchParent
                    height=dip(38)

                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    padding = dip(5)

                    textView {
                        maxLines = 1
                        textColor = 0xff888888.toInt()
                        textSize = 12f
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
                        textColor = 0xff888888.toInt()
                        textSize = 12f

                    }.lparams{
                        width= wrapContent
                        height= wrapContent
                        setHorizontalGravity(Gravity.LEFT)
                        setVerticalGravity(Gravity.CENTER_VERTICAL)
                    }

                }.lparams{
                    width= matchParent
                    height=dip(26)


                }
                listView {
                    id=R.id.file_slector_holder_scrVw
                    dividerHeight = dip(1)
                    divider= getResDrawable(ctx,R.drawable.ls_divi_bkg)
                    isFocusable = false
                    isFocusableInTouchMode = false
                    cacheColorHint = COLOR_TRANS
                }.lparams{
                    width= matchParent
                    height= dip(220)

                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    setGravity(Gravity.CENTER)
                    padding=dip(7)

                    button{
                        id=R.id.file_slector_holder_uplvBtn
                        backgroundResource = R.drawable.opt_menu_mbtn
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        textColor = COLOR_LIGHTGREEN
                        textSize = 16f
                        text = getResString(context,R.string.file_slector_holder_uplevel)
                        padding = 0

                    }.lparams{
                        width = dip(120)
                        height=dip(30)


                    }

                    button{
                        id=R.id.file_slector_holder_okBtn
                        backgroundResource = R.drawable.opt_menu_mbtn
                        textColor = COLOR_LIGHTGREEN
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
        val itmVw = context.linearLayout {
                id = R.id.file_slector_item_hdly
                orientation = LinearLayout.HORIZONTAL
                descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                backgroundResource = R.drawable.ls_itm_bkg
                padding = dip(6)

                imageView {
                    id=R.id.file_slector_item_ico
                    backgroundColor = 0xffffffff.toInt()

                }.lparams{
                    width = dip(30)
                    height = dip(30)

                }

                textView{
                    id=R.id.file_slector_item_fname
                    textColor = 0xff444444.toInt()
                    textSize = 14f
                    gravity = Gravity.CENTER_VERTICAL

                }.lparams{
                    width = matchParent
                    height = dip(30)
                    leftMargin = dip(5)

                }

            }
        return itmVw
    }

    companion object {
        val TYPE_FILE = 0x00
        val TYPE_DIR = 0x01
        val TYPE_ALL = 0x02
        val TYPE_NO_STARTP = 0x10
    }
}
