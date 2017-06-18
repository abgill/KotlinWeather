package com.example.andrew.kotlinapp.modal.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.andrew.kotlinapp.modal.DBSTATUS
import java.lang.Exception

/**
 * Created by andrew on 6/17/17.
 */
class Dao {
    companion object{
        fun getDataStatus(db: SQLiteDatabase) : DBSTATUS{
            try {
                val c: Cursor = db.rawQuery("select * from DATASTATUS", null)
                c.moveToFirst()
                val zip: Int = c.getInt(c.getColumnIndex("ZIP"))
                val lastAccessed: Long = c.getLong(c.getColumnIndex("LASTUPDATED"))
                c.close()

                return DBSTATUS(zip, lastAccessed)
            } catch (e : Exception){
                return DBSTATUS(0,0)
            }
        }

        fun writeDataStatus(db: SQLiteDatabase,status:DBSTATUS){
            db.execSQL("delete from DATASTATUS")

            val values: ContentValues = ContentValues()
            values.put("ZIP", status.zip)
            values.put("LASTUPDATED",status.lastUpdated)

            db.insert("DATASTATUS",null,values)
        }
    }
}