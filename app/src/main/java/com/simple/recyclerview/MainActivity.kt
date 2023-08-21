package com.simple.recyclerview

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var addBtn: ImageButton
    lateinit var tasksNum: TextView
    lateinit var saveNote: String
    lateinit var ourAdapter: ClassAdapter
    lateinit var sqlH: MyDatabaseHalper
    lateinit var editNote: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sqlH = MyDatabaseHalper(this)
        var id = 0

        //setupItemsOnRv()
        callViews()
        setupItemsOnRv()
        tasksNum.setText("${getItems().size} Tasks")
        recycler.setHasFixedSize(true)
        Log.i("size", sqlH.readeDate().size.toString())
        //add new note
        addBtn.setOnClickListener {
            //the view of alaretdialog
            editNote = EditText(this)
            val layoutPram = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            )
            editNote.setLayoutParams(layoutPram)
            AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setMessage("Please Enter your new note..")
                .setView(editNote)
                .setPositiveButton("Save") { dialog, which ->
                    if (editNote.text.isNotEmpty()) {
                        saveNote = editNote.text.toString()
                        //  noteLists.add(Note(id, false, saveNote))
                        sqlH.insertData(Note(id, false, saveNote))
                        id += 1
                        Log.i("data", sqlH.readeDate().size.toString())
                        setupItemsOnRv()
                        dialog.dismiss()
                        saveNote = ""

                    }

                    tasksNum.setText("${getItems().size} Tasks")

                }.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("DATA",sqlH.readeDate().toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
       savedInstanceState.getString("DATA")
    }
    private fun callViews() {
        recycler = findViewById(R.id.tasksList)
        addBtn = findViewById(R.id.addTask)
        tasksNum = findViewById(R.id.tasks_num)
    }

    private fun setupItemsOnRv() {
        if (getItems().size > 0) {
            recycler.visibility = View.VISIBLE
            recycler.layoutManager = LinearLayoutManager(this)
            ourAdapter = ClassAdapter(this, getItems())
            recycler.adapter = ourAdapter
        } else {
            recycler.visibility = View.GONE

        }
    }

    fun getItems(): ArrayList<Note> {
        var listOfNote: ArrayList<Note> = sqlH.readeDate()
        //   Log.i("data",listOfNote.toString())
        return listOfNote
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateOurNotes(NoteList: Note) {
        val ourUpdateDialog = Dialog(this)
        ourUpdateDialog.setContentView(R.layout.update_note)
        ourUpdateDialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        ourUpdateDialog.setCancelable(false)
        val saveBt = ourUpdateDialog.findViewById<Button>(R.id.saveBtn)
        val cancel = ourUpdateDialog.findViewById<Button>(R.id.cancelBtn)
        val edit = ourUpdateDialog.findViewById<EditText>(R.id.editNote)
        edit.setText(NoteList.text)
        saveBt.setOnClickListener {
            var textAfterEdit = edit.text.toString()
            if (textAfterEdit.isNotEmpty()) {
                sqlH.updateData(Note(NoteList.id, NoteList.checkTask, textAfterEdit))
                ourUpdateDialog.dismiss()
                textAfterEdit = ""
                setupItemsOnRv()
            }
        }
        cancel.setOnClickListener {
            Toast.makeText(this, "No changes", Toast.LENGTH_SHORT).show()
            ourUpdateDialog.dismiss()
        }
        ourUpdateDialog.show()


    }
    fun deleteTheItem(NoteList: Note) {
        val dialogD = AlertDialog.Builder(this)
        dialogD.setTitle("Delete The Item")
            .setMessage("Are you sure that you want to delete the item?")
            .setPositiveButton("Yes") { dialog, which ->
                val state = sqlH.deleteData(Note(NoteList.id, false, ""))
                if (state > -1) Toast.makeText(this, "Successful delete", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                setupItemsOnRv()
                tasksNum.setText("${getItems().size} Tasks")

            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }.show()
    }
}