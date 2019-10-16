package es.aizaguirre.notasapp

import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class NotesDBHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertNote(note: Note): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.NoteEntry.COLUMN_NOTE_TITLE, note.title)
        values.put(DBContract.NoteEntry.COLUMN_TEXT, note.text)
        values.put(DBContract.NoteEntry.COLUMNS_DATE, note.date.toString())

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.NoteEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteNote(noteTitle: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.NoteEntry.COLUMN_NOTE_TITLE + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(noteTitle)
        // Issue SQL statement.
        db.delete(DBContract.NoteEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readNote(noteTitle: String): ArrayList<Note> {
        val users = ArrayList<Note>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.NoteEntry.TABLE_NAME + " WHERE "
                    + DBContract.NoteEntry.COLUMN_NOTE_TITLE + "='" + noteTitle + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var text: String
        var date: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                text = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_TEXT))
                date = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMNS_DATE))

                users.add(Note(noteTitle, text, date ))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readAllNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.NoteEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var noteTitle: String
        var text: String
        var date: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                noteTitle = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_NOTE_TITLE))
                text = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_TEXT))
                date = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMNS_DATE))

                notes.add(Note(noteTitle, text, date))
                cursor.moveToNext()
            }
        }
        return notes
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.NoteEntry.TABLE_NAME + " (" +
                        DBContract.NoteEntry.COLUMN_NOTE_TITLE + " TEXT PRIMARY KEY," +
                        DBContract.NoteEntry.COLUMN_TEXT + " TEXT," +
                        DBContract.NoteEntry.COLUMNS_DATE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.NoteEntry.TABLE_NAME
    }

}