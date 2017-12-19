package com.fxqyem.msg.ser

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.fxqyem.msg.R
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ent.AttaEnt
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.utl.DbUtil
import com.fxqyem.msg.utl.NetUtils
import com.fxqyem.msg.vw.getResString
import com.fxqyem.msg.vw.utilHex2Long
import com.fxqyem.msg.vw.utilStr2Long
import java.nio.charset.Charset
import java.net.*
import java.io.*


class MsgService : Service(){

    //private var reHandler: ReHandler? = null
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mReceiver: BroadcastReceiver? = null

    /*Server Socket */
    var msgServer: DatagramSocket? = null

    override fun onCreate() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        //reHandler = ReHandler()
        mReceiver = MyBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction(AppConstants.ACTION_SER_LOAD_MEM_ITEMS)
        filter.addAction(AppConstants.ACTION_SER_SEND_MSG)
        filter.addAction(AppConstants.ACTION_SER_GET_FILE)
        filter.addAction(AppConstants.ACTION_SER_SEND_FILE)
        filter.addAction(AppConstants.ACTION_SER_ACT_RESUME)
        filter.addAction(AppConstants.ACTION_SER_ACT_STOP)
        mLocalBroadcastManager?.registerReceiver(mReceiver, filter)
        initSocket()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) = super.onStartCommand(intent, flags, startId)

    override fun onDestroy() {
        super.onDestroy()
        mLocalBroadcastManager?.unregisterReceiver(mReceiver)
        msgServer?.disconnect()
        msgServer?.close()
        msgServer=null
    }

