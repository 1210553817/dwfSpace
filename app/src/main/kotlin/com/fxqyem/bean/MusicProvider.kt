package com.fxqyem.bean

import android.util.Log
import com.fxqyem.utils.HttpUtils
import com.fxqyem.vw.utilIsInt
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.apache.commons.codec.binary.Hex
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import java.math.BigInteger
import java.net.URLEncoder
import java.net.URLDecoder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Random

object MusicProvider {
	val TAG = "MusicProvider"
	/**
	 ***********wy start
	 */
	fun getWyLs(key: String, page: Int, size: Int): List<SongResult>? {
		try {
			val text = "{\"s\":\"$key\",\"type\":1,\"offset\":${(page - 1) * size},\"limit\":$size,\"total\":true}"
			val s = doPostWithCookie("http://music.163.com/weapi/cloudsearch/get/web?csrf_token=", text, true)
			val neteaseDatas: JsonObject? = Gson().fromJson(s, JsonObject::class.java)
			val songs: JsonArray? = neteaseDatas?.getAsJsonObject("result")?.getAsJsonArray("songs")
			var songResults: List<SongResult>? = null
			if (songs != null && songs.size() > 0) songResults = getWyLsByJson(songs)
			return songResults
		} catch (e: Exception) {
			Log.d(TAG,"getWyLs ${e.message}")
		}
        return null
	}

	//解析搜索时获取到的json，然后拼接成固定格式
	//具体每个返回标签的规范参考https://github.com/metowolf/NeteaseCloudMusicApi/wiki/%E7%BD%91%E6%98%93%E4%BA%91%E9%9F%B3%E4%B9%90API%E5%88%86%E6%9E%90---weapi
	@Throws(Exception::class)
	private fun getWyLsByJson(songs: JsonArray): List<SongResult>? {
		val gson = Gson()
		val list = ArrayList<SongResult>()
		for (indx in 0 until songs.size()) {
			val song = songs.get(indx)
			val songResult = SongResult()
			val songsBean: JsonObject? = gson.fromJson(song, JsonObject::class.java)

			val ar = songsBean?.getAsJsonArray("ar")
			var artistName = ""
			var artistId: String? = ""
			if(ar!=null) {
                for (anArindx in 0 until ar.size()) {
                    val anAr = ar.get(anArindx)
                    anAr ?: continue
                    val anarBean: JsonObject? = gson.fromJson(anAr, JsonObject::class.java)
					if(anArindx>0)artistName += " & "
                    artistName += anarBean?.getAsJsonPrimitive("name")?.asString
                    artistId = anarBean?.getAsJsonPrimitive("id")?.asString
                }
            }

			val songId = songsBean?.getAsJsonPrimitive("id")?.asString
			val songName = songsBean?.getAsJsonPrimitive("name")?.asString
			val songlink = "http://music.163.com/#/song?id=" + songId
			val albumId = songsBean?.getAsJsonObject("al")?.getAsJsonPrimitive("id")?.asString
			val albumName = songsBean?.getAsJsonObject("al")?.getAsJsonPrimitive("name")?.asString
			val albumPic = songsBean?.getAsJsonObject("al")?.getAsJsonPrimitive("picUrl")?.asString
			val songdt = songsBean?.getAsJsonPrimitive("dt")?.asInt ?: 0
			val mvId = songsBean?.getAsJsonPrimitive("mv")?.asString
			val maxbr = songsBean?.getAsJsonObject("privilege")?.getAsJsonPrimitive("maxbr")?.asInt

			songResult.length = secTotime(songdt / 1000)
			songResult.artistName = artistName
			songResult.artistId = artistId
			songResult.songId = songId
			songResult.songName = songName
			songResult.songLink = songlink
			songResult.albumId = albumId
			songResult.albumName = albumName
			songResult.albumPic = albumPic
			songResult.mvId = mvId
			songResult.lrcUrl = getWyLrcUrl(songId)
			songResult.qualityType = maxbr
			songResult.type = "wy"


			list.add(songResult)
		}
		return list
	}

//	//获取封面
//	private fun getWyPic(id: String?): String? {
//		val html: String?
//		try {
//			html = doGetWithCookie("http://music.163.com/api/song/detail/?ids=%5B$id%5D", true)
//			val gson = Gson()
//			val neteasePic: JsonObject? = gson.fromJson(html, JsonObject::class.java)
//			val songsBean: JsonObject? = gson.fromJson(neteasePic?.getAsJsonArray("songs")?.get(0), JsonObject::class.java)
//			return songsBean?.getAsJsonObject("album")?.getAsJsonPrimitive("blurPicUrl")?.asString
//		} catch (e: Exception) {
//			return ""
//		}
//	}

	//解析lrc歌词
	fun getWyLrc(sid: String?): String? {
		val url = "http://music.163.com/api/song/lyric?os=pc&id=$sid&lv=-1&kv=-1&tv=-1"
		val html = doGetWithCookie(url, true)
		html?.contains("uncollected") ?: return null
		val lrcEnt: JsonObject? = Gson().fromJson(html, JsonObject::class.java)
		return lrcEnt?.getAsJsonObject("lrc")?.getAsJsonPrimitive("lyric")?.asString
	}

	//获取lrc歌词
	private fun getWyLrcUrl(sid: String?)="http://music.163.com/api/song/lyric?os=pc&id=$sid&lv=-1&kv=-1&tv=-1"

