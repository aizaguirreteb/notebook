package es.aizaguirre.notasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var notes  = mutableListOf<Note>()
    lateinit var editTextTitle : EditText
    lateinit var editTextBody : EditText
    lateinit var listViewNotes : ListView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * We have to get the elements we need searching by id
         */
        editTextTitle = findViewById(R.id.title)
        editTextBody = findViewById(R.id.body)
        listViewNotes = findViewById(R.id.noteList)

        /**
         * In order to show the list of notes that has been saved
         * we need an adaptor
         */
        val adapter = ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,
            notes)


        /**
         * We set the 'adapter' property of our listView object to the adapter we have created
         */
        listViewNotes.adapter = adapter

    }

    fun addNote(v : View?){

        /**
         * We have to get the title, the text, create a Note and add to the list
         */
        val title = editTextTitle.text.toString()
        val body = editTextBody.text.toString()
        val date = Date()

        val note = Note(title, body, date)

        notes.add(note)
    }
}
