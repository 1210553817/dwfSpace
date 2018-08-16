package com.fxqyem.justmusic

import com.fxqyem.bean.MusicProvider
import com.fxqyem.bean.SongResult
import com.fxqyem.utils.HttpUtils
import com.fxqyem.vw.utilRandInt
import com.fxqyem.vw.utilRandLong
import com.google.gson.Gson
import org.apache.commons.codec.binary.Hex
import org.junit.Test
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Dwf on 2017/8/31.
 */
class KtTest {

    private fun testWY(){
        val songs: List<SongResult>? = MusicProvider.getWyLs("陈奕迅",1,3)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getWyPlayUrl(song.songId,0)
            println(surl)
            val lurl = MusicProvider.getWyLrc(song.songId)
            println(lurl)
            println("----------------")

        }

    }

    private fun wyMusicDetailTest(){
        var sid = "418603077"
        val heads = HashMap<String, String>()
        heads.put("X-REAL-IP", generateChinaRandomIP())
        heads.put("Referrer", "http://music.163.com/")

        var params="{\"method\":\"post\",\"url\":\"http://music.163.com/api/song/enhance/player/url\","+
                "\"params\":{\"ids\":[\"$sid\"],\"br\":128000}" +
                "}"
        var encrypted = encrypt(params)
        var result = HttpUtils.doPost("http://music.163.com/api/linux/forward", "eparams=$encrypted", heads )
        println(result)
    }

    fun encrypt(raw: String): String? {
        var scrt = "7246674226682325323F5E6544673A51"
        val encrypted: ByteArray?
        try {
            encrypted = encrypt(raw, Hex.decodeHex(scrt.toCharArray()))
            return String(Hex.encodeHex(encrypted)).toUpperCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(Exception::class)
    fun encrypt(sSrc: String, sKey: ByteArray): ByteArray? {
        return encrypt(sSrc.toByteArray(charset("utf-8")),sKey)
    }
    @Throws(Exception::class)
    fun encrypt(sSrc: ByteArray, sKey: ByteArray): ByteArray? {
        return encrypt(sSrc, sKey,null)
    }
    @Throws(Exception::class)
    fun encrypt(sSrc: ByteArray, sKey: ByteArray?, iv: ByteArray?): ByteArray? {
        if (sKey == null) {
            print("Key为空null")
            return null
        }
        // 判断Key是否为16位
        if (sKey.size != 16) {
            print("Key长度不是16位")
            return null
        }
        val skeySpec = SecretKeySpec(sKey, "AES")
        var ivspec: IvParameterSpec? = null
        if (iv != null) {
            ivspec = IvParameterSpec(iv)
        }
        val cipher: Cipher
        if (ivspec == null) {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        } else {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec)
        }
        return cipher.doFinal(sSrc)
    }
    fun generateChinaRandomIP(): String {
        val ips = arrayOf("47.93.50.","60.5.0.","60.5.65.","1.193.66.","61.158.132.","14.21.128.","59.33.174.","61.186.81.","61.139.72.")
        return ips[Random().nextInt(ips.size-1)] + (1 + Random().nextInt(255))
    }

    fun testXM(){
       val songs: List<SongResult>? = MusicProvider.getXmLs("陈奕迅",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getXmUrl(song.songId,0,"mp3")
            println(surl)
            val lurl = MusicProvider.getXmUrl(song.songId,0,"lrc")
            println(lurl)
            println("----------------")

        }

    }

    fun testKG(){
       val songs: List<SongResult>? = MusicProvider.getKgLs("周杰伦",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getKgPlayUrl(song.songId,0)
            println(surl)
            val lurl = MusicProvider.getKgUrl(song.songId,"0","lrc")
            println(lurl)
            println("----------------")

        }

    }

    fun testQQ(){
        val songs: List<SongResult>? = MusicProvider.getQqLs("周杰伦",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getQqPlayUrl(song.songId,0)
            println(surl)
            val lurl = MusicProvider.getQqLrcA(song.songId?:"")
            println(lurl)
            println("----------------")

        }

    }

    fun testBD(){
        val songs: List<SongResult>? = MusicProvider.getBdLs("周杰伦",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getBdUrl(song.songId,0,"mp3")
            println(surl)
            val lurl = MusicProvider.getBdUrl(song.songId,0,"lrc")
            println(lurl)
            println("----------------")

        }

    }

    fun testMG(){
        val songs: List<SongResult>? = MusicProvider.getMgLs("陈奕迅",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            println(song.lrcUrl)
            println("----------------")

        }

    }

    fun testF(){
        val songs: List<SongResult>? = MusicProvider.getWsLs("周杰伦",1,10)
        songs?:return
        for(song in songs){
            println(Gson().toJson(song))
            val surl = MusicProvider.getWsPlayUrl(song.songId)
            println(surl)
            println("----------------")

        }

    }

    /**
     * adb push c:\justMusic.apk /mnt/sdcard/apks/wx.apk
     * adb install c:\justMusic.apk
     * adb uninstall com.fxqyem
     * ps | grep 'fxqyem'
     * kill 5655
     *
     *cd D:\Dev\android\android-sdk-windows\platform-tools
     *
     *adb pull /sdcard/Android/data/com.fxqy.slimMusic/database/datas.db c:/
     *
     *
     */




    fun littleTestXM(){
        /*
         *  {"songId":"1776156051","songName":"告白气球"}
         *  {"songId":"376049","songName":"晴天"}
         *  */
        //Cookie
        val cookie = "_xiamitoken=18a1d9114cb3c25f4c427c96e63b5c52; _unsign_token=a056bfbac218f1e7fdddd5a951802025; gid=151364912431365; UM_distinctid=1606c8fa4f2135-012a1ed396cedb-5e193d17-1fa400-1606c8fa4f3e5; cna=9d+4Eh8o42UCAW/PAVyC7Nyo; bdshare_firstime=1513652712960; CNZZDATA921634=cnzz_eid%3D491554699-1513648037-%26ntime%3D1513648037; CNZZDATA2629111=cnzz_eid%3D834776642-1513645923-%26ntime%3D1513651323; XMPLAYER_isOpen=1; __guestplay=MTc5NTk3Nzg5NSwx; XMPLAYER_url=/song/playlist/id/1771778464/object_name/default/object_id/0; XMPLAYER_addSongsToggler=0; isg=AoqKYiRi5P2yYmhRMoH40qlL23Ds0w-0SkanQhTCP11oxyiB_Qpm5rZ7owXg"

        val host = "www.xiami.com"
        val referer = "http://www.xiami.com/play?ids=/song/playlist/id/376049/object_name/default/object_id/0"
        val tms = java.util.Date().time
        val url = "http://www.xiami.com/song/playlist/id/376049/object_name/default/object_id/0/cat/json?_ksTS=${tms}_692&callback=jsonp693"

        val reqHeaders = HashMap<String, String>()
        reqHeaders.put("Host", host)
        reqHeaders.put("User-agent", userAgent)
        reqHeaders.put("Referer", referer)
        val restr = HttpUtils.doPost(url, "", reqHeaders)

        println(restr)



    }

    private fun genSites(){
        val cenInArr = arrayOf(
                "a sample hold file used by soft which for shadow ",
                "This file contains the mappings of IP addresses to host names Each ",
                "entry should be kept on an individual line The IP address should ",
                "be placed in the first column followed by the corresponding host name ",
                "The IP address and the host name should be separated by at least one ",
                "space Additio nally comments such as these may be inserted on individual ",
                "lines or following the machine name denoted by an symbol ",
                "True love is best seen as the promotion and action not an emotion. Love is not exclu sively ",
                "based how we feel Certainly our emotions are involved But they cannot be our only criteria ",
                "for love True love is when you care enough about another person that you will lay down your ",
                "life for them When this happens,then love truly is as strong as death.How many of you have ",
                "or father husband or wife son or daughter or friend who would sacrifice his or ",
                "unsel fishly lay your life on the line to save them from death Many people in an emergency ",
                "room with their loved ones and prayed please God take me instead of them.Find true ",
                "and be a true lover as well May you find a love which is not only strong as death but to leave to a truly for feeling life",
                "The production and cons umption of top quality ham is the subject of the same awe and mystery as the making of fine wine Cutting de-boned jamón de bellota on a ham-slicing machine is regarded as sacri lege The leg must be bolted on to a frame called a jamo nero and then cut by hand using a long narrow blade Most towns in the ham producing areas of Extre madura Cast illa y León and Anda lucía hold solemn ham slicing compet itions often attended by hundreds of spectators"
            ).joinToString(separator = "").split(" ")

        val beforeArr = arrayOf("www","bbs","blog","mail","ftp","map","wap","m","news","chat","doc","api")
        val centerStartArr = arrayOf(
                "Since ","Agent","Host","Song","Plat","Form","User","Tiny","Header","Call","Like",
                "Annoyingly","Previously","Level","However","Support","Probably","Client","Question","Handful","Consumption",
                "Fake","Maybe","Unqualified","Examination","Identity","Force","Simulate","Intellectual","Perhaps",
                "Cannes","Shaggy","Sacred","Toward","Domestic","Depends","Unmask"
        )
        val centerEndrr = arrayOf("Refer","Default","Unsign","Play","Token","Kit","Apple","Zilla","Model",
                "Upgrad","Remains","Creation","Inauthentic","Doubt","Pictures","Adaptation","Spawned","Acclaimed","Particular",
                "Chalamet","Academic","Hammer","Sundance","Festival","January","Culture","Photography",
                "Released","Classics","Universe","Holmes","Industry","Transformative","Pattinson")
        val afterArr = arrayOf("com","org","gov","hk","com.cn","cn","edu","biz","int",
                "mil","uk","cc","tw","net","tv","us","uk","fr",
                "ru","it","ma","kr","mg","mz","za","ci")

        while(true){
            val bef = beforeArr[utilRandInt(0,beforeArr.size-1)]
            val ces = centerStartArr[utilRandInt(0,centerStartArr.size-1)]
            val cin = cenInArr[utilRandInt(0,cenInArr.size-1)]
            val cee = centerEndrr[utilRandInt(0,centerEndrr.size-1)]
            val aft = afterArr[utilRandInt(0,afterArr.size-1)]
            //val rmdstr = String.format("%s",utilRandLong())
            //val rmdstr = String.format("%s",utilRandInt(0,999))
            //var rmdstr = java.lang.Long.toHexString(utilRandLong())
            var rmdstr ="er"
            var suffix="ed"
            val url = "http://$bef.$cin$rmdstr$ces$cee$suffix.$aft"
            HttpUtils.doGetAsyn(url,object: HttpUtils.CallBack{
                override fun onRequestComplete(result: String?) {
                    println("requestComplete...")
                }
            })
            //val ourl = "http://blog.163.com/sun__haiming/blog/static/$bef$cin$ces$cee$aft$rmdstr/"
            //println(ourl)
            //HttpUtils.doGet(url)

        }


    }

    @Test
    fun testAllAPP(){

        testQQ()

        //wyMusicDetailTest()

        //testMG()

        //littleTestXM()

        //genSites()

        //tcpTestMain()

//        var str = HttpUtils.doGetEncoding("http://218.200.230.40:18089/files/lyric/2017-07-26/b4584b80df21414d8fac31ee23f0c759.lrc",null,"utf-8")
//        var restr = MusicProvider.addMs(str,-600)
//        println(restr)

        //println(MusicProvider.addMs("[03:01.500]",6600))
    }

    private fun tcpTestMain(){
        for(i in 1..50){
            object: Thread() {
                override fun run() {
                    tcpConnectTest(i)
                }
            }.start()
        }
    }
    private fun tcpConnectTest(i: Int){
        var socket: Socket? = null
        var nos: OutputStream? =null
        var inp: InputStream? = null
        var fos: OutputStream? =null
        try {
            println("Thread $i is running...")
            socket = Socket("14.17.52.184",80)
            nos = socket.getOutputStream()
            socket.soTimeout = 10000
            val buf = "dgdfgdfgdfgdgfdfgdfg".toByteArray(Charset.forName("utf-8"))
            nos.write(buf,0,buf.size)
            nos.flush()
        }catch(e: Exception){
            e.printStackTrace()
        }finally {
            fos?.close()
            inp?.close()
            nos?.close()
            socket?.close()
        }
    }

    companion object {
        val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"
    }



}
