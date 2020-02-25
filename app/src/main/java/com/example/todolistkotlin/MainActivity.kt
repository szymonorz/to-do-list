package com.example.todolistkotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.main_layout.*
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.collections.ArrayList

class MainActivity: AppCompatActivity(), CustomDialog.CustomDialogListener{


    var list: ArrayList<DaysClass> = ArrayList()
    var gson = GsonBuilder().setPrettyPrinting().create()
    private val FILE_NAME = "task_list.txt"
    val mAuth = FirebaseAuth.getInstance()
    val reference = FirebaseDatabase.getInstance().reference



    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)

        load()
        setContentView(R.layout.main_layout)
        recycler_view.setLayoutManager(LinearLayoutManager(this))
        recycler_view.setAdapter(ListAdapter(list))

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

        val temp = ArrayList(list)
        val index: Int = isInList(scnd, list)
        lateinit var d: DaysClass
        var position: Int = 0
        if(index < 0)
        {
            val array: ArrayList<ItemClass?> = ArrayList()
            array.add(ItemClass(name,scnd))
            println(array)
            d = DaysClass(scnd, array)
            println(d)
            list.add(0,d)
            list.sortWith(compareBy({ it.date }))
            position = list.indexOf(d)

        }
        else
        {
            val temp: ArrayList<DaysClass> = ArrayList(list)
            val a: ArrayList<ItemClass?>? = temp.get(index).itemClass
            a!!.add(0,ItemClass(name, scnd))
            list = temp
            list.sortWith(compareBy({ it.date }))
            position = index

        }
        save(position)
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


    fun save(position: Int)
    {
        println("save")
        reference.child(mAuth.currentUser!!.uid)
            .child(list.get(position).date.toString())
            .setValue(list.get(position).itemClass)
            .addOnFailureListener(object: OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Log.d(".MainAcitivty", p0.printStackTrace().toString())
                }
            })
    }

    fun load()
    {
        Log.d(".MainActivity","Loading data")
        reference.child(mAuth.currentUser!!.uid).addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {

                val l = ArrayList<DaysClass>()
                p0.children.forEach {
                    val arr = ArrayList<ItemClass?>()
                    it.children.forEach {
                        val i = ItemClass(it.child("name").value.toString(),it.child("surname").value.toString())
                        arr.add(i)
                    }
                    l.add(DaysClass(it.key, arr))
                }
                list = ArrayList(l)
                recycler_view.adapter = ListAdapter(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }
        })
    }



}