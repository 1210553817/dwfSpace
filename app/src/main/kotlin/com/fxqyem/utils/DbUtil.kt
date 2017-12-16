package com.fxqyem.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.fxqyem.bean.AppConstants
import com.fxqyem.bean.SongGrpInfo
import com.fxqyem.bean.SongInfo

import java.io.File

class DbUtil {
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
            e.printStackTrace()
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
        db = dbHelper!!.writableDatabase
        return db
    }

    fun query(tableName: String?, columns: Array<String>, selection: String?,
              selectionArgs: Array<String>?, groupBy: String?, having: String?,
              orderBy: String): Cursor? {
        var cursor: Cursor? = null
        try {
            if (db == null)
                return null
            cursor = db!!.query(tableName, columns, selection, selectionArgs,
                    groupBy, having, orderBy)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cursor
    }

    fun insert(tableName: String, values: ContentValues): Int {
        var flag = -1
        try {
            if (db == null)
                return flag
            db!!.insert(tableName, null, values)
            val cursor = db!!.rawQuery("select last_insert_rowid() from " + tableName, null)
            cursor!!.moveToFirst()
            val inid = cursor.getInt(0)
            flag = inid
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
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
            e.printStackTrace()
        }

        return flag
    }

    fun update(tableName: String, values: ContentValues,
               whereClause: String, whereArgs: Array<String>): Int {
        var count = -1
        try {
            if (db == null)
                return count
            count = db!!.update(tableName, values, whereClause, whereArgs)
            return count
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return count
    }

    fun delete(tableName: String?, whereClause: String?, whereArgs: Array<String>?): Int {
        var count = -1
        try {
            count = db?.delete(tableName, whereClause, whereArgs)?:count
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return count
    }

    fun doForSql(sql: String) {
        try {
            if (db == null)
                return
            db!!.execSQL(sql)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "doForSql error! " + e.message)
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

    //    //创建表
    //    @Override
    //    public void onCreate(SQLiteDatabase db) {
    //        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
    //                TB_NAME+ "("+
    //                UserInfo. ID+ " integer primary key,"+
    //                UserInfo. USERID+ " varchar,"+
    //                UserInfo. TOKEN+ " varchar,"+
    //                UserInfo. TOKENSECRET+ " varchar,"+
    //                UserInfo. USERNAME+ " varchar,"+
    //                UserInfo. USERICON+ " blob"+
    //                ")"
    //        );
    //        Log. e("Database" ,"onCreate" );
    //    }
    //    //更新表
    //    @Override
    //    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
    //        onCreate(db);
    //        Log. e("Database" ,"onUpgrade" );
    //    }

    //    Cursor cursor = db.rawQuery("select last_insert_rowid() from person",null);
    //    cursor.moveToFirst();
    //    int strid = cursor.getInt(0);

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
        initTables(cdb)
        return db
    }

    private fun initTables(cdb:SQLiteDatabase) {
        //Log.d(TAG,"DatabaseNewVersion: $DB_VERSION, DatabaseVersion: ${cdb.version}<-------------------------------------------------------------")
        if (DB_VERSION<=cdb.version) {
            return
        }

        db?.execSQL("CREATE TABLE IF NOT EXISTS " +
                AppConstants.DB_TBNAME_FAVINFO + "(" +
                "fid integer primary key autoincrement ," +
                "fnm varchar," +
                "fsubnm varchar," +
                "ftbnm varchar," +
                "picnm varchar," +
                "picurl varchar," +
                "indx integer" +
                ")"
        )

        db?.execSQL("CREATE TABLE IF NOT EXISTS " +
                AppConstants.DB_TBNAME_TMPLS + "(" +
                "sid integer primary key autoincrement ," +
                "oid integer," +
                "snm varchar," +
                "sart varchar," +
                "salb varchar," +
                "sdur integer," +
                "sindx integer," +
                "lrcnm varchar," +
                "lrcurl varchar," +
                "picnm varchar," +
                "picurl varchar," +
                "datanm varchar," +
                "dataurl varchar," +
                "ssz integer" +
                ")"
        )
        if(11>cdb.version) {
            cdb?.execSQL("ALTER TABLE ${AppConstants.DB_TBNAME_TMPLS} ADD COLUMN songid varchar")
            cdb?.execSQL("ALTER TABLE ${AppConstants.DB_TBNAME_TMPLS} ADD COLUMN type varchar")
            cdb?.execSQL("ALTER TABLE ${AppConstants.DB_TBNAME_TMPLS} ADD COLUMN qmid varchar")
        }
        val favls = queryFavLs()
        if(favls!=null&&favls.size>0) {
            for (fav in favls) {
                if (16 > cdb.version) {
                    cdb?.execSQL("ALTER TABLE ${fav.tbName} ADD COLUMN songid varchar")
                    cdb?.execSQL("ALTER TABLE ${fav.tbName} ADD COLUMN type varchar")
                    cdb?.execSQL("ALTER TABLE ${fav.tbName} ADD COLUMN qmid varchar")
                }
            }
        }

        cdb.version = DB_VERSION
    }

    fun creatFavLsTable(): String {
        var tbindx=0
        val cursor = queryForSql("select seq from sqlite_sequence where name=?", arrayOf(AppConstants.DB_TBNAME_FAVINFO))
        if(cursor!=null&&cursor.moveToNext()){
            tbindx = cursor.getInt(cursor.getColumnIndex("seq"))
        }
        val tablenm = AppConstants.DB_TBNAME_FAVLS + (tbindx+1)
        db?.execSQL("CREATE TABLE IF NOT EXISTS $tablenm (" +
                "sid integer primary key autoincrement ," +
                "oid integer," +
                "snm varchar," +
                "sart varchar," +
                "salb varchar," +
                "sdur integer," +
                "sindx integer," +
                "lrcnm varchar," +
                "lrcurl varchar," +
                "picnm varchar," +
                "picurl varchar," +
                "datanm varchar," +
                "dataurl varchar," +
                "ssz integer," +
                "songid varchar," +
                "type varchar," +
                "qmid varchar" +
                ")"

        )

        return tablenm
    }

    fun queryFavLs():ArrayList<SongGrpInfo>{
        val c = query(AppConstants.DB_TBNAME_FAVINFO,
                arrayOf("fid", "fnm", "fsubnm", "ftbnm", "picnm", "picurl", "indx"), null, null, null, null,
                "indx desc")
        val chdlsa = java.util.ArrayList<SongGrpInfo>()
        while (c != null && c.moveToNext()) {
            val info = SongGrpInfo()
            info.id = c.getInt(c.getColumnIndex("fid"))
            info.name = c.getString(c.getColumnIndex("fnm"))
            info.subname = c.getString(c.getColumnIndex("fsubnm"))
            info.tbName = c.getString(c.getColumnIndex("ftbnm"))
            info.picName = c.getString(c.getColumnIndex("picnm"))
            info.picUrl = c.getString(c.getColumnIndex("picurl"))
            info.indx = c.getInt(c.getColumnIndex("indx"))
            chdlsa.add(info)
        }
        c?.close()

        return chdlsa
    }

    fun queryFavSongLs(tbnm: String?,curDataType: Int): ArrayList<SongInfo>?{
        tbnm?:return null

        val curSongLs = ArrayList<SongInfo>()
        val c = query(tbnm,
                arrayOf("sid", "snm", "sart", "salb", "lrcnm", "lrcurl", "picnm", "picurl", "datanm", "dataurl", "oid", "sdur", "ssz", "sindx","songid","type","qmid"), null, null, null, null,
                "sindx")
        while (c != null && c.moveToNext()) {
            val info = SongInfo()
            info.id = c.getInt(c.getColumnIndex("sid"))
            info.name = c.getString(c.getColumnIndex("snm"))
            info.artist = c.getString(c.getColumnIndex("sart"))
            info.album = c.getString(c.getColumnIndex("salb"))
            info.lrcNm = c.getString(c.getColumnIndex("lrcnm"))
            info.lrcUrl = c.getString(c.getColumnIndex("lrcurl"))
            info.picNm = c.getString(c.getColumnIndex("picnm"))
            info.picUrl = c.getString(c.getColumnIndex("picurl"))
            info.data = c.getString(c.getColumnIndex("datanm"))
            info.dataUrl = c.getString(c.getColumnIndex("dataurl"))
            info.oid = c.getInt(c.getColumnIndex("oid"))
            info.duration = c.getInt(c.getColumnIndex("sdur"))
            info.size = c.getInt(c.getColumnIndex("ssz"))
            info.indx = c.getInt(c.getColumnIndex("sindx"))

            info.songId = c.getString(c.getColumnIndex("songid"))
            info.type = c.getString(c.getColumnIndex("type"))
            info.qmid = c.getString(c.getColumnIndex("qmid"))

            info.dataType=curDataType
            curSongLs!!.add(info)
        }
        c?.close()
        close()
        return curSongLs
    }


    companion object {
        private val TAG = "DbUtil"
        private val DB_VERSION=16
    }
}
