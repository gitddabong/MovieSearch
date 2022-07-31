package com.example.moviesearch.classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql : String = "CREATE TABLE if not exists recent_words (" +
//                "_id integer primary key autoincrement," +
                "word text primary key," +
                "date timestamp default current_timestamp not null);"

        val execSQL = db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS recent_words"

        db?.execSQL(sql)
        onCreate(db)
    }

}