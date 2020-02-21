package com.example.todolistkotlin

import android.content.ClipData
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import kotlinx.android.synthetic.main.main_layout.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity: AppCompatActivity(), CustomDialog.CustomDialogListener{


    private var list: ArrayList<DaysClass> = ArrayList()

    val dateTimeStrToLocalDateTime: (DaysClass) -> LocalDate = {
        LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy/M/dd"))
    }
    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)
        setContentView(R.layout.main_layout)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = ListAdapter(list)

        button.setOnClickListener {
            openDialog()
        }
    }

    fun openDialog()
    {
        val dialog = CustomDialog()
        dialog.show(supportFragmentManager,"h")
    }


    override fun applyText(name: String, scnd: String) {

        val index: Int = isInList(scnd, list)
        if(index < 0)
        {
            val array: ArrayList<ItemClass> = ArrayList()
            array.add(ItemClass(name,scnd))

            list.add(DaysClass(scnd, array))
        }
        else
        {
            val a: ArrayList<ItemClass>? = list.get(index).itemClass
            a!!.add(ItemClass(name, scnd))
        }
        list.sortWith(compareBy({ it.date }))
        recycler_view.adapter = ListAdapter(list)
        //recycler_view.adapter?.notifyDataSetChanged() <---- ArrayIndexOutOfBounds
    }

    fun isInList(name: String, list: ArrayList<DaysClass>): Int
    {
        list.forEach {
            if (it.date.equals(name))
                return list.indexOf(it)
        }
        return -1
    }



}