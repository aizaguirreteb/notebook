package es.aizaguirre.notasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private var notes  = mutableListOf<Note>()
    var listOfSavedNotes = mutableListOf<Note>()
    lateinit var editTextTitle : EditText
    lateinit var editTextBody : EditText
    lateinit var listViewNotes : ListView
    lateinit var adapter : ArrayAdapter<Note>
    lateinit var notesDBHelper : NotesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesDBHelper = NotesDBHelper(this)

        /**
         * We have to get the elements we need searching by id
         */
        getLayoutViews()

        /**
         * In order to show the list of notes that has been saved
         * we need an adaptor that has been declared for the whole class
         */
        setAdapter()

        setListViewListeners()



    }

    private fun setListViewListeners() {
        listViewNotes.setOnItemClickListener { parent, view, position, id ->
            val notesSaved = notesDBHelper.readAllNotes()
            val note = notesSaved[position]
            editTextTitle.setText(note.title)
            editTextBody.setText(note.text)
        }

        listViewNotes.setOnItemLongClickListener { parent, view, position, id ->
            val notesSaved = notesDBHelper.readAllNotes()
            val note = notesSaved[position]
            notesDBHelper.deleteNote(note.title)
            //notes.removeAt(position)
            adapter.notifyDataSetChanged()
            setAdapter()
            Toast.makeText(this, "Note Deleted", Toast.LENGTH_LONG).show()
            true
        }
    }

    private fun setAdapter() {

        val listOfSavedNotes = notesDBHelper.readAllNotes()
        adapter = ArrayAdapter<Note>(
            this, android.R.layout.simple_list_item_1,
            listOfSavedNotes
        )


        /**
         * We set the 'adapter' property of our listView object to the adapter we have created
         */
        listViewNotes.adapter = adapter
    }

    private fun getLayoutViews() {
        editTextTitle = findViewById(R.id.title)
        editTextBody = findViewById(R.id.body)
        listViewNotes = findViewById(R.id.noteList)
    }

    fun addNote(v : View?){

        if(editTextTitle.text.toString() != "") {

            /**
             * We have to get the title, the text, create a Note and add to the list
             */
            val title = editTextTitle.text.toString()
            val body = editTextBody.text.toString()
            val date = Date().toString()

            val note = Note(title, body, date)
            var result = notesDBHelper.insertNote(Note(title, body, date))
            Toast.makeText(this, "Addin note " + result, Toast.LENGTH_LONG).show()
            //Clear all edit text!!!!
            //resetNoteViews()

           // notes.add(note)
            adapter.notifyDataSetChanged()
            setAdapter()
            resetNoteViews()
        } else {

            var msg = resources.getString(R.string.emptyTitle)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

    }

    fun resetNoteViews(){
        editTextBody.setText("")
        editTextTitle.setText("")
    }
}
