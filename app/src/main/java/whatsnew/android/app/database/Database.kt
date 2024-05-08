package whatsnew.android.app.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.Executor


class Database(context: Context?, private val executor: Executor) :
    SQLiteOpenHelper(context, databaseName, null, version) {
    private var cursor: Cursor? = null
    override fun onCreate(liteDatabase: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TableName + " ("
                + idColume + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + titleColume + " TEXT,"
                + imageUrlColume + " TEXT,"
                + urlColume + " TEXT)")
        liteDatabase.execSQL(query)
    }

    fun addData(title: String?, url: String?, imageurl: String?) {
        executor.execute {
            val db = writableDatabase // should use this part thread
            val values = ContentValues()
            values.put(titleColume, title)
            values.put(imageUrlColume, imageurl)
            values.put(urlColume, url)
            db.insert(TableName, null, values)
            db.close()
        }
    }

    fun deleteData(title: String, url: String, imageUrl: String) {
        executor.execute {
            val db = writableDatabase
            db.delete(TableName, "Title=?", arrayOf(title))
            db.delete(TableName, "ImageURL=?", arrayOf(imageUrl))
            db.delete(TableName, "URL=?", arrayOf(url))
            db.close()
        }
    }

    fun readData(): Cursor? {
        executor.execute {
            val dp = readableDatabase
            cursor = dp.rawQuery("SELECT * FROM " + TableName, null)
        }
        return cursor
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS " + TableName)
        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val databaseName = "Store.db"
        private const val version = 1
        private const val TableName = "FavoriteFragment"
        private const val idColume = "ID"
        const val titleColume = "Title"
        const val imageUrlColume = "ImageURL"
        const val urlColume = "URL"
    }
}
