package com.example.todolistkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todolistkotlin.adapter.ExpandableListViewAdapter
import com.example.todolistkotlin.interfaces.FullscreenDialogInterface
import com.example.todolistkotlin.model.DaysClass
import com.example.todolistkotlin.model.ItemClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.main_layout.*

class MainActivity : AppCompatActivity(), FullscreenDialogInterface {


    var list: ArrayList<DaysClass> = ArrayList()
    lateinit var adapter: ExpandableListViewAdapter
    var gson = GsonBuilder().setPrettyPrinting().create()
    private val FILE_NAME = "task_list.txt"
    val mAuth = FirebaseAuth.getInstance()
    val reference = FirebaseDatabase.getInstance().reference
    lateinit var relativeLayout: RelativeLayout
    lateinit var container: ViewGroup
    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)

        load()
        setContentView(R.layout.main_layout)
        container = findViewById(android.R.id.content)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        adapter = ExpandableListViewAdapter(this, list)
        relativeLayout = findViewById(R.id.relative)
        expandable_view.setAdapter(adapter)
        val toolbar: Toolbar? = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        button.setOnClickListener {
            openDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            val i = Intent(applicationContext, LoginActivity::class.java)

                            startActivity(i)
                            finish()
                        }
                    })

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDialog()
    {
        val dialog = FullscreenDialog()
        dialog.show(supportFragmentManager,"h")

    }


    override fun applyText(title: String, description: String, date: String) {

        val temp = ArrayList(list)
        val index: Int = isInList(date, list)
        lateinit var d: DaysClass
        var position: Int = 0
        if(index < 0)
        {
            val array: ArrayList<ItemClass?> = ArrayList()
            array.add(
                ItemClass(
                    title,
                    description,
                    date
                )
            )
            println(array)
            d = DaysClass(date, array)
            println(d)
            list.add(0,d)
            list.sortBy { it.date }
            position = list.indexOf(d)

        }
        else
        {
            val temp: ArrayList<DaysClass> = ArrayList(list)
            val a: ArrayList<ItemClass?>? = temp.get(index).itemClass
            a!!.add(
                0,
                ItemClass(
                    title,
                    description,
                    date
                )
            )
            list = temp
            list.sortBy { it.date }
            position = index

        }
        save(position)
        adapter.notifyDataSetChanged()
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
        val l = ArrayList<DaysClass>()
        Log.d(".MainActivity","Loading data")
        reference.child(mAuth.currentUser!!.uid).addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    val arr = ArrayList<ItemClass?>()
                    it.children.forEach {
                        val i = ItemClass(
                            it.child("title").value.toString(),
                            it.child("description").value.toString(),
                            it.child("date").value.toString()
                        )
                        arr.add(i)
                        Log.d("Loading", "Loaded")
                    }
                    list.add(
                        DaysClass(
                            it.key,
                            arr
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }
        })
    }

}