package com.example.todolistkotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.activities.MainActivity
import com.example.todolistkotlin.model.ItemClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_layout.view.*

class ItemAdapter(items: ArrayList<ItemClass>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    val items = items
    val reference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle = itemView.itemTitle
        val description = itemView.itemDescription
        val checkbox = itemView.checkbox
        val delete = itemView.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): ItemClass {
        return items.get(position)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items.get(position)
        holder.itemTitle.text = item.Title
        holder.description.text = item.Description
        holder.checkbox.isChecked = item.checked
        if (holder.checkbox.isChecked)
            holder.checkbox.setBackgroundResource(R.drawable.checked)

        holder.checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setBackgroundResource(R.drawable.checked)
                //updateDatabaseChecked(position, "true")
            } else {
                buttonView.setBackgroundResource(R.drawable.unchecked)
                //updateDatabaseChecked(position, "false")
            }
        })
        holder.delete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                updateDatabaseDelete(position)
            }
        })
    }

    private fun updateDatabaseDelete(position: Int) {
        MainActivity().reference.child(mAuth.uid as String).child(getItem(position).Date as String)
            .child(getItem(position).Id.toString()).removeValue()


    }

    private fun updateDatabaseChecked(position: Int, value: String) {
        Log.d("poz", position.toString())
        reference.child(mAuth.uid as String).child(getItem(position).Date as String)
            .child(position.toString()).child("checked").setValue(value)

    }
}