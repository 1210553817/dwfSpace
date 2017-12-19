package com.fxqyem.msg.act

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.view.*
import android.widget.*
import com.fxqyem.msg.R
import com.fxqyem.msg.adp.MemLsAdapter
import com.fxqyem.msg.adp.MemMsgLsAdapter
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ben.OnBackListener
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.lay.MsgSendLay
import com.fxqyem.msg.utl.DbUtil
import com.fxqyem.msg.vw.utilNotNull
import org.jetbrains.anko.onClick
import android.widget.AbsListView
import com.fxqyem.msg.utl.SDCardUtils
import com.fxqyem.msg.vw.FileSelector
import com.fxqyem.msg.vw.utilIsEmpty
import java.io.File


class MsgSendFrgm : Fragment() , OnBackListener {
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    var memMsgLsView: ListView? = null
    var citem: MsgEnt? = null
    private var poffset = 0
    private var plimit = 10
    private var pmoreld = true
    private var pmorelmt = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = initViews()

    private fun initViews(): View? {
       // Log.d(TAG,"MsgSendFrgm initViews..........")
        citem = arguments.getSerializable("ITEM") as MsgEnt
        AppContext.instance?.clearChat(citem?.ip)
        val hdvw = MsgSendLay().createView(activity)
        val tit = hdvw.findViewById(R.id.msg_send_lay_tit) as TextView
        memMsgLsView = hdvw.findViewById(R.id.msg_send_lay_msgls) as ListView
        val backBtn = hdvw.findViewById(R.id.msg_send_lay_backBtn) as ImageButton
        //val optBtn = hdvw.findViewById(R.id.msg_send_lay_optBtn) as ImageButton
        val sendBtn = hdvw.findViewById(R.id.msg_send_lay_sendBtn) as Button
        val attaBtn = hdvw.findViewById(R.id.msg_send_lay_attaBtn) as Button
        val msgTxt = hdvw.findViewById(R.id.msg_send_lay_editTxt) as EditText
        tit.text = citem?.add
        backBtn.onClick {
            this@MsgSendFrgm.fragmentManager.popBackStack()
        }
        sendBtn.onClick {
            val msgtxt = msgTxt.text.toString()
            if(utilNotNull(msgtxt)) {
                val itt = Intent(AppConstants.ACTION_SER_SEND_MSG)
                itt.putExtra("IP", citem?.ip)
                itt.putExtra("TXT", msgtxt)
                mLocalBroadcastManager?.sendBroadcast(itt)
                msgTxt.setText("".toCharArray(),0,0)
            }
        }
        attaBtn.onClick {
            openFileSelector()
        }

        initListView()

        return hdvw
    }

    private fun initListView(){
        val db = DbUtil(this.activity)
        val list = db.getMsgls(citem?.ip,poffset,plimit)
        val msgLsAdapter = MemMsgLsAdapter(activity,list)
        memMsgLsView?.adapter = msgLsAdapter
        memMsgLsView?.setSelectionFromTop(list.size-1,0)
        memMsgLsView?.setOnScrollListener(object : AbsListView.OnScrollListener {
            private var scrtp = 0
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                when (scrollState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE ->{
                        if(scrtp>0){
                            if(scrtp>1) loadListMore()
                            scrtp++
                        }
                    }
                }

            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem == 0) {
                    val  fitm = view.getChildAt(0)
                    if(fitm!=null && fitm?.top == 0){
                        scrtp = 1
                    }else{
                        scrtp=0
                    }
                }else if (visibleItemCount + firstVisibleItem == totalItemCount) {

                }
                if(firstVisibleItem>0){
                    scrtp=0
                }
            }
        })
    }

    private fun loadListMore(){
        if(!pmoreld)return
        val db = DbUtil(this.activity)
        poffset += plimit
        plimit = pmorelmt
        val list = db.getMsgls(citem?.ip,poffset,plimit)
        if(list.isEmpty()){
            pmoreld = false
        }else {
            if(list.size<plimit)pmoreld = false
            val adp = memMsgLsView?.adapter as MemMsgLsAdapter?
            adp?.load2Btm = 0
            adp?.list?.addAll(0, list)
            adp?.notifyDataSetChanged()
        }
    }
    fun loadMsgItem(msg: MsgEnt){
        val ip = msg.ip?:return
        if (ip == citem?.ip) {
            val msgAdp = memMsgLsView?.adapter as MemMsgLsAdapter?
            var list = msgAdp?.list
            if (null == list) {
                list = ArrayList()
            }
            list.add(msg)
            msgAdp?.load2Btm = 1
            msgAdp?.list = list
            msgAdp?.notifyDataSetChanged()
            memMsgLsView?.setSelectionFromTop(list.size-1,0)
            AppContext.instance?.clearChat(citem?.ip)
        }
    }

    /*文件进度条*/
    fun loadFilePrgrs(id: Long?,ip: String?,per: Long?){
        id?:return
        ip?:return
        per?:return
        if (ip == citem?.ip) {
            val msgAdp = memMsgLsView?.adapter as MemMsgLsAdapter?
            msgAdp?.updateFileItem(id,memMsgLsView,per)
        }
    }

    private fun openFileSelector(){
        val fileSelector = FileSelector(activity,FileSelector.TYPE_FILE or FileSelector.TYPE_NO_STARTP)
        val sdpth = SDCardUtils.sdCardPath
        fileSelector.open(if (utilIsEmpty(sdpth)) "/" else sdpth, null)
        fileSelector.setOnSelectOkListener(object: FileSelector.OnSelectOkListener{
            override fun onSelectOk(fpath: String?,file: File?) {
                val fbol = file?.exists()?:false
                if(fbol&&fpath!=null) {
                    val itt = Intent(AppConstants.ACTION_SER_SEND_FILE)
                    itt.putExtra("IP", citem?.ip)
                    itt.putExtra("FPATH", fpath)
                    itt.putExtra("FNAME", file?.name)
                    mLocalBroadcastManager?.sendBroadcast(itt)
                }
            }
        })
    }

    override fun onBackKeyUp(reqValue: Int, event: KeyEvent, reqCod: Int): Boolean {
//        when (reqCod) {
//
//        }
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        AppContext.instance?.clearChat(citem?.ip)
        val act = this.activity as MsgActivity
        val memAdp = act.memListVw?.adapter as MemLsAdapter
        memAdp.notifyDataSetChanged()
    }
    companion object {
        private val TAG = "MsgSendFrgm"
    }
}
