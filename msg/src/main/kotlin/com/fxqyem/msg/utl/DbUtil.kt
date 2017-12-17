package com.fxqyem.msg.utl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.fxqyem.msg.ben.AppConstants
import com.fxqyem.msg.ent.MsgEnt
import com.fxqyem.msg.vw.utilIsEmpty
import java.io.File
import java.sql.Timestamp
import java.util.*

class DbUtil(private val context: Context) {
    var db: SQLiteDatabase? = null
    private var dbHelper: SQLiteOpenHelper? = null

    /**
     * 创建SD卡数据库

     * @param sdBasePath SD卡根路径
     * *
     * @param dbPath     数据库文件夹
     * *
     * @param dbName     数据库文件名
     * *
     * @return
     */
    fun openSdDatabase(sdBasePath: String, dbPath: String, dbName: String): SQLiteDatabase? {
        if (!SDCardUtils.isSDCardEnable) {
            return null
        }
        try {
            val databaseFilename = "${sdBasePath}/${dbPath}/${dbName}"
            val dir = File("${sdBasePath}/${dbPath}")
            if (!dir.exists())
                dir.mkdirs()
            val dbFile = File(databaseFilename)
            if (!dbFile.exists()) {
                dbFile.createNewFile()
            }
            db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null)
        } catch (e: Exception) {
            Log.e(TAG, "openSdDatabase Sql error! \n" + e.message)
        }

