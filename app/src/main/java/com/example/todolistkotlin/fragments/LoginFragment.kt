package com.example.todolistkotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.todolistkotlin.R
import com.example.todolistkotlin.activities.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment(), View.OnClickListener {


    private val REQUEST_SIGN_IN_GOOGLE = 101
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val convertView = inflater.inflate(R.layout.login_fragment, container, false)
        convertView.register.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
        val googleButton: SignInButton = convertView.findViewById(R.id.sign_google)
        googleButton.setOnClickListener(this)
        val loginButton = convertView.findViewById<MaterialButton>(R.id.sign_email)
        loginButton.setOnClickListener(this)


        return convertView
    }

    private fun signInGoogle() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, REQUEST_SIGN_IN_GOOGLE)
    }

    private fun authWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    changeActivity()
                } else {
                    Toast.makeText(requireContext(), "Failed to log in", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeActivity() {
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
        activity!!.finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.sign_google -> signInGoogle()
            R.id.sign_email -> {
                val email = view!!.findViewById<EditText>(R.id.email_login).text.toString()
                val password = view!!.findViewById<EditText>(R.id.password_login).text.toString()
                authWithEmailAndPassword(email, password)
            }
            R.id.register -> fragmentManager!!.beginTransaction()
                .replace(R.id.fragment, RegisterFragment()).addToBackStack(null).commit()
        }


    }
}