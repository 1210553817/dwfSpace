package com.fxqyem.justmusic

import com.fxqyem.bean.MusicProvider
import com.fxqyem.bean.SongResult
import com.fxqyem.utils.HttpUtils
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
        testB()

        //littleTest()

    }

    fun littleTest(){
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

    companion object {
        val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"
    }


}
