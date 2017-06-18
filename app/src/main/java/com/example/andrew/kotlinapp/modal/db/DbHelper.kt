package com.example.andrew.kotlinapp.modal.db

import android.content.Context
import android.database.sqlite.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Created by andrew on 6/17/17.
 */
class DbHelper(context: Context)
    : SQLiteOpenHelper(context, Companion.DB_NAME, null, Companion.DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(Companion.FORECAST_SCHEMA)
        }catch (e : SQLException){
            e.printStackTrace()
        }
        try {
            db?.execSQL(Companion.DATA_STATUS_SCHEMA)
        }catch (e:SQLException){
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_FORECAST)
    }

    companion object {
        val DB_NAME = "db"
        val DB_VERSION = 1
        val FORECAST_SCHEMA : String = "CREATE TABLE FORECAST(" +
                "DAY INT PRIMARY KEY NOT NULL," +
                "LOW  REAL," +
                "HIGH  REAL," +
                "CITY   TEXT"+
                ");"
        val DROP_FORECAST = "DROP TABLE IF EXISTS FORECAST"
        val DATA_STATUS_SCHEMA = "CREATE TABLE DATASTATUS(" +
                "ZIP    INT," +
                "LASTUPDATED    INT" +
                ");"

    }
}