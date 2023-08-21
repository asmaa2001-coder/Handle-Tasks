package com.simple.recyclerview

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
in this class we should create the adapter which is the link between Note and
the Recycler view
1--create two class ClassAdapter which inherit from RecyclerView.Adapter
and create the view holder which inherit from :RecyclerView.ViewHolder
2--we have 3 methode
A ==onCreateViewHolder methode which is responsible to make the adapter in the recyclerview
B ==onBindViewHolder which is responsible for get the date to show it in the recycler view
C ==getItemCount to get the length of the items
 */
class ClassAdapter(private val context: Context, private val tasks: List<Note>) :
    RecyclerView.Adapter<ClassAdapter.NoteHelper>() {
 val data= MyDatabaseHalper(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHelper {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_list, parent, false)
        return NoteHelper(itemView)
    }

    override fun onBindViewHolder(holder: NoteHelper, position: Int) {
        val currntItem = tasks[position]
        holder.note.text = currntItem.text
        holder.editItem.setOnClickListener { view ->
            if (context is MainActivity) context.updateOurNotes(currntItem)
        }
        holder.delItem.setOnClickListener { view ->
            if (context is MainActivity) context.deleteTheItem(currntItem)
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTextView(holder: NoteHelper, currntItem: Note) {
        val state = currntItem.checkTask
        if (state) {
            holder.note.paintFlags = holder.note.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            data.updateData(Note(currntItem.id,state,currntItem.text))
        } else {
            holder.note.paintFlags = holder.note.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            data.updateData(Note(currntItem.id,state,currntItem.text))

        }
    }


    inner class NoteHelper(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note: TextView = itemView.findViewById(R.id.taskShow)
        var check: CheckBox = itemView.findViewById(R.id.checkTask)
        var delItem: ImageButton = itemView.findViewById(R.id.deleteButton)
        var editItem: ImageButton = itemView.findViewById(R.id.editBtn)

        init {
            check.setOnClickListener {
                val currntItem = tasks[adapterPosition]
                currntItem.checkTask = !currntItem.checkTask
                (itemView.context as MainActivity).ourAdapter.updateTextView(this, currntItem)
            }
        }


    }
}
