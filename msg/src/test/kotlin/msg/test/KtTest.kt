package com.fxqyem.justmusic

import org.junit.Test
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ben.AppContext
import com.fxqyem.msg.ent.AttaEnt
import com.fxqyem.msg.ent.MsgEnt
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.*
import java.nio.charset.Charset

/**
 * Created by Dwf on 2017-12-8 15:25:21.
 */
class KtTest {
    private val msgServer = DatagramSocket(AppConstants.MSG_GLOBAL_PORT)

    fun sendUdpFileAttr(){
        sendOnlineMsg()

        Thread.sleep(5000)

        println("initSocket 000000000 -----------------------")

        val file = File("C:\\Windows\\Web\\Wallpaper\\win10_2.jpg")
        val fname = file.name.replace(":","::")
        val fsize = java.lang.Long.toHexString(file.length())

        println("initSocket 1111111111 -----------------------")

        val addr = InetAddress.getByName("192.168.1.3")
        val atta = AttaEnt("0",fname,fsize)
        val wel = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_SENDMSG or AppConstants.MSG_FILEATTACHOPT),
                AppConstants.MSG_GLOBAL_ZERO+atta.genAttaStr())

        val sbuf = wel.genMsgStr().toByteArray()
        val spacket = DatagramPacket(sbuf, sbuf.size, addr, AppConstants.MSG_GLOBAL_PORT)
        msgServer?.send(spacket)

//        val buffer = ByteArray(4096)
//        val packet = DatagramPacket(buffer, buffer.size)
//        while(true)
//        {
//            msgServer?.receive(packet)
//            val msg = String(packet.data, packet.offset, packet.length)
//
//            println("initSocket receive -----------------------$msg")
//        }

        println("initSocket 22222222222 -----------------------")

        sendTcpFile(file,atta)

        println("initSocket 33333333333 -----------------------")
    }

    private fun sendOnlineMsg(){
        val wel = MsgEnt(AppContext.instance?.utit, AppContext.instance?.usub, (AppConstants.MSG_BR_ENTRY), AppContext.instance?.uname)
        wel.ip = "255.255.255.255"
        sendMsg(wel)
    }

    private fun sendMsg(msg: MsgEnt){
        val addr = InetAddress.getByName(msg.ip)
        val sbuf = msg.genMsgStr().toByteArray(Charset.forName("GBK"))
        val spacket = DatagramPacket(sbuf, sbuf.size, addr, AppConstants.MSG_GLOBAL_PORT)
        msgServer?.send(spacket)

    }

    private fun sendTcpFile(file: File,atta: AttaEnt){
        var server: ServerSocket? = null
        var socket: Socket? = null
        var nin: InputStream? = null
        var fin: InputStream? = null
        var nos: OutputStream? = null
        try {
            server = ServerSocket(AppConstants.MSG_GLOBAL_PORT)
            socket = server.accept()

            println("read start...")
            nin = socket.getInputStream()
            val rbuff = ByteArray(1024)
            val length = nin.read(rbuff)
            val remsg = String(rbuff, 0, length, Charset.forName("GBK"))
            println(remsg)

            println("write start...")
            nos = socket.getOutputStream()
            val fin = FileInputStream(file)
            val buf = ByteArray(1024)
            var len = fin.read(buf)
            while (len > 0) {
                nos.write(buf, 0, len)
                len = fin.read(buf)
            }
        }catch(e: Exception) {
            nin?.close()
            nos?.close()
            fin?.close()
            socket?.close()
            server?.close()
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
     *adb pull /sdcard/Android/data/com.fxqy.MSG/database/msgdatas.db ./temp/
     *
     *
     */



    @Test
    fun testAll(){

        sendUdpFileAttr()

//        val strs = listOf("111111","22222222222","33333333333").joinToString(separator = "")
//        println(strs)

//        val zro = AppConstants.MSG_GLOBAL_ZERO
//        val astr = "dfgdfgdfgdfgdfgsdfsdfsf"
//
//        val arrs = astr.split(zro)
//        println("----->${arrs[0]}")
//        for(i in arrs)
//            println(i)

    }

}
