package com.example.todolistkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(), View.OnClickListener
{


    private val REQUEST_SIGN_IN_GOOGLE = 101
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        mAuth = FirebaseAuth.getInstance()
        val googleButton: SignInButton = findViewById(R.id.sign_google)
        googleButton.setOnClickListener(this)
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    private fun changeActivity(user: FirebaseUser)
    {
        val i: Intent = Intent(this,MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun signIn()
    {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, REQUEST_SIGN_IN_GOOGLE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode,resultCode,data)
        println(resultCode)
        if(requestCode == REQUEST_SIGN_IN_GOOGLE && resultCode == Activity.RESULT_OK)
        {
            println("okokokokokok")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            authWithGoogle(account!!)
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.sign_google -> signIn()
        }
    }

    private fun authWithGoogle(account: GoogleSignInAccount)
    {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful)
                {
                    val user = mAuth.currentUser
                    changeActivity(user!!)
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
            changeActivity(currentUser)
    }
}