//    private inner class ReHandler : Handler() {
//        override fun handleMessage(msg: Message) {
//            val bun = msg.data
//            when (msg.what) {
//                AppConstants.WHAT_LOAD_MEM_ITEMS -> {
//                    sendOnlineMsg()
//                }
//                AppConstants.WHAT_SEND_MSG -> {
//                    val ip = bun.getString("IP")
//                    val txt = bun.getString("TXT")
//                    val msg = MsgEnt("CUIHW_PC", "CUIHW_PC2016120516235600", (AppConstants.MSG_SENDMSG), txt)
//                    msg.type = 1
//                    msg.ip = ip
//                    loadMemMsg(msg)
//                    sendMsg(msg)
//
//                }
//
//                AppConstants.WHAT_ON_ACT_RESUME ->{
//                    onActResume()
//                }
//                AppConstants.WHAT_ON_ACT_STOP ->{
//                    onActStop()
//                }
//            }
//        }
//    }

    private inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bun: Bundle? = intent.extras
            when(intent.action){
                AppConstants.ACTION_SER_LOAD_MEM_ITEMS -> {
                    sendOnlineMsg()
                }
                AppConstants.ACTION_SER_SEND_MSG -> {
                    val ip = bun?.getString("IP")
                    val txt = bun?.getString("TXT")
                    val msg = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_SENDMSG), txt)
                    msg.type = 1
                    msg.ip = ip
                    loadMemMsg(msg)
                    sendMsg(msg)
                }
                AppConstants.ACTION_SER_GET_FILE -> {
                    val id = bun?.getLong("ID")
                    val ip = bun?.getString("IP")
                    val pno = bun?.getString("PNO")
                    val fno = bun?.getString("FNO")
                    val fnm = bun?.getString("FNM")
                    val fsz = bun?.getString("FSZ")
                    if(ip==null||pno==null||fno==null||fnm==null){
                        loadAlert(getResString(this@MsgService,R.string.no_allInfo_canNotRcvFile))
                    }else{
                        val pnol = utilStr2Long(pno)
                        if(pnol==null){
                            loadAlert(getResString(this@MsgService,R.string.err_pno_canNotRcvFile))
                        }else {
                            rcvTcpFile(id,ip, pnol, fno, fnm, utilHex2Long(fsz)?:0,{
                                idd,ipp,perr ->
                                loadFilePrgrsMsg(idd,ipp,perr)

                            })
                        }
                    }
                }
                AppConstants.ACTION_SER_SEND_FILE -> {
                    val ip = bun?.getString("IP")
                    val fpath = bun?.getString("FPATH")
                    val fname = bun?.getString("FNAME")
                    val msg = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_SENDMSG), fname)
                    msg.type = 1
                    msg.mtype = 2
                    msg.ip = ip
                    loadMemMsg(msg)
                    val id = msg.id
                    val file = File(fpath)
                    if(ip==null||!file.exists()){
                        loadAlert(getResString(this@MsgService,R.string.no_allInfo_canNotSendFile))
                    }else {
                        sendTcpFile(id,ip, file, { _, _ ->
                            true
                        }, { idd,ipp,perr ->
                            loadFilePrgrsMsg(idd,ipp,perr)

                        })
                    }
                }
                AppConstants.ACTION_SER_ACT_RESUME -> {
                    onActResume()
                }
                AppConstants.ACTION_SER_ACT_STOP -> {
                    onActStop()
                }


            }
        }
    }


    private fun loadMemItems(msg: MsgEnt){
        val itt = Intent(AppConstants.ACTION_LOAD_MEM_ITEMS)
        itt.putExtra("MSGENT", msg)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }

    private fun loadMemMsg(msg: MsgEnt){
        synchronized(this) {
            if(msg.type==0)AppContext.instance?.addChat(msg.ip)
            val db = DbUtil(this)
            db.add2MsgLs(msg)
            val itt = Intent(AppConstants.ACTION_RECEIVE_MSG)
            itt.putExtra("MSG", msg)
            mLocalBroadcastManager?.sendBroadcast(itt)
        }
    }
    private fun loadFilePrgrsMsg(id: Long?,ip: String?,per: Long?){
        val itt = Intent(AppConstants.ACTION_FILE_PRGRS_MSG)
        itt.putExtra("ID", id)
        itt.putExtra("IP", ip)
        itt.putExtra("PER", per)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }

    private fun loadAlert(msg: String){
        val itt = Intent(AppConstants.ACTION_ALERT_MSG)
        itt.putExtra("MSG", msg)
        mLocalBroadcastManager?.sendBroadcast(itt)
    }

    /*network*/
    private fun initSocket(){
        if(!NetUtils.isConnected(this)){
            loadAlert(getResString(this,R.string.no_internet_connection))
            return
        }
        if(null!=msgServer){
            sendOnlineMsg()
            return
        }
        object: Thread() {
            override fun run() {
                msgServer = DatagramSocket(AppConstants.MSG_GLOBAL_PORT)
                sendOnlineMsg()
                /*receive*/
                while(!(msgServer?.isClosed?:true)){
                    val buffer = ByteArray(4096)
                    val packet = DatagramPacket(buffer, buffer.size)
                    try {
                        msgServer?.receive(packet)
                        val msg = String(packet.data, 0, packet.length, Charset.forName("GBK"))
                        //Log.d(TAG, "initSocket receive------>$msg")
                        processMessage(msg, packet.address.hostName, packet.address.hostAddress)


                    }catch (e: Exception){
                        Log.d(TAG,"msgServer closed")
                    }
                }
            }

        }.start()

    }
    private fun processMessage(msg: String?,host: String?,ip: String){
        msg?:return
        val msgEnt = MsgEnt(msg)
        msgEnt.host = host
        msgEnt.ip = ip
        if(msgEnt.useful!=1)return
        val lowCmd = msgEnt.cmd and 0xff
        val optCmd = msgEnt.cmd and 0xff00
        val foptCmd = msgEnt.cmd and 0xff0000
        when(lowCmd){
            AppConstants.MSG_BR_ENTRY -> {
                val wel = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_ANSENTRY), AppContext.instance?.uname)
                wel.ip = ip
                sendMsg(wel)
                msgEnt.type = 0
                loadMemItems(msgEnt)
            }
            AppConstants.MSG_ANSENTRY -> {
                msgEnt.type = 1
                loadMemItems(msgEnt)
            }
            AppConstants.MSG_SENDMSG -> {
                msgEnt.mtype = 0
                if(AppConstants.MSG_SENDCHECKOPT == optCmd){
                    sendCfmMsg(ip,msgEnt.pno)
                }
                if(AppConstants.MSG_FILEATTACHOPT == foptCmd){
                    val attaEnt = AttaEnt(msgEnt.add)
                    msgEnt.add = attaEnt.tit
                    msgEnt.path = attaEnt.id
                    msgEnt.fname = attaEnt.tit
                    msgEnt.fsize = attaEnt.size
                    msgEnt.mtype = 1
                }else{
                    val addarr = msgEnt.add?.toByteArray(Charset.forName("GBK"))
                    if(addarr!=null) {
                        var indx = addarr.indexOf(AppConstants.MSG_GLOBAL_ZEROBYTE)
                        if(indx<0)indx = addarr.size
                        msgEnt.add = String(addarr, 0, indx,Charset.forName("GBK"))
                    }
                }
                msgEnt.type = 0
                loadMemMsg(msgEnt)
            }

        }
    }

    private fun sendOnlineMsg(){
        val wel = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_BR_ENTRY), AppContext.instance?.uname)
        wel.ip = "255.255.255.255"
        sendMsg(wel)
    }

    private fun sendCfmMsg(ip: String,pno: String?){
        val wel = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, AppConstants.IPMSG_RECVMSG,pno)
        wel.ip = ip
        sendMsg(wel)
    }

    private fun sendMsg(msg: MsgEnt){
        if(!NetUtils.isConnected(this)){
            loadAlert(getResString(this,R.string.no_internet_connection))
            msgServer?.close()
            msgServer = null
            return
        }
        msgServer?:return
        object: Thread() {
            override fun run() {
                val addr = InetAddress.getByName(msg.ip)
                val sbuf = msg.genMsgStr().toByteArray(Charset.forName("GBK"))
                val spacket = DatagramPacket(sbuf, sbuf.size, addr, AppConstants.MSG_GLOBAL_PORT)
                try {
                    msgServer?.send(spacket)
                }catch (e: Exception){
                    Log.e(TAG,"sendMsg error!${e.message}")
                }
            }
        }.start()
    }

    private fun rcvTcpFile(id: Long?,ip: String, pno: Long, fno: String, fnm: String, fsz: Long,progressFun: (Long?,String,Long)->Unit){
        object: Thread() {
            override fun run() {
                var socket: Socket? = null
                var nos: OutputStream? =null
                var inp: InputStream? = null
                var fos: OutputStream? =null
                val db = DbUtil(this@MsgService)
                try {
                    socket = Socket(ip,AppConstants.MSG_GLOBAL_PORT)
                    val msg = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, AppConstants.MSG_GETFILEDATA,
                            "${java.lang.Long.toHexString(pno)}:$fno:0:")
                    msg.ip = ip
                    nos = socket.getOutputStream()
                    socket.soTimeout = 10000
                    val msgstr = msg.genMsgStr()
                    val buf = msgstr.toByteArray(Charset.forName("GBK"))
                    nos.write(buf,0,buf.size)
                    nos.flush()
                    //start receive
                    inp = socket.getInputStream()
                    val rcvPath = AppContext.instance?.fileRcvPath
                    fos = FileOutputStream("$rcvPath$fnm")
                    val rbuff = ByteArray(4096)
                    var len=inp.read(rbuff)
                    var part=0L
                    var dnsz=len
                    val perclen = fsz/100
                    while(len > 0) {
                        fos.write(rbuff, 0, len)
                        if(fsz in 1 .. dnsz) break
                        len = inp.read(rbuff)
                        dnsz += len
                        if (perclen > 0){
                            val pct = dnsz / perclen
                            if (pct != part) {
                                part = pct
                                progressFun(id,ip,part)
                            }
                        }

                    }
                    if(dnsz>0)fos.flush()
                    db.updMsgMtype(id,3)
                }catch (te: SocketTimeoutException){
                    val msgs = getResString(this@MsgService,R.string.rcv_file_timeout)
                    loadAlert(msgs)
                    Log.e(TAG,"rcvTcpFile error!${te.message}")
                    db.updMsgMtype(id,4)
                    progressFun(id,ip,-1)

                }catch(e: Exception){
                    val msgs = getResString(this@MsgService,R.string.rcv_file_unknownerror)
                    loadAlert(msgs)
                    Log.e(TAG,"rcvTcpFile error!${e.message}")
                    db.updMsgMtype(id,5)
                    progressFun(id,ip,-2)
                }finally {
                    fos?.close()
                    inp?.close()
                    nos?.close()
                    socket?.close()
                }
            }
        }.start()

    }

    private fun sendTcpFile(id: Long?,ip:String,file: File,ckFun: (MsgEnt,String)->Boolean,progressFun: (Long?,String,Long)->Unit){
        object: Thread() {
            override fun run() {
                var server: ServerSocket? = null
                var socket: Socket? = null
                var nin: InputStream? = null
                var fin: InputStream? = null
                var nos: OutputStream? = null
                val db = DbUtil(this@MsgService)
                try {
                    val fname = file.name.replace(":","::")
                    val fsize = java.lang.Long.toHexString(file.length())
                    val atta = AttaEnt("0",fname,fsize)
                    val pmsg = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_SENDMSG or AppConstants.MSG_FILEATTACHOPT),
                            AppConstants.MSG_GLOBAL_ZERO+atta.genAttaStr())
                    pmsg.ip = ip
                    sendMsg(pmsg)
                    /*tcp connect*/
                    server = ServerSocket(AppConstants.MSG_GLOBAL_PORT)
                    server.soTimeout = 20000
                    socket = server.accept()
                    nin = socket.getInputStream()
                    val rbuff = ByteArray(1024)
                    val length = nin.read(rbuff)
                    val remsg = String(rbuff, 0, length, Charset.forName("GBK"))
                    if(ckFun(pmsg,remsg)) {
                        nos = socket.getOutputStream()
                        fin = FileInputStream(file)
                        val buf = ByteArray(4096)
                        var len = fin.read(buf)
                        var part=0L
                        var dnsz=len
                        val perclen = file.length()/100
                        while (len > 0) {
                            nos.write(buf, 0, len)
                            len = fin.read(buf)
                            if (perclen > 0){
                                dnsz += len
                                val pct = dnsz / perclen
                                if (pct != part) {
                                    part = pct
                                    progressFun(id,ip,part)
                                }
                            }
                        }

                    }
                    db.updMsgMtype(id,3)
                }catch (te: SocketTimeoutException){
                    val msgs = getResString(this@MsgService,R.string.send_file_timeout)
                    loadAlert(msgs)
                    Log.d(TAG,msgs+te.message)
                    db.updMsgMtype(id,4)
                    progressFun(id,ip,-1)
                }catch(e: Exception) {
                    val msgs = getResString(this@MsgService,R.string.send_file_unknownerror)
                    loadAlert(msgs)
                    Log.d(TAG,msgs+e.message)
                    db.updMsgMtype(id,5)
                    progressFun(id,ip,-2)
                }finally{
                    nin?.close()
                    nos?.close()
                    fin?.close()
                    socket?.close()
                    server?.close()
                }
            }
        }.start()
    }

    /**common*/
    private fun onActStop() {

    }

    private fun onActResume() {
        initSocket()
    }

    override fun onBind(intent: Intent): IBinder?  = null

    companion object {
        private val TAG = "MsgService"
    }
}
