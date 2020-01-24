package com.example.todolistkotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*

class ListAdapter(val items: ArrayList<ItemClass>, val context: Context): RecyclerView.Adapter<ListAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        println("bind")
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val tvItemText = view.text
        val tvItemText2 = view.text2
        val vRadio = view.radio


        fun bind(item: ItemClass)
        {
            tvItemText.text = item.Name
            tvItemText2.text = item.Surname
            vRadio.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked)
                {
                    Log.d("bind","isChecked")
                }else{
                    Log.d("bind","isNotChecked")
                }
            }

        }


    }
}
