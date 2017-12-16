package com.fxqyem.justmusic

import com.fxqyem.bean.MusicProvider
import com.fxqyem.bean.SongResult
import com.google.gson.Gson
import org.junit.Test
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ent.MsgEnt
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

import com.fxqyem.vw.*

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

    fun testSocket(){
        println("initSocket 1111111111 -----------------------")
        val msgServer = DatagramSocket(AppConstants.MSG_GLOBAL_PORT)
        val buffer = ByteArray(4096)
        val packet = DatagramPacket(buffer, buffer.size)
        val addr = InetAddress.getByName("255.255.255.255")
        val wel = MsgEnt("zhangsan", "zhangsanLocalhost", (AppConstants.MSG_BR_ENTRY), "TRANS")
        val sbuf = wel.genMsgStr().toByteArray()
        val spacket = DatagramPacket(sbuf, sbuf.size, addr, AppConstants.MSG_GLOBAL_PORT)
        msgServer?.send(spacket)
        while(true)
        {
            msgServer?.receive(packet)
            val msg = String(packet.data, packet.offset, packet.length)

            println("initSocket receive -----------------------$msg")
        }
        println("initSocket 22222222222 -----------------------")
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
    fun testAll(){
        testSocket()
        //testB()

//        val rdm = utilRandInt(0,6)
//        println("$rdm")
    }

}
