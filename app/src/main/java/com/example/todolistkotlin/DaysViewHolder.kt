package com.example.todolistkotlin

import android.view.TextureView
import android.view.View
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class DaysViewHolder(itemView: View?) : GroupViewHolder(itemView)
{
    val textView: TextView = itemView!!.findViewById(R.id.title)


    fun bind(day: DaysClass)
    {
        textView.setText(day.date)
    }
}