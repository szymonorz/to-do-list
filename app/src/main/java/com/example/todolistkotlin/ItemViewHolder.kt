package com.example.todolistkotlin

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.item_layout.view.*

class ItemViewHolder(itemView: View?) : ChildViewHolder(itemView) {
    private lateinit var  cardView: MaterialCardView

    val tvItemText = itemView?.text
    val tvItemText2 = itemView?.text2
    val vRadio = itemView?.radio


    fun bind(item: ItemClass)
    {
        tvItemText?.text = item.Name
        tvItemText2?.text = item.Surname
        cardView =this.itemView.findViewById<MaterialCardView>(R.id.card)
        vRadio?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                Log.d("bind","isChecked")
                cardView.setStrokeColor(Color.RED)
            }else{
                Log.d("bind","isNotChecked")
                cardView.setStrokeColor(Color.BLUE)
            }
        }

    }

}