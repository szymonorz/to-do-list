package com.example.todolistkotlin

import android.content.ClipData
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.main_layout.*

class MainActivity: AppCompatActivity(), CustomDialog.CustomDialogListener{


    private val items: ArrayList<ItemClass> = ArrayList()
    public lateinit var dateString: String
    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)
        setContentView(R.layout.main_layout)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = ListAdapter(items,this)
        button.setOnClickListener {
            openDialog()
        }
    }

    fun openDialog()
    {
        val dialog = CustomDialog()
        dialog.show(supportFragmentManager,"shit")
    }


    override fun applyText(name: String, scnd: String) {
        items.add(ItemClass(name,scnd))
        recycler_view.adapter?.notifyDataSetChanged()
    }

}