	//获取音乐播放地址
	fun getWyPlayUrl(sid: String?, quality: Int): String? {
		var tag=""
		when(quality){
            0 -> tag="128000"
            1 -> tag="192000"
            2 -> tag="320000"
            3 -> return null
		}
		val heads = java.util.HashMap<String, String>()
		heads.put("X-REAL-IP", generateChinaRandomIP())
		heads.put("Referrer", "http://music.163.com/")
		var params="{\"method\":\"post\",\"url\":\"http://music.163.com/api/song/enhance/player/url\","+
				"\"params\":{\"ids\":[\"$sid\"],\"br\":$tag}}"
		var encrypted = encrypt(params)
		var result = HttpUtils.doPost("http://music.163.com/api/linux/forward", "eparams=$encrypted",heads )
		val gson = Gson()
		val urlEnt: JsonObject? = gson.fromJson(result, JsonObject::class.java)
		val urlDat = urlEnt?.getAsJsonArray("data")
		val urlItm: JsonObject? = gson.fromJson(urlDat?.get(0), JsonObject::class.java)
		if (200 == urlItm?.getAsJsonPrimitive("code")?.asInt ?: 0) {
			return urlItm?.getAsJsonPrimitive("url")?.asString
		}
		return null
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

	/**
	 ***********xm start
	 */
	//虾米不支持mv和无损解析
	@Throws(Exception::class)
	fun getXmLs(key: String, page: Int, size: Int): List<SongResult>? {
		val url = "http://api.xiami.com/web?v=2.0&app_key=1&key=$key&page=$page&limit=$size&r=search/songs"
		val s = doPostWithRefer(url, "http://m.xiami.com")
		if (s.isEmpty()) {
			return null //搜索歌曲失败
		}
		val gson = Gson()
		val dato: JsonObject? = gson.fromJson(s, JsonObject::class.java)
		val sdat = dato?.getAsJsonObject("data")
		val songs = sdat?.getAsJsonArray("songs")
		if (songs == null || songs.size() < 1) return null

		return getXmLsByJson(songs)
	}

	@Throws(Exception::class)
	private fun getXmLsByJson(songs: JsonArray): List<SongResult> {
		val list = ArrayList<SongResult>()
		for (indx in 0 until songs.size()) {
			val songsItm = songs.get(indx)
			val songsBean: JsonObject? = Gson().fromJson(songsItm, JsonObject::class.java)
			val songId = songsBean?.getAsJsonPrimitive("song_id")?.asString
            val songName = songsBean?.getAsJsonPrimitive("song_name")?.asString
            val songLink = "http://www.xiami.com/song/$songId"
            val artistId = songsBean?.getAsJsonPrimitive("artist_id")?.asString
            val artistName = songsBean?.getAsJsonPrimitive("artist_name")?.asString
            val albumId = songsBean?.getAsJsonPrimitive("album_id")?.asString
            val albumName = songsBean?.getAsJsonPrimitive("album_name")?.asString
            val picUrl = songsBean?.getAsJsonPrimitive("album_logo")?.asString

			val songResult = SongResult()
            songResult.songId = songId
            songResult.songName = songName
            songResult.songLink =songLink
            songResult.artistId = artistId
            songResult.artistName = artistName
            songResult.albumId = albumId
            songResult.albumName = albumName
            songResult.picUrl = picUrl
            songResult.type = "xm"
			list.add(songResult)
		}
		return list
	}
	fun getXmUrl(ids: String?,quality: Int, format: String): String? {
		var res = getXmUrlA(ids,quality, format)
		res?:return res
		if(res.startsWith("//"))res = "https:$res"
		return res
	}
	fun getXmUrlA(ids: String?,quality: Int, format: String): String? {
		ids?:return null
		val host = "www.xiami.com"
		val referer = "http://www.xiami.com/play?ids=/song/playlist/id/$ids/object_name/default/object_id/0"
		val tms = java.util.Date().time
		val url = "https://www.xiami.com/song/playlist/id/$ids/object_name/default/object_id/0/cat/json?_ksTS=${tms}_692&callback=jsonp693"

		val reqHeaders = HashMap<String, String>()
		reqHeaders.put("Host", host)
		reqHeaders.put("User-agent", userAgent)
		reqHeaders.put("Referer", referer)
		val restr = HttpUtils.doPost(url, "", reqHeaders)
		if(!restr.contains("jsonp693(")){
			return null
		}
		var html = restr.replace("jsonp693(","")
		html = html.substring(0,html.length-1)
		if (html.isEmpty())return null
		try {
			val gson = Gson()
			val xiamiIds: JsonObject? = gson.fromJson(html, JsonObject::class.java)
			val trackList = xiamiIds?.getAsJsonObject("data")?.getAsJsonArray("trackList")
            trackList?:return null
			if(trackList.size()<1)return null
			val xmEnt = trackList.get(0)
			val xmbean: JsonObject? = gson.fromJson(xmEnt, JsonObject::class.java)

            val songId = xmbean?.getAsJsonPrimitive("songId")?.asString

            when(format){
                "mp3"->{
                    if(0==quality) {
                        val location = xmbean?.getAsJsonPrimitive("location")?.asString
						location?: return null
						return getXiaMp3Url(location)
                    }else {
						songId?:return null
                        val hqUrl = getXmPlayUrl(songId)
						hqUrl?:return null
						return getXiaMp3Url(hqUrl)

                    }
                }
                "jpg"->{
					return xmbean?.getAsJsonPrimitive("pic")?.asString

                }
                "lrc"->{
                    return xmbean?.getAsJsonPrimitive("lyric")?.asString
                }

            }
		} catch (e: Exception) {
			Log.e(TAG,"getXmUrl error! ${e.message}")
		}
		return null
	}

	private fun getXmPlayUrl(songId: String): String? {
		val url = String.format("http://www.xiami.com/song/gethqsong/sid/%s", songId)
		val s = doPostWithRefer(url, "http://www.xiami.com/")
		val ret: JsonObject? = Gson().fromJson(s, JsonObject::class.java)
		return ret?.getAsJsonPrimitive("location")?.asString
	}


	/**
	 ***********kg start
	 */

	//酷狗支持无损和mv解析
	@Throws(Exception::class)
	fun getKgLs(key: String, page: Int, size: Int): List<SongResult>? {
        //http://songsearch.kugou.com/song_search_v2?callback=&clientver=&platform=WebFilter&keyword=%E5%91%A8%E6%9D%B0%E4%BC%A6&page=1&pagesize=30
        //http://www.kugou.com/yy/index.php?r=play/getdata&hash=3C3D93A5615FB42486CAB22024945264&album_id=1645030&_=1497972864535
		val url = "http://ioscdn.kugou.com/api/v3/search/song?keyword=" + URLEncoder.encode(key, "utf-8") + "&page=" + page + "&pagesize=" + size + "&showtype=10&plat=2&version=7910&tag=1&correct=1&privilege=1&sver=5"
		val s = doGetWithCookie(url)
		val gson = Gson()
		val kugouDatas: JsonObject? = gson.fromJson(s, JsonObject::class.java)
        kugouDatas?: return null
		if (kugouDatas.getAsJsonObject("data").getAsJsonPrimitive("total").asInt == 0) {
			return null//没有搜到歌曲
		}
		val data = kugouDatas.getAsJsonObject("data").getAsJsonArray("info")
		if (null == data || data.size() < 1) return null
		return getKgLsByJson(data)
	}

	//解析搜索时获取到的json，然后拼接成固定格式
	@Throws(Exception::class)
	private fun getKgLsByJson(songs: JsonArray): List<SongResult>? {
		val list = ArrayList<SongResult>()
		val gson = Gson()
		var i = 0
		while (i < songs.size()) {
			val songResult = SongResult()
			val songsBean: JsonObject? = gson.fromJson(songs.get(i), JsonObject::class.java)
			var songId = songsBean?.getAsJsonPrimitive("hash")?.asString
            songId=songId?:songsBean?.getAsJsonPrimitive("sqhash")?.asString
            songId=songId?:songsBean?.getAsJsonPrimitive("320hash")?.asString
			val songName = songsBean?.getAsJsonPrimitive("songname")?.asString
			val albumId = songsBean?.getAsJsonPrimitive("album_id")?.asString
			val albumName = songsBean?.getAsJsonPrimitive("album_name")?.asString?.replace("+", ";")
			val artistName = songsBean?.getAsJsonPrimitive("singername")?.asString?.replace("+", ";")
			val length = songsBean?.getAsJsonPrimitive("duration")?.asInt
			songResult.artistName = artistName
			songResult.songId = songId
			songResult.songName = songName
			songResult.length = if(length!=null)secTotime(length) else ""
//			if (!albumId!!.isEmpty() && isNumber(albumId)) {
//				songResult.picUrl = getKgUrl(albumId, "320", "jpg")
//			} else {
//				songResult.picUrl = getKgUrl(songId, "320", "jpg")
//			}

			songResult.albumId = albumId
			songResult.albumName = albumName
			songResult.type = "kg"
			list.add(songResult)
			i++
		}
		return list
	}
	
	fun getKgPlayUrl(id: String?, quality: Int): String?{
		val tag = when(quality) {
            0 -> "128"
            1 -> "320"
            2 -> "320"
            else -> "128"
        }
		 return getKgUrl(id,tag,"mp3")
	}

	fun getKgUrl(id: String?, quality: String, format: String): String? {
		try {
			var html: String?
			val gson = Gson()
			if (format == "jpg" && utilIsInt(id)) {
				html = doGetWithCookie("http://ioscdn.kugou.com/api/v3/album/info?albumid=$id&version=7910")
				html?: return null
				val kugouPic: JsonObject? = gson.fromJson(html, JsonObject::class.java)
				val pdt = kugouPic?.getAsJsonObject("data")
				pdt?:return null
				return pdt.getAsJsonPrimitive("imgurl")?.asString?.replace("{size}", "480")
			} else if (format == "jpg") {
				html = doGetWithCookie("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + id)
				html?:return null
				if (html.contains("hash error")) {
					return null
				}
				val json: JsonObject? = gson.fromJson(html, JsonObject::class.java)
				val songName = json?.getAsJsonPrimitive("fileName")?.asString
				val snm = songName?.split(("-").toRegex())?.dropLastWhile({ it.isEmpty() })
				snm?:return null
				val snmarr = snm.toTypedArray()[0].trim({ it <= ' ' })
				html = doGetWithCookie(snmarr + "&size=480")
				val imgJson: JsonObject? = gson.fromJson(html, JsonObject::class.java)
				return imgJson?.getAsJsonPrimitive("url")?.asString
			}
			if (format == "lrc") {
				html = doGetWithCookie("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + id)
				html?:return null
				if (html.contains("hash error")) {
					return null
				}
				val kugouLrc: JsonObject? = gson.fromJson(html, JsonObject::class.java)
				val songName = kugouLrc?.getAsJsonPrimitive("songName")?.asString
				val lrclen = kugouLrc?.getAsJsonPrimitive("timeLength")?.asString
				val len = lrclen + "000"
				if (format == "lrc") {
					html = doGetWithCookie("http://m.kugou.com/app/i/krc.php?cmd=100&keyword=" + songName +
							"&hash=" + id + "&timelength=" + len + "&d=0.38664927426725626", false)
                    html?:return null
					return "[ti:$songName]\n[by: FXQY]\n$html"
				}
			}
			if (format == "mp3") {
                html = doGetWithCookie("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + id)
                html?:return null
                if (html.contains("hash error")) {
                    return null
                }
				val kugouMp3Url: JsonObject? = gson.fromJson(html, JsonObject::class.java)
				return kugouMp3Url?.getAsJsonPrimitive("url")?.asString
			}
			if (format == "mp4") {
				/**
				val key = getMD5(id + "kugoumvcloud")
				html = doGetWithCookie("http://trackermv.kugou.com/interface/index?cmd=100&pid=6&ext=mp4&hash=" + id +
				"&quality=-1&key=" + key + "&backupdomain=1")
				// /interface/index?cmd=100&pid=6&ext=mp4&hash=1f1668e15ee298b4d3ee630cef0c6a90&quality=-1&key=0cda6579ff6a8822d5d5a9e504bbcc57&backupdomain=1
				if (html?.contains("Bad key")?:true)
				{
				return ""
				}
				val kugouMv: JsonObject = gson.fromJson(html, JsonObject::class.java)
				val mvdata = kugouMv?.getAsJsonObject("mvdata")
				if (quality == "hd")
				{
				val rq = mvdata?.getAsJsonObject("rq")?.getAsJsonPrimitive("downurl")?.asString
				if (rq != null && !rq.isEmpty())
				{
				return rq
				}
				val sq = mvdata?.getAsJsonObject("sq")?.getAsJsonPrimitive("downurl")?.asString
				if (sq != null && !sq.isEmpty())
				{
				return sq
				}
				val hd = mvdata?.getAsJsonObject("hd")?.getAsJsonPrimitive("downurl")?.asString
				if (hd != null && !hd.isEmpty())
				{
				return hd
				}
				val sd = mvdata?.getAsJsonObject("sd")?.getAsJsonPrimitive("downurl")?.asString
				if (sd != null && !sd.isEmpty())
				{
				return sd
				}
				}
				else
				{
				val sq = mvdata?.getAsJsonObject("sq")?.getAsJsonPrimitive("downurl")?.asString
				if (sq != null && !sq.isEmpty())
				{
				return sq
				}
				val hd = mvdata?.getAsJsonObject("hd")?.getAsJsonPrimitive("downurl")?.asString
				if (hd != null && !hd.isEmpty())
				{
				return hd
				}
				val sd = mvdata?.getAsJsonObject("sd")?.getAsJsonPrimitive("downurl")?.asString
				if (sd != null && !sd.isEmpty())
				{
				return sd
				}
				}
				 */
			}
		} catch (ignored: Exception) {
		}
		return null
	}
	
	private fun getKgLrc(sid: String)=getKgUrl(sid,"","lrc")

	/**
	 ***********qq start
	 */
	//腾讯支持无损和mv解析
	@Throws(Exception::class)
	fun getQqLs(key: String, page: Int, size: Int): List<SongResult>? {
		val url = "http://soso.music.qq.com/fcgi-bin/search_cp?aggr=0&catZhida=0&lossless=1&sem=1&w=" + urlEncode(key) + "&n=" + size + "&t=0&p=" + page + "&remoteplace=sizer.yqqlist.song&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
		var html = doGetWithCookie(url)
		html?:return null
		html = html.substring(0, html.length - 1).replace("callback(", "")
		val tencentDatas: JsonObject? = Gson().fromJson(html, JsonObject::class.java)
		val songs = tencentDatas?.getAsJsonObject("data")?.getAsJsonObject("song")
		val sls = songs?.getAsJsonArray("list")
		if (sls == null || sls.size() < 1) return null
		return getQqLsByJson(sls)
	}

	//解析搜索时获取到的json，然后拼接成固定格式
	@Throws(Exception::class)
	private fun getQqLsByJson(songs: JsonArray): List<SongResult> {
		val list = ArrayList<SongResult>()
		for (indx in 0 until songs.size()) {
			val song = songs.get(indx)
			val songResult = SongResult()
			val songsBean: JsonObject? = Gson().fromJson(song, JsonObject::class.java)
			val songId = songsBean?.getAsJsonPrimitive("songid")?.asString
			val songName = songsBean?.getAsJsonPrimitive("songname")?.asString
			val songlink = "http://y.qq.com/#type=song&mid=" + songsBean?.getAsJsonPrimitive("songmid")?.asString
			val arEnt = songsBean?.getAsJsonArray("singer")?.get(0)
			val arEnto: JsonObject? = Gson().fromJson(arEnt, JsonObject::class.java)
			val artistId = arEnto?.getAsJsonPrimitive("id")?.asString
			val artistName = arEnto?.getAsJsonPrimitive("name")?.asString
			val albumId = songsBean?.getAsJsonPrimitive("albumid")?.asString
			val albumName = songsBean?.getAsJsonPrimitive("albumname")?.asString
			//val mvId = songsBean?.getAsJsonPrimitive("vid")?.asString
			songResult.artistName = artistName
			songResult.artistId = artistId
			songResult.songId = songId
			songResult.songName = songName
			songResult.songLink = songlink
			songResult.albumId = albumId
			songResult.albumName = albumName

			val mid = songsBean?.getAsJsonPrimitive("mediaMid")?.asString ?: (songsBean?.getAsJsonPrimitive("media_mid")?.asString)
			songResult.qqmid = mid
			// if (songsBean.getSizeflac() != 0) {
			// songResult.setBitRate("无损");
			// songResult.setFlacUrl("http://dl.stream.qqmusic.qq.com/F000" + mid + ".flac?vkey=F7B5C260CB57AE3339B157A9443C33A01043A9AB6A8CFC7600535EEC4FDA13A31B1C94259C6A655FAB2A255A4C107F6D3A2FB1F2308ABE60&guid=YYFM&uin=123456&fromtag=53");
			// songResult.setFlacUrl("http://dl.stream.qqmusic.qq.com/F000" + mid + ".flac?vkey=F7B5C260CB57AE3339B157A9443C33A01043A9AB6A8CFC7600535EEC4FDA13A31B1C94259C6A655FAB2A255A4C107F6D3A2FB1F2308ABE60&guid=YYFM&uin=123456&fromtag=53");
			// }
			//ape为A000
			val albumMid = songsBean?.getAsJsonPrimitive("albummid")?.asString
			if (albumMid != null && albumMid.length > 2) {
				songResult.picUrl = "http://i.gtimg.cn/music/photo/mid_album_500/" + albumMid.substring(albumMid.length - 2, albumMid.length - 1) + "/" + albumMid.substring(albumMid.length - 1) + "/" + albumMid + ".jpg"
			}
			songResult.length = secTotime(songsBean?.getAsJsonPrimitive("interval")?.asInt ?: 0)
			songResult.type = "qq"
			// songResult.setLrcUrl(GetLrcUrl(SongId, SongName, artistName)); //暂不去??歌词，直接解析浪费性能
			list.add(songResult)
		}
		return list
	}

	private fun getQqPlayUrl(mid: String?, quality: Int): String? {
		var prefix=""
		var tag=""
		when(quality) {
            0 -> {
                prefix = "M500"
                tag = "0"
            }
			1 ->{
                prefix = "O600"
                tag = "50"
            }
			2 -> {
                prefix = "M800"
                tag = "50"
            }
			3 ->{
                return null
            }
        }
		
		val vtm = Random(System.currentTimeMillis()).nextLong()
		val key = getQqKey((vtm).toString())
		key?:return null
		return "http://ws.stream.qqmusic.qq.com/$prefix$mid.mp3?vkey=$key&guid=$vtm&fromtag=$tag"
	}

	//  private fun getQqMvUrl(id:String, quality:String):String {
//    try
//    {
//      val html = NetUtil.doGetWithCookie("http://vv.video.qq.com/getinfo?vid=" + id + "&platform=11&charge=1&otype=json")
//      html = html.substring(0, html.length - 1).replace("QZOutputJson=", "")
//      val gson = Gson()
//      val tencentMvData = gson.fromJson(html, TencentMvData::class.java)
//      if (tencentMvData.getFl() == null)
//      {
//        return ""
//      }
//      val fi = tencentMvData.getFl().getFi()
//      val dic = HashMap()
//      val count = fi.size()
//      for (fiBean in fi)
//      {
//        dic.put(fiBean.getName(), fiBean.getId())
//      }
//      val mvID:Int
//      if (quality == "hd")
//      when (count) {
//        4 -> mvID = dic.get("fhd")
//        3 -> mvID = dic.get("shd")
//        2 -> mvID = dic.get("hd")
//        else -> mvID = dic.get("sd")
//      }
//      else
//      {
//        when (count) {
//          4 -> mvID = dic.get("shd")
//          3 -> mvID = dic.get("hd")
//          else -> mvID = dic.get("sd")
//        }
//      }
//      val vkey = GetVkey(mvID, id)
//      val fn = id + ".p" + (mvID - 10000) + ".1.mp4"
//      return tencentMvData.getVl().getVi().get(0).getUl().getUi().get(0).getUrl() + fn + "?vkey=" + vkey
//    }
//    catch (e:Exception) {
//      return ""
//    }
//  }
//  private fun getQqVkey(id:Int, videoId:String):String {
//    try
//    {
//      val fn = videoId + ".p" + (id - 10000) + ".1.mp4"
//      val url = "http://vv.video.qq.com/getkey?format=" + id + "&otype=json&vid=" + videoId +
//      "&platform=11&charge=1&filename=" + fn
//      val html = NetUtil.doGetWithCookie(url)
//      if (html.isEmpty())
//      {
//        return ""
//      }
//      html = html.substring(0, html.length - 1).replace("QZOutputJson=", "")
//      val gson = Gson()
//      val tencentMvKey = gson.fromJson(html, TencentMvKey::class.java)
//      return tencentMvKey.getKey()
//    }
//    catch (e:Exception) {
//      return ""
//    }
//  }
	private fun getQqKey(time: String): String? {
		try {
			var html = doGetWithCookie("http://base.music.qq.com/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=" + time)
			html = html?.replace("jsonCallback(", "")?.replace(");", "")
			val gson = Gson()
			val tencentGetKey: JsonObject? = gson.fromJson(html, JsonObject::class.java)
			return tencentGetKey?.getAsJsonPrimitive("key")?.asString
		} catch (e: Exception) {
			Log.e(TAG,e.message)
		}
        return null
	}

//	fun getQqLrc(sid: String):String?{
//		var restr: String?=null
//		if(!isNumber(sid)) return null
//
//		try{
//			val id=Integer.parseInt(sid)
//			val midn=id%100
//            val prUrl="http://music.qq.com/miniportal/static/lyric/$midn/$id.xml"
//			restr= HttpUtils.doGetEncoding(prUrl,null,"GBK")
//			if(restr==null) return restr
//			val regexa = Regex(""".+<!\[CDATA\[([\s\S]*)\]\]>.*""")
//		    val result = regexa.find(restr)
//		    if (result != null) {
//				val ctt = result.groupValues[1]
//				return ctt.replace("&apos;", "'")
//		    }else{
//				return null
//			}
//		}catch(e: Exception){
//			return null
//		}
//
//	}

    //https://github.com/xiangwenhu/vbox/blob/master/
    private fun getQqLrcA(qsongMid: String):String?{
        val cyqqBaseUrl = "https://c.y.qq.com"
        val commonParams = "g_tk=5381&loginUin=0&hostUin=0&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
        val urlSuffix = "$cyqqBaseUrl/lyric/fcgi-bin/fcg_query_lyric_new.fcg?$commonParams&format=json"
        val realUrl ="$urlSuffix&songmid=$qsongMid&pcachetime=${java.util.Date().time}"
        try{
            val hdMap = HashMap<String,String>()
            hdMap.put("Accept","application/json")
            hdMap.put("Content-Type","application/json")
            hdMap.put("referer","https://y.qq.com/portal/player.html")
            val qlre = HttpUtils.doGetEncoding(realUrl,hdMap,"utf-8")
            val qlrcn =qlre?.replace("(MusicJsonCallback\\()|(\\}\\))".toRegex(),"")
            val plreo: JsonObject? = Gson().fromJson(qlrcn+"}",JsonObject::class.java)
            val lyricStr = plreo?.getAsJsonPrimitive("lyric")?.asString
            if(lyricStr==null||lyricStr.length<10) return null
            val destr: ByteArray = BASE64Decoder().decodeBuffer(lyricStr)
            return String(destr)
        }catch(e: Exception){
            return null
        }

    }
    /**
	 * ********baidu start**********
     */
    fun getBdLs(key: String, page: Int, size: Int): List<SongResult>? {
        val enck = urlEncode(key)
        val url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&page_no=$page&page_size=$size&query=$enck"
        val html = HttpUtils.doGetEncoding(url,null,"utf-8")
		html?:return null
        val tencentDatas: JsonObject? = Gson().fromJson(html, JsonObject::class.java)
        val songs = tencentDatas?.getAsJsonArray("song_list")
        val artist = tencentDatas?.getAsJsonObject("artist")
        if (songs == null || songs.size() < 1) return null
        return getBdLsByJson(songs,artist)
    }

	private fun getBdLsByJson(songs: JsonArray,arObj: JsonObject?): List<SongResult>?{
        val list = ArrayList<SongResult>()
        for (indx in 0 until songs.size()) {
            val song = songs.get(indx)
            val songResult = SongResult()
            val songsBean: JsonObject? = Gson().fromJson(song, JsonObject::class.java)
            val songId = songsBean?.getAsJsonPrimitive("song_id")?.asString
            val songName = songsBean?.getAsJsonPrimitive("title")?.asString
            val songLink = ""
            val artistId = songsBean?.getAsJsonPrimitive("artist_id")?.asString
            val artistName = arObj?.getAsJsonPrimitive("name")?.asString
            val albumId = songsBean?.getAsJsonPrimitive("album_id")?.asString
            val albumName = songsBean?.getAsJsonPrimitive("album_title")?.asString
			val picUrl = arObj?.getAsJsonObject("avatar")?.getAsJsonPrimitive("big")?.asString
            songResult.songId = songId
            songResult.songName = songName?.replace("</?[^<]+>".toRegex(),"")
            songResult.songLink = songLink
            songResult.artistName = artistName?.replace("</?[^<]+>".toRegex(),"")
            songResult.artistId = artistId
            songResult.albumId = albumId
            songResult.albumName = albumName?.replace("</?[^<]+>".toRegex(),"")
			songResult.picUrl = picUrl
			songResult.type = "bd"
            list.add(songResult)
        }
        return list

	}

	fun getBdUrl(ids: String?,quality: Int, format: String): String? {
        ids?:return null
		val quaStr=if(quality==0) "baidu.ting.song.play" else "baidu.ting.song.playAAC"
        val turl = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=$quaStr&songid=$ids"
        val html = HttpUtils.doGetEncoding(turl,null,"utf-8")
        html?:return null
        try {
            val gson = Gson()
            val bdsdtl: JsonObject? = gson.fromJson(html, JsonObject::class.java)
            val fileinfo = bdsdtl?.getAsJsonObject("bitrate")
            val songinfo = bdsdtl?.getAsJsonObject("songinfo")

            when(format){
                "mp3"->{
					fileinfo?:return null
					return fileinfo.getAsJsonPrimitive("file_link")?.asString
                }
                "jpg"->{
                    songinfo?:return null
                    return songinfo.getAsJsonPrimitive("pic_big")?.asString

                }
                "lrc"->{
                    songinfo?:return null
                    return songinfo.getAsJsonPrimitive("lrclink")?.asString
                }

            }
        } catch (e: Exception) {
			Log.d(TAG,"getBdUrl ${e.message}")
        }
        return null
    }

	private fun getBdLrc(id: String?): String?{
        id?:return null
        val quaStr="baidu.ting.song.lry"
        val turl = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=$quaStr&songid=$id"
		val lyrs = HttpUtils.doGetEncoding(turl,null,"utf-8")
		val lyro: JsonObject? = Gson().fromJson(lyrs,JsonObject::class.java)

		return lyro?.getAsJsonPrimitive("lrcContent")?.asString
	}

    /**
     * ********5sing ws start**********
     */
    fun getWsLs(key: String, page: Int, size: Int): List<SongResult>? {
        val enck = urlEncode(key)
        val url = "http://goapi.5sing.kugou.com/search/search?t=0&filterType=3&sortType=0&version=6.0.3&ps=$size&pn=$page&k=$enck"
        val html = HttpUtils.doGetEncoding(url,null,"utf-8")
        html?:return null
        val tencentDatas: JsonObject? = Gson().fromJson(html, JsonObject::class.java)
        val songs = tencentDatas?.getAsJsonObject("data")?.getAsJsonArray("songArray")
        if (songs == null || songs.size() < 1) return null
        return getWsLsByJson(songs)
    }

    private fun getWsLsByJson(songs: JsonArray): List<SongResult>?{
        val list = ArrayList<SongResult>()
        for (indx in 0 until songs.size()) {
            val song = songs.get(indx)
            val songsBean: JsonObject? = Gson().fromJson(song, JsonObject::class.java)
            val songId = songsBean?.getAsJsonPrimitive("songId")?.asString
            val songName = songsBean?.getAsJsonPrimitive("songName")?.asString
            val artistId = songsBean?.getAsJsonPrimitive("singerId")?.asString
            val singer = songsBean?.get("singer")
            var artistName = ""
			if(singer!=null && !singer.isJsonNull) {
                artistName = songsBean.getAsJsonPrimitive("singer")?.asString ?: ""
            }
            val songResult = SongResult()
            songResult.songId = songId
            songResult.songName = songName
            songResult.artistName = artistName
            songResult.artistId = artistId
            songResult.type = "ws"
            list.add(songResult)
        }
        return list

    }

    fun getWsPlayUrl(ids: String?): String? {
        ids?:return null
        val turl = "http://mobileapi.5sing.kugou.com/song/getSongUrl?songtype=bz&version=6.0.3&songid=$ids"
        val html = HttpUtils.doGetEncoding(turl,null,"utf-8")
        html?:return null
        try {
            val bdsdtl: JsonObject? = Gson().fromJson(html, JsonObject::class.java)
            val finfo = bdsdtl?.getAsJsonObject("data")
            finfo?:return null
            var furl=finfo.getAsJsonPrimitive("lqurl")?.asString
            furl=furl?:finfo.getAsJsonPrimitive("squrl")?.asString
            furl=furl?:finfo.getAsJsonPrimitive("hqurl")?.asString
            return furl
        } catch (e: Exception) {
			Log.d(TAG,"getWsPlayUrl ${e.message}")
        }
        return null
    }

	/**
	 ***********Common start
	 */
	private val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
	private val cookieStr = "__remember_me=true; MUSIC_U=5f9d910d66cb2440037d1c68e6972ebb9f15308b56bfeaa4545d34fbabf71e0f36b9357ab7f474595690d369e01fbb9741049cea1c6bb9b6; __csrf=8ea789fbbf78b50e6b64b5ebbb786176; os=uwp; osver=10.0.10586.318; appver=1.2.1; deviceId=0e4f13d2d2ccbbf31806327bd4724043"
	private val modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
			"b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
			"104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
			"575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
			"3ece0462db0a22b8e7"
	private val nonce = "0CoJUm6Qyw8W8jud"
	private val pubKey = "010001"

	private fun doPostWithCookie(url: String, text: String?, needCookie: Boolean): String {
		val param = encryptedRequest(text)
		val reqHeaders = HashMap<String, String>()
		reqHeaders.put("User-agent", userAgent)
		if (needCookie) {
			reqHeaders.put("Cookie", cookieStr)
		}
		return HttpUtils.doPost(url, param, reqHeaders)
	}

	private fun doGetWithCookie(url: String, needCookie: Boolean = false): String? {
		val reqHeaders = HashMap<String, String>()
		reqHeaders.put("User-agent", userAgent)
		if (needCookie) {
			reqHeaders.put("Cookie", cookieStr)
		}
		return HttpUtils.doGet(url, reqHeaders)
	}

	private fun doPostWithRefer(url: String, refer: String): String {
		val reqHeaders = HashMap<String, String>()
		reqHeaders.put("User-agent", userAgent)
		reqHeaders.put("Referer", refer)
		return HttpUtils.doPost(url, "", reqHeaders)
	}

	private fun doPostData(url: String, params: HashMap<String, String>): String {
		val reqHeaders = HashMap<String, String>()
		reqHeaders.put("User-agent", userAgent)
		val sbd = StringBuilder()
		var k = 0
		for ((key, value) in params) {
			if (k > 0) sbd.append("&")
			sbd.append("$key=$value")
			k++
		}
		return HttpUtils.doPost(url, "", reqHeaders)
	}

	//based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
	private fun encryptedRequest(text: String?): String {
		val secKey = createSecretKey(16)
		val encText = aesEncrypt(aesEncrypt(text, nonce), secKey)
		val encSecKey = rsaEncrypt(secKey, pubKey, modulus)
		try {
			assert(encText != null)
			return "params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + URLEncoder.encode(encSecKey, "UTF-8")
		} catch (e: Exception) {
			Log.e(TAG,e.message)
		}
        return ""
	}

	private fun aesEncrypt(text: String?, key: String): String? {
		try {
			val iv = IvParameterSpec("0102030405060708".toByteArray(charset("UTF-8")))
			val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
			val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
			val encrypted = cipher.doFinal(text?.toByteArray())
			return BASE64Encoder().encode(encrypted)
		} catch (ex: Exception) {
            Log.e(TAG,ex.message)
		}
        return null
	}

	//based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
	private fun rsaEncrypt(text: String?, pubKey: String, modulus: String): String {
		val texta = StringBuilder(text).reverse().toString()
		val rs = BigInteger(String.format("%x", BigInteger(1, texta.toByteArray())), 16)
				.modPow(BigInteger(pubKey, 16), BigInteger(modulus, 16))
		var r = rs.toString(16)
		if (r.length >= 256) {
			return r.substring(r.length - 256, r.length)
		} else {
			while (r.length < 256) {
				r = "0$r"
			}
		}
        return r
	}

	//based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
	private fun createSecretKey(i: Int) = getRandomString(i)

	private fun getRandom(count: Int)=Math.round(Math.random() * (count)).toInt()


	private val string = "0123456789abcde"
	private fun getRandomString(length: Int): String {
		val sb = StringBuffer()
		val len = string.length
		for (i in 0 until length) {
			sb.append(string[getRandom(len - 1)])
		}
		return sb.toString()
	}

	private fun secTotime(seconds: Int): String {
		val sb = StringBuilder()
		var temp = seconds / 3600
        var tmp:String = if(temp < 10) "0$temp" else "$temp"
		sb.append("$tmp:")
		temp = seconds % 3600 / 60
        tmp = if(temp < 10) "0$temp:" else "$temp:"
		sb.append(tmp)
		temp = seconds % 3600 % 60
        tmp = if(temp < 10) "0$temp" else "$temp"
		sb.append(tmp)
		return sb.toString()
	}

	private fun urlEncode(str: String): String {
		try {
			return URLEncoder.encode(str, "UTF-8")
		} catch (e: Exception) {
			Log.e(TAG,e.message)
		}
        return ""
	}

	private fun getXiaMp3Url(raw: String): String {
		val url = ""
		try {
			val num = Integer.parseInt(raw.substring(0, 1))
			val str = raw.substring(1)
			val num2 = str.length % num
			val length = Math.ceil((str.length.toDouble()) / (num.toDouble())).toInt()
			val strArray = arrayOfNulls<String>(num)
			var startIndex = 0
			for (i in 0 until num) {
				if (i < num2) {
					strArray[i] = str.substring(startIndex, startIndex + length)
					startIndex = startIndex + length
				} else if (num2 == 0) {
					strArray[i] = str.substring(startIndex, startIndex + length)
					startIndex = startIndex + length
				} else {
					strArray[i] = str.substring(startIndex, startIndex + length - 1)
					startIndex = startIndex + length - 1
				}
			}
			val builder = StringBuilder()
			if (num2 == 0) {
				for (j in 0 until length) {
					for (k in 0 until num) {
						builder.append(strArray[k]?.substring(j, j + 1))
					}
				}
			} else {
				for (m in 0 until length) {
					if (m == (length - 1)) {
						for (n in 0 until num2) {
							builder.append(strArray[n]?.substring(m, m + 1))
						}
					} else {
						for (num10 in 0 until num) {
							builder.append(strArray[num10]?.substring(m, m + 1))
						}
					}
				}
			}
			val input = URLDecoder.decode(builder.toString(), "UTF-8")
			if (input != null) {
				val one = input.replace(("\\^").toRegex(), "0")
				val two = one.replace(("\\+").toRegex(), " ")
				return two.replace(("\\.mp$").toRegex(), ".mp3")
			}
		} catch (e: Exception) {
			Log.d(TAG,"getXiaMp3Url ${e.message}")
		}

		return url
	}

    fun getPlayUrl(rst: SongResult): String?{
        val tp = rst.type
        val sid = rst.songId

        when (tp) {
            "wy" -> return getWyPlayUrl(sid,0)
            "xm" -> return getXmUrl(sid,0,"mp3")
            "kg" -> return getKgPlayUrl(sid,0)
            "qq" ->{
                val qqmid = rst.qqmid
                return getQqPlayUrl(qqmid,0)
            }
            "bd" ->{
                return getBdUrl(sid,0,"mp3")
            }
            "ws" ->{
                return getWsPlayUrl(sid)
            }
        }

        return null
    }

    fun getLrcText(rst: SongResult): String?{
        val tp = rst.type
        val sid = rst.songId

        when (tp) {
            "wy" ->{
                sid?:return null
                return getWyLrc(sid)
            }
            "xm" ->{
                sid?:return null
				val llrc = getXmUrl(sid,0,"lrc")
				llrc?:return null
				return  HttpUtils.doGetEncoding(llrc,null,"utf-8")
            }
            "kg" -> {
                sid?:return null
                return getKgLrc(sid)
            }
            "qq" ->{
				val qmid = rst.qqmid
                qmid?:return null
                return getQqLrcA(qmid)
            }
            "bd" ->{
                sid?:return null
                return  getBdLrc(sid)
            }

        }

        return null
    }


}
