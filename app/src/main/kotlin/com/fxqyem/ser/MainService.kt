package com.fxqyem.ser

import android.app.Service
import android.content.*
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.KeyEvent
import com.fxqyem.bean.*
import com.fxqyem.utils.LrcParser
import com.fxqyem.vw.utilRandInt
import org.jetbrains.anko.doAsync
import java.util.*


class MainService : Service(), MediaPlayer.OnPreparedListener {

    private var mPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var task: TimerTask? = null
    private var mRevwHandler: RevwHandler? = null
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mReceiver: BroadcastReceiver? = null
    //Datas
    private var curSongLs: ArrayList<SongInfo>? = null

    override fun createConfigurationContext(overrideConfiguration: Configuration?): Context {
        return super.createConfigurationContext(overrideConfiguration)
    }

    private var lrcTim: IntArray? = null
    private var lrcMsg: Array<String?>? = null
    //Params
    private var curSongIndx = -1
    private var mediaBtnNum: Int = 0
    private var curLrcLn = 0

    private var isPlaying = false
    private var isMediaBtnCount: Boolean = false
    private var lrcUsf = false
    private var lrcCan = false
    private var volAniRun = false


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this)
        val filter = IntentFilter()
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_NEXT)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_PREV)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_PLAY)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_STOP)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_STOP)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_RESUME)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_INDX)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_SENDLS)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_SEEK)
        filter.addAction(AppConstants.PLAYER_CTRL_ACTION_SHOWMODECHG)
        filter.addAction(Intent.ACTION_MEDIA_BUTTON)
        filter.addAction(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        mReceiver = PlayCtrlBroadcastReceiver()
        mLocalBroadcastManager?.registerReceiver(mReceiver, filter)

        (getSystemService(Context.AUDIO_SERVICE) as AudioManager).registerMediaButtonEventReceiver(ComponentName(this, MediaButtonBroadcastReceiver::class.java))

        mRevwHandler = RevwHandler()

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //curSongLs = AppContext.instance?.tmpLs
        curSongIndx = AppContext.instance?.curIndex?:0
        sendPlayBroadCast(false, false)
        isPlaying = true
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mPlayer?.release()
        mPlayer = null
        mLocalBroadcastManager!!.unregisterReceiver(mReceiver)
        (getSystemService(Context.AUDIO_SERVICE) as AudioManager).unregisterMediaButtonEventReceiver(ComponentName(this, MediaButtonBroadcastReceiver::class.java))
        timer?.cancel()
        timer = null


        super.onDestroy()
    }

    fun play() {
        if (mPlayer == null) {
            if (!createPlayer()) return
        } else {
            if (volAniRun) return
            isPlaying = mPlayer?.isPlaying?:false
            if (isPlaying) {
                val dur = 1200
                volAniRun = true
                object : CountDownTimer(dur.toLong(), 100) {
                    internal var begin = 1.0f
                    internal var end = 0.0f

                    override fun onTick(unf: Long) {
                        val a = (dur - unf).toFloat()
                        val b = dur.toFloat()
                        val aval = a / b * (end - begin) + begin
                        mPlayer?.setVolume(aval, aval)
                    }

                    override fun onFinish() {
                        timer?.cancel()
                        mPlayer?.setVolume(end, end)
                        mPlayer?.pause()
                        volAniRun = false
                        this.cancel()
                    }
                }.start()
            } else {
                mPlayer?.setVolume(0.0f, 0.0f)
                mPlayer?.start()
                startTimer()
                val dur = 1000
                volAniRun = true
                object : CountDownTimer(dur.toLong(), 100) {
                    internal var begin = 0.0f
                    internal var end = 1.0f

                    override fun onTick(unf: Long) {
                        val a = (dur - unf).toFloat()
                        val b = dur.toFloat()
                        val aval = a / b * (end - begin) + begin
                        mPlayer?.setVolume(aval, aval)
                    }

                    override fun onFinish() {
                        mPlayer?.setVolume(end, end)
                        volAniRun = false
                        this.cancel()
                    }
                }.start()
            }
            isPlaying = !isPlaying
            sendPlayBroadCast(isPlaying, false)
        }
    }

    fun indxPlay() {
        createPlayer()
    }

    private operator fun next() {
        nextIndx()
        createPlayer()
    }

    private fun nextIndx() {
        if (curSongIndx == curSongLs!!.size - 1) {
            curSongIndx = 0
        } else {
            curSongIndx++
        }
    }

    private fun prev() {
        prevIndx()
        createPlayer()
    }

    private fun prevIndx() {
        if (curSongIndx == 0) {
            curSongIndx = curSongLs!!.size - 1
        } else {
            curSongIndx--
        }
    }

    private fun sendPlayBroadCast(isp: Boolean, isnew: Boolean) {
        curSongLs = AppContext.instance?.tmpLs
        curSongLs?:return
        val itt = Intent(AppConstants.PLAYER_REVW_ACTION_PLAY)
        itt.putExtra("INDX", curSongIndx)
        if (mPlayer != null) {
            itt.putExtra("DURATION", mPlayer!!.duration)
        }
        if(curSongIndx<curSongLs?.size?:0-1&&curSongIndx>-1) {
            itt.putExtra("TITLE", curSongLs?.get(curSongIndx)?.name)
            itt.putExtra("ARTIST", curSongLs?.get(curSongIndx)?.artist)
        }
        itt.putExtra("ISPLAY", isp)
        itt.putExtra("ISNEW", isnew)
        itt.putExtra("LRCTIM", lrcTim)
        itt.putExtra("LRCMSG", lrcMsg)
        mLocalBroadcastManager?.sendBroadcast(itt)

    }

    private fun createPlayer(): Boolean {
        if (curSongLs == null|| curSongLs?.size?:0<1 || curSongIndx < 0
                || curSongIndx > curSongLs!!.size - 1
                || curSongLs!![curSongIndx] == null)
            return false
        try {
            if (mPlayer != null) {
                if (timer != null) timer?.cancel()
                mPlayer?.reset()
            } else {
                mPlayer = MediaPlayer()
                mPlayer?.setOnCompletionListener {
                    val lpmd = AppContext.instance?.playMode?:0
                    when(lpmd) {
                        0 -> next()
                        1 -> indxPlay()
                        2 -> prev()
                        3 ->{
                            val inx=utilRandInt(0,(curSongLs?.size?:1)-1)
                            when(inx){
                                curSongIndx -> curSongIndx=0
                                else -> curSongIndx = inx
                            }
                            indxPlay()
                        }
                        else-> next()
                    }

                }
                mPlayer?.setOnPreparedListener(this)
            }
            val snm = curSongLs!![curSongIndx].data
            mPlayer?.setDataSource(snm)
            mPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.e(TAG, "Create player Error!" + e.message)
            return false
        }

        return true
    }

    override fun onPrepared(mp: MediaPlayer) {
        lrcUsf = false
        curLrcLn = 0
        lrcTim = null
        lrcMsg = null
        if (isPlaying) {
            mp.start()
            startTimer()
        }
        mp.setVolume(1.0f, 1.0f)
        parseLrc(curSongLs?.get(curSongIndx))
        sendPlayBroadCast(isPlaying, true)
        AppContext.instance?.updateCurIndex(curSongIndx)
    }

    private inner class PlayCtrlBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Log.w(TAG,"************PlayCtrlBroadcastReceiver: "+intent.getAction());
            curSongLs = AppContext.instance?.tmpLs
            curSongIndx = AppContext.instance?.curIndex?:0
            var bd: Bundle?
            bd = intent?.extras
            if (AppConstants.PLAYER_CTRL_ACTION_NEXT == intent.action) {
                next()
            } else if (AppConstants.PLAYER_CTRL_ACTION_PREV == intent.action) {
                prev()
            } else if (AppConstants.PLAYER_CTRL_ACTION_PLAY == intent.action) {
                play()
            } else if (AppConstants.PLAYER_CTRL_ACTION_INDX == intent.action) {
                val type = bd?.getInt("TYPE", -1)
                val sls = bd?.getSerializable(AppConstants.PLAYER_KEY_LS) as ArrayList<SongInfo>?
                if (sls != null && sls.size > 0) curSongLs = sls
                val indx = bd?.getInt("INDX", -1)
                val isp = bd?.getBoolean("PLY", false)

                if (type == 1) {
                    if (indx!! > -1) curSongIndx = indx
                    if (isp!!) {
                        isPlaying = true
                        indxPlay()
                    }
                } else if (type == 2) {
                    val oldindx = bd?.getInt("OLDINDX", -1)
                    if (oldindx == curSongIndx && mPlayer != null) {
                        if (indx!! > -1) curSongIndx = indx
                        isPlaying = false
                        if (curSongLs != null && curSongLs!!.size > 0 && curSongIndx > -1 && curSongIndx < curSongLs!!.size) {
                            indxPlay()
                        } else if (mPlayer != null) {
                            mPlayer!!.stop()
                            sendPlayBroadCast(false, false)
                            mPlayer = null
                        }
                    } else {
                        if (indx!! > -1) curSongIndx = indx
                    }
                }
            } else if (AppConstants.PLAYER_CTRL_ACTION_SEEK == intent.action) {
                bd = intent.extras
                val skPos = bd?.getInt("SKPOS",-1)?:-1
                if (mPlayer != null&&skPos>-1) {
                    mPlayer?.seekTo(skPos)
                }
            } else if (AppConstants.PLAYER_CTRL_ACTION_SHOWMODECHG == intent.action) {
                bd = intent.extras
                lrcCan = bd!!.getBoolean("FLG")
                //sendPlayBroadCast(null != mPlayer ? mPlayer.isPlaying() : false, true);
            } else if (AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_STOP == intent.action) {
                actStop()
            } else if (AppConstants.PLAYER_CTRL_ACTION_ACTIVITY_RESUME == intent.action) {
                actResume()
            } else if (Intent.ACTION_MEDIA_BUTTON == intent.action) {//媒体键
                //Log.w(TAG,"#####PlayCtrlBroadcastReceiver: 媒体键 "+intent.getAction());
                val keyEvent = intent.extras.get(
                        Intent.EXTRA_KEY_EVENT) as KeyEvent
                if (keyEvent != null)
                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_HEADSETHOOK -> if (isMediaBtnCount) {
                            mediaBtnNum++
                        } else {
                            isMediaBtnCount = true
                            object : CountDownTimer(1000, 1000) {
                                override fun onTick(unf: Long) {}

                                override fun onFinish() {
                                    //Log.w("MediaBtnTAG","KEYCODE_HEADSETHOOK Times: "+mediaBtnNum);
                                    if (1 == mediaBtnNum) {
                                        play()
                                    } else if (3 == mediaBtnNum) {
                                        next()
                                    } else if (5 == mediaBtnNum) {
                                        prev()
                                    }
                                    isMediaBtnCount = false
                                    mediaBtnNum = 0
                                }
                            }.start()
                        }
                    }
            } else if (android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                if (mPlayer != null && mPlayer!!.isPlaying) {
                    play()
                }
            }
        }
    }

    private inner class RevwHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val bd = msg.data
            when (msg.what) {
                WHAT_REVW -> {
                    val itt = Intent(AppConstants.PLAYER_REVW_ACTION_PROG)
                    itt.putExtra("PROG", mPlayer!!.currentPosition)
                    val curPos = bd.getInt("LRCCPOS", -1)
                    if (curPos > -1) {
                        itt.putExtra("LRCCPOS", curPos)
                    }
                    mLocalBroadcastManager?.sendBroadcast(itt)
                }
            }
        }
    }

    private fun startTimer() {
        if (mPlayer == null || curSongLs == null) return
        if (null != timer) {
            timer?.cancel()
            timer?.purge()
            timer = null
        }
        timer = Timer()
        task = object : TimerTask() {
            internal var curPos = -1

            override fun run() {
                val message = Message()
                message.what = WHAT_REVW
                val bd = Bundle()
                if (mPlayer == null) {
                    this.cancel()
                    return
                }
                val offset = mPlayer?.currentPosition?:0
                if (lrcUsf && lrcCan) {
                    while (true) {
                        // Log.w(TAG, "...................RevwHandler handleMessage");
                        if (curLrcLn < lrcTim!!.size - 1 && offset > lrcTim!![curLrcLn + 1]) {
                            curLrcLn++
                            // Log.w(TAG, ">>>>>>>>>>>>>>>Big");
                        } else if (curLrcLn > 0 && offset < lrcTim!![curLrcLn - 1]) {
                            curLrcLn--
                            // Log.w(TAG, ">>>>>>>>>>>>>>>Small");
                        } else if (curLrcLn < lrcTim!!.size - 1 && Math.abs(offset - lrcTim!![curLrcLn + 1]) < 500) {
                            curLrcLn++
                            break
                        } else if (curLrcLn > 0 && lrcTim!![curLrcLn] - offset > 800) {
                            curLrcLn--
                            break
                        } else {
                            break
                        }
                    }
                    if (curPos != curLrcLn) {
                        curPos = curLrcLn
                        bd.putInt("LRCCPOS", curPos)
                    }
                    message.data = bd
                }
                mRevwHandler?.sendMessage(message)
            }
        }
        timer?.schedule(task, 0, 1000)
    }


    fun parseLrc(songInfo: SongInfo?) {
        songInfo?:return
        val mpath=songInfo.data
        if (null == mpath || mpath.length < 6) {
            return
        }
        try {
            if (songInfo.type == null || ""==songInfo.type) {
                val ifo = LrcParser.parseLrc(mpath.substring(0, mpath.lastIndexOf(".")) + ".lrc",mPlayer?.duration?:0) ?: return
                lrcTim = ifo.tims
                lrcMsg = ifo.mes
                if (lrcTim != null && lrcMsg != null && lrcTim!!.size > 0) {
                    lrcUsf = true
                    return
                }
            } else {
                doAsync {
                    val sitm = SongResult()
                    sitm.type = songInfo.type
                    sitm.songId = songInfo.songId
                    sitm.qqmid = songInfo.qmid
                    sitm.lrcUrl = songInfo.lrcUrl
                    val lrcText = MusicProvider.getLrcText(sitm)
                    if(lrcText!=null) {
                        val ifo = LrcParser.parseLrcA(lrcText.toByteArray(),mPlayer?.duration?:0)
                        lrcTim = ifo?.tims
                        lrcMsg = ifo?.mes
                        if (lrcTim != null && lrcMsg != null && lrcTim!!.size > 0) {
                            lrcUsf = true
                            sendPlayBroadCast(isPlaying,true)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

    }

    private fun actStop() {
        timer?.cancel()

    }

    private fun actResume() {
        sendPlayBroadCast(mPlayer?.isPlaying?:false, true)
        startTimer()
    }

    companion object {
        private val TAG = "MainService"
        private val WHAT_REVW = 1
    }
}
