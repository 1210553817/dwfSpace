package com.fxqyem.brw.utl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.fxqyem.brw.ben.AppConstants
import java.io.File
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
    private fun openSdDatabase(sdBasePath: String, dbPath: String, dbName: String): SQLiteDatabase? {
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
    private fun openInnerDatabase(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteDatabase? {
        dbHelper = DbHelper(context, name, factory, version)
        db = dbHelper?.writableDatabase
        return db
    }

    fun query(tableName: String?, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, groupBy: String?, having: String?,
              orderBy: String,limit: String): Cursor? {
        var cursor: Cursor? = null
        db?:return cursor
        try {
            cursor = db?.query(tableName, columns, selection, selectionArgs,
                    groupBy, having, orderBy,limit)
        } catch (e: Exception) {
            Log.e(TAG, "query Sql error! \n" + e.message)
        }

        return cursor
    }

    fun insert(tableName: String, values: ContentValues): Int {
        var flag = -1
        db?:return flag
        try {
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
        db?:return flag
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
            cursor.close()
        } catch (e: Exception) {
            Log.e(TAG, "insertUseMap Sql error! \n" + e.message)
        }

        return flag
    }

    fun update(tableName: String, values: ContentValues,
               whereClause: String, whereArgs: Array<String>): Int {
        var count = -1
        db?:return count
        try {
            count = db?.update(tableName, values, whereClause, whereArgs)?:count
            return count
        } catch (e: Exception) {
            Log.e(TAG, "update Sql error! \n" + e.message)
        }

        return count
    }

    fun delete(tableName: String?, whereClause: String?, whereArgs: Array<String>?): Int {
        var count = -1
        db?:return count
        try {
            count = db?.delete(tableName, whereClause, whereArgs)?:count
        } catch (e: Exception) {
            Log.e(TAG, "delete Sql error! \n" + e.message)
        }

        return count
    }

    fun doForSql(sql: String) {
        db?:return
        try {
            db?.execSQL(sql)
        } catch (e: Exception) {
            Log.e(TAG, "doForSql error! \n" + e.message)
        }

        return
    }

    fun queryForSql(sql: String, selectionArgs: Array<String>): Cursor? {
        var cursor: Cursor? = null
        db?:return cursor
        try {
            cursor = db?.rawQuery(sql, selectionArgs)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cursor
    }

    fun close() {
        db?.close()
        dbHelper?.close()
    }


    fun OpenDbCommon(sdBasePath: String, dbPath: String, dbName: String, context: Context?, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase? {
        context?:return db
        if (SDCardUtils.isSDCardEnable) {
            openSdDatabase(sdBasePath, dbPath, dbName)
        } else {
            openInnerDatabase(context, dbName, factory,DB_VERSION)
        }
        val cdb = db
        cdb?:return db
        if(AppConstants.DB_NAME_MSGDATAS==dbName) {
            //initMsgTables(cdb)
        }
        return db
    }


    /**msg Datebase */
    fun openMsgDb(): SQLiteDatabase?{
            return OpenDbCommon(SDCardUtils.sdCardPath, AppConstants.DB_BASE_PATH, AppConstants.DB_NAME_MSGDATAS, context, null)
    }

    /**Common */
    companion object {
        private val TAG = "DbUtil"
        private val DB_VERSION=1
    }


}
