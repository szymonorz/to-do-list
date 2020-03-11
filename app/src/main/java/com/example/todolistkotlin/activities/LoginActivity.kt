package com.example.todolistkotlin.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistkotlin.R
import com.example.todolistkotlin.fragments.LoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity()
{


    private val REQUEST_SIGN_IN_GOOGLE = 101
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        mAuth = FirebaseAuth.getInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment, LoginFragment()).commit()



    }

    private fun changeActivity()
    {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode,resultCode,data)
        when (requestCode)
        {
            REQUEST_SIGN_IN_GOOGLE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    authWithGoogle(account!!)
                }
            }

        }

    }


    private fun authWithGoogle(account: GoogleSignInAccount)
    {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        Log.d("LoginGoogle", "authWithGoogle")
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful)
                {
                    changeActivity()
                }else
                {
                    Toast.makeText(this,"Failed", Toast.LENGTH_LONG).show()
                }

            }
    }


    override fun onStart() {
        super.onStart()
        println(mAuth.currentUser)
        val currentUser= mAuth.currentUser
        if(currentUser!=null)
            changeActivity()
    }
}