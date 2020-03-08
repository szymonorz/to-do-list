package com.example.todolistkotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.model.DaysClass
import kotlinx.android.synthetic.main.expandable_days.view.*

class DayRecyclerViewAdapter(days: ArrayList<DaysClass>) :
    RecyclerView.Adapter<DayRecyclerViewAdapter.DayViewHolder>() {
    private val days = days
    private val viewPool = RecyclerView.RecycledViewPool()
    lateinit var itemAdapter: ItemAdapter

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.title
        val item_view = itemView.item_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.expandable_days, parent, false)

        return DayViewHolder(v)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days.get(position)
        holder.title.text = day.date
        itemAdapter = ItemAdapter(day.itemClass)
        val lManager = LinearLayoutManager(holder.item_view.context, RecyclerView.VERTICAL, false)
        holder.item_view.apply {
            layoutManager = lManager
            adapter = itemAdapter
            setRecycledViewPool(viewPool)

        }

        Log.d("ViewHolder", "bind")

    }

}