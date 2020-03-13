package com.example.todolistkotlin.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.adapter.DayRecyclerViewAdapter
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
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.main_layout.*
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), FullscreenDialogInterface {


    var list: ArrayList<DaysClass> = ArrayList()
    lateinit var recyclerAdapter: DayRecyclerViewAdapter
    private var gson = GsonBuilder().create()
    private val FILE_NAME = "task_list.json"
    private val FILE_SELECT = 100
    private val FILE_CREATE = 101
    private val mAuth = FirebaseAuth.getInstance()

    companion object

    var reference = FirebaseDatabase.getInstance().reference
    lateinit var relativeLayout: RelativeLayout
    lateinit var container: ViewGroup
    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstance: Bundle?)
    {
        super.onCreate(savedInstance)

        load()
        Log.d("a", list.toString())
        setContentView(R.layout.main_layout)
        container = findViewById(android.R.id.content)
        recyclerAdapter = DayRecyclerViewAdapter(list)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        relativeLayout = findViewById(R.id.relative)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        expandable_view.apply {
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter

        }
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

            R.id.upload -> chooseJson()
            R.id.download -> toJsonFile()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun chooseJson() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(Intent.createChooser(intent, "Select the json file"), FILE_SELECT)
    }

    private fun toJsonFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        }

        startActivityForResult(intent, FILE_CREATE)

    }

    private fun openDialog()
    {
        val dialog = FullscreenDialog()
        dialog.show(supportFragmentManager,"h")

    }


    override fun applyText(title: String, description: String, date: String, checked: Boolean) {

        val index: Int = isInList(date, list)
        lateinit var d: DaysClass
        var position: Int = 0
        if(index < 0)
        {
            val array: ArrayList<ItemClass> = ArrayList()
            array.add(
                ItemClass(
                    array.size.toString(),
                    title,
                    description,
                    date
                )
            )
            d = DaysClass(date, array)
            list.add(0,d)
            list.sortBy { it.date }
            position = list.indexOf(d)

        }
        else
        {
            val a: ArrayList<ItemClass> = list.get(index).itemClass
            a.add(
                0,
                ItemClass(
                    a.size.toString(),
                    title,
                    description,
                    date
                )
            )
            list.sortBy { it.date }
            position = index

        }
        save(position)
        recyclerAdapter.notifyDataSetChanged()
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

    fun deleteList() {
        reference.child(mAuth.currentUser!!.uid).removeValue()
    }

    fun saveList() {
        deleteList()
        list.forEach { day ->
            reference.child(mAuth.currentUser!!.uid)
                .child(day.date.toString())
                .setValue(day.itemClass)
                .addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        Log.d(".MainAcitivty", p0.printStackTrace().toString())
                    }
                })

        }
        recyclerAdapter.notifyDataSetChanged()

    }

    fun load()
    {
        Log.d(".MainActivity","Loading data")
        ArrayList<DaysClass>()
        reference.child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach { items ->
                    val arr = ArrayList<ItemClass>()
                    items.children.forEach {
                        val i = ItemClass(
                            it.key.toString(),
                            it.child("title").value.toString(),
                            it.child("description").value.toString(),
                            it.child("date").value.toString(),
                            it.child("checked").value.toString().toBoolean()
                        )
                        arr.add(i)
                        Log.d("Loading", "Loaded")
                    }
                    list.add(
                        DaysClass(
                            items.key,
                            arr
                        )
                    )
                    Log.d("Load", list.toString())
                    recyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("onCncelled", p0.message)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val stringBuilder = StringBuilder()
                    val uri = data!!.data as Uri
                    val mimeType = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                    Log.d("MimeType", mimeType)
                    if (mimeType.contains("json")) {
                        contentResolver.openInputStream(uri)?.use {
                            BufferedReader(InputStreamReader(it)).use { reader ->
                                var line = reader.readLine()
                                while (line != null) {
                                    stringBuilder.append(line)
                                    line = reader.readLine()
                                }
                            }
                        }
                        val result = stringBuilder.toString()
                        Log.d("ReadJso", result)
                        lateinit var temp: ArrayList<DaysClass>
                        temp = gson.fromJson(
                            result,
                            object : TypeToken<ArrayList<DaysClass>>() {}.type
                        )
                        list.clear()
                        recyclerAdapter.notifyDataSetChanged()
                        temp.forEach { day ->
                            list.add(day)
                        }
                        saveList()
                        Log.d("List", list.toString())
                        load()

                    }
                }
            }

            FILE_CREATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data as Uri
                    contentResolver.openFileDescriptor(uri, "w")?.use {
                        FileOutputStream(it.fileDescriptor).use {
                            it.write(gson.toJson(list).toByteArray())
                        }
                    }
                    Toast.makeText(this, uri.path, Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}