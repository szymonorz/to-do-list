package com.example.todolistkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.model.ItemClass
import kotlinx.android.synthetic.main.item_layout.view.*

class ItemAdapter(items: ArrayList<ItemClass>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    val items = items

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle = itemView.itemTitle
        val description = itemView.itemDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items.get(position)
        holder.itemTitle.text = item.Title
        holder.description.text = item.Description
    }
}