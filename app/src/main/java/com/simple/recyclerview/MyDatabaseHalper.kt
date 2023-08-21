package com.simple.recyclerview

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabaseHalper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "noteDataBase"
        const val TABLE_NAME = "my_note"

        //CLOUMNS OF THE TABLE
        const val KEY_ID = "ID"
        const val KEY_BODY = "BODY"
        const val KEY_STATE = "STATE"


    }

    //for creation the table for the first time
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY ,"
                + KEY_BODY + " TEXT NOT NULL,"
                + KEY_STATE + " BOOLEAN" + ")"
                )
        db?.execSQL(CREATE_TABLE)

    }

    //to do any updates on the database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //to insert new data
    fun insertData(note: Note): Long {
        val dbWriter = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(KEY_BODY, note.text)
        contentValue.put(KEY_STATE, note.checkTask)
        val column = dbWriter.insert(TABLE_NAME, null, contentValue)
        dbWriter.close()
        return column

    }

    fun updateData(note: Note): Int {
        val dbWriter = this.writableDatabase
        val content = ContentValues()
        content.put(KEY_STATE, note.checkTask)
        content.put(KEY_BODY, note.text)
        val newUpdate = dbWriter.update(TABLE_NAME, content, KEY_ID + "=" + note.id, null)
        dbWriter.close()
        return newUpdate

    }

    fun deleteData(note: Note) :Int{
        val dbWriter = this.writableDatabase
        val success = dbWriter.delete(TABLE_NAME, KEY_ID + "=" + note.id,null)
        dbWriter.close()
        return success
    }

    fun readeDate(): ArrayList<Note> {
        val listOfNote = ArrayList<Note>()
        val query = "SELECT * FROM $TABLE_NAME"
        val reader = this.readableDatabase
        var cursor: Cursor?
        try {
          cursor=  reader.rawQuery(query, null)
        } catch (e: java.lang.Exception) {
            reader.execSQL(query)
            return ArrayList()
        }
        var body: String
        var state: String
        var id: Int
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                    body = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BODY))
                    state = cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATE))
                    val content = Note(id, state.toBoolean(), body)
                    listOfNote.add(content)
                } while (cursor.moveToNext())

            }
        }
        Log.i("data",listOfNote.toString())
        cursor?.close()
        return listOfNote
    }
}