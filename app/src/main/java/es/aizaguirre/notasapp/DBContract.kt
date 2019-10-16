package es.aizaguirre.notasapp

import android.provider.BaseColumns

object DBContract {

    class NoteEntry : BaseColumns {
        companion object{
            val TABLE_NAME = "notes"
            val COLUMN_NOTE_TITLE = "title"
            val COLUMN_TEXT = "text"
            val COLUMNS_DATE = "date"
        }
    }
}