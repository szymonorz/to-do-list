package com.example.todolistkotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.main_layout.*
import java.io.*
import java.lang.StringBuilder
import kotlin.collections.ArrayList

class MainActivity: AppCompatActivity(), CustomDialog.CustomDialogListener{


    private var list: ArrayList<DaysClass> = ArrayList()
    var gson: Gson = Gson()
    private val FILE_NAME = "task_list.txt"


    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)
        try {
            load()
        }catch(e: FileNotFoundException)
        {
            println("File not found")
        }


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
        save()
        recycler_view.adapter = ListAdapter(list)
    }

    fun isInList(name: String, list: ArrayList<DaysClass>): Int
    {
        list.forEach {
            if (it.date.equals(name))
                return list.indexOf(it)
        }
        return -1
    }

    fun save()
    {
        val json: String = gson.toJson(list)
        val fos: FileOutputStream
        fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        fos.write(json.toByteArray())
        Toast.makeText(this,"Saved to ${filesDir}", Toast.LENGTH_SHORT).show()
        fos.close()
    }

    fun load()
    {
        Log.d(".MainActivity","Loading data")
        val fis: FileInputStream
        fis = openFileInput(FILE_NAME)
        val isr = InputStreamReader(fis)
        val br = BufferedReader(isr)
        val sb = StringBuilder()
        br.lineSequence().forEach {
            sb.append(it).append("\n")
        }

        list = gson.fromJson<ArrayList<DaysClass>>(sb.toString(),object: TypeToken<ArrayList<DaysClass>>(){}.type)
        fis.close()
    }



}