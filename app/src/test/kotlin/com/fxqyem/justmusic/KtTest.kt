package com.fxqyem.justmusic

import com.fxqyem.bean.MusicProvider
import com.fxqyem.bean.SongResult
import com.fxqyem.utils.HttpUtils
import com.fxqyem.vw.utilRandInt
import com.fxqyem.vw.utilRandLong
import com.google.gson.Gson
import org.junit.Test

/**
 * Created by Dwf on 2017/8/31.
 */
class KtTest {

    fun testB(){
       val songs: List<SongResult>? = MusicProvider.getXmLs("周杰伦",1,10)
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

    fun testC(){
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

    fun testD(){
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



    fun testE(){
        val songs: List<SongResult>? = MusicProvider.getWyLs("周杰伦",1,10)
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
     * adb push c:\justMusic.apk /mnt/sdcard/apks/
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



    @Test
    fun testAllAPP(){
        //testB()

        //littleTestXM()

        genSites()
    }

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
                "This is a sample HOSTS file used by Microsoft TCPIP for Windows ",
                "This file contains the mappings of IP addresses to host names Each ",
                "entry should be kept on an individual line The IP address should ",
                "be placed in the first column followed by the corresponding host name ",
                "The IP address and the host name should be separated by at least one ",
                "space Additionally comments such as these may be inserted on individual ",
                "lines or following the machine name denoted by an symbol ",
                "True love is best seen as the promotion and action not an emotion. Love is not exclusively ",
                "based how we feel.Certainly our emotions are involved But they cannot be our only criteria ",
                "for love.True love is when you care enough about another person that you will lay down your ",
                "life for them. When this happens,then love truly is as strong as death.How many of you have ",
                "or father husband or wife son or daughter or friend who would sacrifice his or ",
                "unselfishly lay your life on the line to save them from death Many people in an emergency ",
                "room with their loved ones and prayed please God take me instead of them.Find true ",
                "and be a true lover as well.May you find a love which is not only strong as death but to leave to a truly for feeling life"
            ).joinToString(separator = "").split(" ")

        val beforeArr = arrayOf("www","bbs","blog","mail","ftp","map")
        val centerStartArr = arrayOf(
                "Agent","Host","Song","Plat","Form","User","Tiny","Header","Call","Like",
                "Annoyingly","Previously","Level","However","Support","Probably","Client","Question","Handful","Consumption"
        )
        val centerEndrr = arrayOf("Refer","Default","Unsign","Play","Token","Kit","Apple","Zilla","Model","Upgrad","Remains","Creation")
        val afterArr = arrayOf("com","org","gov","hk","com.cn","cn","edu","biz","int","mil","uk")

        while(true){
            val bef = beforeArr[utilRandInt(0,beforeArr.size-1)]
            val ces = centerStartArr[utilRandInt(0,centerStartArr.size-1)]
            val cin = cenInArr[utilRandInt(0,cenInArr.size-1)]
            val cee = centerEndrr[utilRandInt(0,centerEndrr.size-1)]
            val aft = afterArr[utilRandInt(0,afterArr.size-1)]
            //val rmdstr = String.format("%s",utilRandLong())
            //val rmdstr = String.format("%s",utilRandInt(0,999))
            var rmdstr = java.lang.Long.toHexString(utilRandLong())
            val url = "http://$bef.$cin$rmdstr$ces$cee.$aft"
            HttpUtils.doGet(url)

            rmdstr = java.lang.Long.toHexString(utilRandLong())
            val ourl = "http://blog.163.com/sun__haiming/blog/static/$bef$cin$ces$cee$aft$rmdstr/"
            //println(ourl)
            HttpUtils.doGet(ourl)

        }


    }

    companion object {
        val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"
    }


}