        return db
    }

    /**
     * 创建应用内部数据库

     * @param context
     * *
     * @param name
     * *
     * @param factory
     * *
     * @param version
     * *
     * @return
     */
    fun openInnerDatabase(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteDatabase? {
        dbHelper = DbHelper(context, name, factory, version)
        db = dbHelper?.writableDatabase
        return db
    }

    fun query(tableName: String?, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, groupBy: String?, having: String?,
              orderBy: String,limit: String): Cursor? {
        var cursor: Cursor? = null
        try {
            if (db == null)
                return null
            cursor = db?.query(tableName, columns, selection, selectionArgs,
                    groupBy, having, orderBy,limit)
        } catch (e: Exception) {
            Log.e(TAG, "query Sql error! \n" + e.message)
        }

        return cursor
    }

    fun insert(tableName: String, values: ContentValues): Int {
        var flag = -1
        try {
            if (db == null)
                return flag
            db!!.insert(tableName, null, values)
            val cursor = db?.rawQuery("select last_insert_rowid() from " + tableName, null)
            cursor?.moveToFirst()
            val inid = cursor?.getInt(0)
            flag = inid?:flag
            cursor?.close()
        } catch (e: Exception) {
            Log.e(TAG, "insert Sql error! \n" + e.message)

        }

        return flag
    }

    fun insertUseMap(tableName: String?, parameter: Map<*, *>): Int {
        var flag = -1
        val columns = StringBuilder()
        val values = StringBuilder()
        var i = 0
        val it = parameter.keys.iterator()
        while (it.hasNext()) {
            val key = it.next() as String
            if (i > 0) {
                columns.append(",")
                values.append(",")
            }
            columns.append(key)
            values.append(parameter[key])
            i++
        }
        val sql = "insert into $tableName ($columns) values ($values)"
        try {
            if (db == null)
                return flag
            db!!.execSQL(sql)
            val cursor = db?.rawQuery("select last_insert_rowid() from $tableName", null)
            cursor?:return flag
            cursor.moveToFirst()
            val inid = cursor.getInt(0)
            flag = inid
            cursor?.close()
        } catch (e: Exception) {
            Log.e(TAG, "insertUseMap Sql error! \n" + e.message)
        }

        return flag
    }

    fun update(tableName: String, values: ContentValues,
               whereClause: String, whereArgs: Array<String>): Int {
        var count = -1
        try {
            if (db == null)
                return count
            count = db?.update(tableName, values, whereClause, whereArgs)?:count
            return count
        } catch (e: Exception) {
            Log.e(TAG, "update Sql error! \n" + e.message)
        }

        return count
    }

    fun delete(tableName: String?, whereClause: String?, whereArgs: Array<String>?): Int {
        var count = -1
        try {
            count = db?.delete(tableName, whereClause, whereArgs)?:count
        } catch (e: Exception) {
            Log.e(TAG, "delete Sql error! \n" + e.message)
        }

        return count
    }

    fun doForSql(sql: String) {
        try {
            if (db == null)
                return
            db?.execSQL(sql)
        } catch (e: Exception) {
            Log.e(TAG, "doForSql error! \n" + e.message)
        }

        return
    }

    fun queryForSql(sql: String, selectionArgs: Array<String>): Cursor? {
        var cursor: Cursor? = null
        try {
            if (db == null)
                return cursor
            cursor = db?.rawQuery(sql, selectionArgs)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cursor
    }

    fun close() {
        if (db != null) db!!.close()
        if (dbHelper != null) dbHelper!!.close()
    }


    fun OpenDbCommon(sdBasePath: String, dbPath: String, dbName: String, context: Context?, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase? {
        if (SDCardUtils.isSDCardEnable) {
            openSdDatabase(sdBasePath, dbPath, dbName)
        } else {
            openInnerDatabase(context, dbName, factory,DB_VERSION)
        }
        val cdb = db
        cdb?:return db
        if(AppConstants.DB_NAME_MSGDATAS==dbName) {
            initMsgTables(cdb)
        }
        return db
    }

    private fun initMsgTables(cdb:SQLiteDatabase) {
        //Log.d(TAG,"DatabaseNewVersion: $DB_VERSION, DatabaseVersion: ${cdb.version}<-------------------------------------------------------------")
        if (DB_VERSION<=cdb.version) {
            return
        }
        db?.execSQL(arrayOf(
                "CREATE TABLE IF NOT EXISTS ${AppConstants.DB_TBNAME_MSGLS} (" ,
                "mid integer primary key autoincrement ," ,
                "type integer," ,
                "mtype integer," ,
                "tit varchar," ,
                "sub varchar," ,
                "addt text," ,
                "ip varchar," ,
                "path varchar," ,
                "pno varchar," ,
                "fname varchar," ,
                "fsize varchar," ,
                "time timestamp default (datetime('now','localtime'))" ,
                ")"
        ).joinToString(separator = ""))
//        db?.execSQL(arrayOf(
//                "CREATE TABLE IF NOT EXISTS ${AppConstants.DB_TBNAME_CHATLS} (" ,
//                "cid integer primary key autoincrement ," ,
//                "tit varchar," ,
//                "host varchar," ,
//                "ip varchar," ,
//                "time timestamp default (datetime('now','localtime'))" ,
//                ")"
//        ).joinToString(separator = ""))


        cdb.version = DB_VERSION
    }
    /**msg Datebase */
    fun openMsgDb(): SQLiteDatabase?{
            return OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_MSGDATAS, context, null)
    }

    fun getMsgls(ip: String?,offset: Int,limit: Int): ArrayList<MsgEnt>{
        val msgLs = ArrayList<MsgEnt>()
        ip?:return msgLs
        openMsgDb()
        val c = query(AppConstants.DB_TBNAME_MSGLS,
                arrayOf("mid", "type", "mtype", "tit", "sub", "addt", "ip", "path","pno","fname","fsize","time"),
                "ip=?", arrayOf(ip), null, null,"mid desc","$offset,$limit")
        while (c != null && c.moveToNext()) {
            val ent = MsgEnt()
            ent.id = c.getLong(c.getColumnIndex("mid"))
            ent.type = c.getInt(c.getColumnIndex("type"))
            ent.mtype = c.getInt(c.getColumnIndex("mtype"))
            ent.tit = c.getString(c.getColumnIndex("tit"))
            ent.sub = c.getString(c.getColumnIndex("sub"))
            ent.add = c.getString(c.getColumnIndex("addt"))
            ent.ip = c.getString(c.getColumnIndex("ip"))
            ent.path = c.getString(c.getColumnIndex("path"))
            ent.pno = c.getString(c.getColumnIndex("pno"))
            ent.fname = c.getString(c.getColumnIndex("fname"))
            ent.fsize = c.getString(c.getColumnIndex("fsize"))
            ent.time = c.getString(c.getColumnIndex("time"))
            msgLs?.add(ent)
        }
        c?.close()
        close()
        msgLs.reverse()
        return msgLs
    }

    fun add2MsgLs(msg: MsgEnt){
        val cv = ContentValues()
        openMsgDb()
        cv.put("type", msg.type.toString())
        cv.put("mtype", msg.mtype.toString())
        cv.put("tit", msg.tit)
        cv.put("sub", msg.sub)
        cv.put("addt",msg.add)
        cv.put("ip", msg.ip)
        cv.put("path", msg.path)
        cv.put("pno", msg.pno)
        cv.put("fname", msg.fname)
        cv.put("fsize", msg.fsize)
        val rei = insert(AppConstants.DB_TBNAME_MSGLS, cv)
        close()
        val rid = if(rei<0)Date().time else rei.toLong()
        msg.id = rid
    }

    fun updMsgMtype(id: Long?,mtype: Int?){
        id?:return
        mtype?:return
        openMsgDb()
        val cv = ContentValues()
        cv.put("mtype", mtype)
        update(AppConstants.DB_TBNAME_MSGLS,cv,"mid=?", arrayOf("$id"))
        close()
    }

    private fun rmMsgItm(ids: Array<Int>?) {
        rmItm(AppConstants.DB_TBNAME_MSGLS,"mid",ids)
    }

    private fun rmItm(tbnm: String,colnm: String,ids: Array<Int>?) {
        ids?:return
        if(ids.isEmpty())return
        val idsk = StringBuilder()
        val idsV = mutableListOf<String>()
        for(dinx in ids.indices){
            val did = ids[dinx]
            if(dinx>0)idsk.append(",")
            idsk.append("?")
            idsV.add(did.toString())
        }
        openMsgDb()
        delete(tbnm,"$colnm in($idsk)",idsV.toTypedArray())
        close()
    }

    /**Common */
    fun utilSqlNull(str: String?): String {
        val s = utilSqlEsc(str) ?: return "NULL"
        return "'$s'"
    }
    fun utilSqlEsc(str: String?): String? {
        if (utilIsEmpty(str)) return null
        return str?.replace("'".toRegex(), "''")
    }
    companion object {
        private val TAG = "DbUtil"
        private val DB_VERSION=1
    }


}
