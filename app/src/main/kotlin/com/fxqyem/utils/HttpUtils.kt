package com.fxqyem.utils


import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object HttpUtils {
    private val TIMEOUT_IN_MILLIONS = 10000
    interface CallBack {
        fun onRequestComplete(result: String?)
    }
    interface StateCall {
        fun onStateChange(state: Int,value: Int,result: String?)
    }
    /**
     * 异步的Get请求

     * @param urlStr
     * *
     * @param callBack
     */
    fun doGetAsyn(urlStr: String,callBack: CallBack?){
        doGetAsyn(urlStr,null,callBack)
    }
    fun doGetAsyn(urlStr: String, headParams: Map<String,String>?, callBack: CallBack?) {
        object : Thread() {
            override fun run() {
                try {
                    val result = doGet(urlStr,headParams)
                    callBack?.onRequestComplete(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }.start()
    }
    /**
     * 异步的Post请求
     * @param urlStr
     * *
     * @param params
     * *
     * @param callBack
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun doPostAsyn(urlStr: String, params: String,callBack: CallBack?) {
        doPostAsyn(urlStr, params,null,callBack)
    }
    @Throws(Exception::class)
    fun doPostAsyn(urlStr: String, params: String,headParams: Map<String,String>?,callBack: CallBack?) {
        object : Thread() {
            override fun run() {
                try {
                    val result = doPost(urlStr, params,headParams)
                    callBack?.onRequestComplete(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }.start()

    }
    /**
     * Get请求，获得返回数据

     * @param urlStr
     * *
     * @return
     * *
     * @throws Exception
     */
    fun doGet(urlStr: String): String? {
        return doGet(urlStr,null)
    }
    fun doGet(urlStr: String,headParams: Map<String,String>?): String? {
        var url: URL?
        var conn: HttpURLConnection? = null
        var inpt: InputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = TIMEOUT_IN_MILLIONS
            conn.connectTimeout = TIMEOUT_IN_MILLIONS
            conn.requestMethod = "GET"
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            if(headParams!=null){
                for(ent in headParams.entries){
                    conn.setRequestProperty(ent.key,ent.value)
                }
            }
            if (conn.responseCode == 200) {
                inpt = conn.inputStream
                baos = ByteArrayOutputStream()
                val buf = ByteArray(128)
                var len = inpt.read(buf)
                while (len!= -1) {
                    baos.write(buf, 0, len)
                    len = inpt.read(buf)
                }
                baos.flush()
                return baos.toString()
            } else {
                throw RuntimeException(" responseCode is not 200 ... ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (inpt != null)
                    inpt.close()
            } catch (e: IOException) {
            }

            try {
                if (baos != null)
                    baos.close()
            } catch (e: IOException) {
            }

            conn?.disconnect()
        }

        return null

    }
    /**
     * 向指定 URL 发送POST方法的请求

     * @param url
     * *            发送请求的 URL
     * *
     * @param param
     * *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * *
     * @return 所代表远程资源的响应结果
     * *
     * @throws Exception
     */
    fun doPost(url: String, param: String?): String {
        return doPost(url, param,null)
    }
    fun doPost(url: String, param: String?,headParams: Map<String,String>?): String {
        var out: PrintWriter? = null
        var inpt: BufferedReader? = null
        var result = ""
        try {
            val realUrl = URL(url)
            // 打开和URL之间的连接
            val conn = realUrl
                    .openConnection() as HttpURLConnection
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            if(headParams!=null){
                for(ent in headParams.entries){
                    conn.setRequestProperty(ent.key,ent.value)
                }
            }
            conn.useCaches = false
            // 发送POST请求必须设置如下两行
            conn.doOutput = true
            conn.doInput = true
            conn.readTimeout = TIMEOUT_IN_MILLIONS
            conn.connectTimeout = TIMEOUT_IN_MILLIONS

            if (param != null && param.trim { it <= ' ' } != "") {
                // 获取URLConnection对象对应的输出流
                out = PrintWriter(conn.outputStream)
                // 发送请求参数
                out.print(param)
                // flush输出流的缓冲
                out.flush()
            }
            // 定义BufferedReader输入流来读取URL的响应
            inpt = BufferedReader(InputStreamReader(conn.inputStream))
            var line: String?
            line = inpt.readLine()
            while (line != null) {
                result += line
                line = inpt.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
                inpt?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }// 使用finally块来关闭输出流、输入流
        return result
    }

    fun doDownloadAsyn(urlStr: String,filePath: String,fileName: String,stateCall: StateCall){

        object : Thread() {
            override fun run() {
                try {
                    doDownload(urlStr,filePath,fileName,stateCall)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }.start()
    }

    fun doDownload(urlStr: String,filePath: String,fileName: String,stateCall: StateCall){
        var url: URL?
        var conn: HttpURLConnection? = null
        var inpt: InputStream? = null
        var baos: FileOutputStream? = null
        try {
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            if (conn.responseCode == 200) {
                stateCall.onStateChange(0,0,fileName)
                val contentLength = conn.contentLength
                val perclen = contentLength/100
                inpt = conn.inputStream
                val output = File(filePath,fileName)
                baos = FileOutputStream(output)
                val buf = ByteArray(4096)
                var part=0
                var len = inpt.read(buf)
                var dnsz=len
                while (len!= -1) {
                    baos.write(buf, 0, len)
                    len = inpt.read(buf)
                    dnsz+=len;
                    val pct = dnsz/perclen
                    if(pct!=part){
                        stateCall.onStateChange(1,part,fileName)
                        part=pct;
                    }
                }
                baos.flush()
                stateCall.onStateChange(2,100,fileName)
            } else {
                throw RuntimeException(" responseCode is not 200 ... ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            try {
                baos?.close()
                inpt?.close()
            } catch (e: IOException) {
            }
            conn?.disconnect()
        }

    }
    fun doGetEncoding(urlStr: String,headParams: Map<String,String>?,encode: String?): String? {
        var url: URL?
        var conn: HttpURLConnection? = null
        var inpt: InputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            if(headParams!=null){
                for(ent in headParams.entries){
                    conn.setRequestProperty(ent.key,ent.value)
                }
            }
            if (conn.responseCode == 200) {
                inpt = conn.inputStream
                val reader = InputStreamReader(inpt,encode)
                return reader.readText()
            } else {
                throw RuntimeException(" responseCode is not 200 ... ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (inpt != null)
                    inpt.close()
            } catch (e: IOException) {
            }

            try {
                if (baos != null)
                    baos.close()
            } catch (e: IOException) {
            }

            conn?.disconnect()
        }

        return null

    }
}