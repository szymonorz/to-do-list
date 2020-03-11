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
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.register_fragment.view.*

class RegisterFragment : Fragment(), View.OnClickListener {
    lateinit var emailEdit: EditText
    lateinit var passwordEdit1: EditText
    lateinit var passwordEdit2: EditText
    private val emailRegExp = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val convertView = inflater.inflate(R.layout.register_fragment, container, false)
        emailEdit = convertView.findViewById(R.id.email_register)

        convertView.login.setOnClickListener(this)
        val registerButton = convertView.findViewById<MaterialButton>(R.id.register_email)
        registerButton.setOnClickListener(this)
        passwordEdit1 = convertView.findViewById<EditText>(R.id.password_register_1)
        passwordEdit2 = convertView.findViewById<EditText>(R.id.password_register_2)



        return convertView
    }


    private fun createUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                changeActivity()
            } else {
                Toast.makeText(requireContext(), "Failed to create a user", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun changeActivity() {
        val i: Intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
        activity!!.finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login -> fragmentManager!!.beginTransaction()
                .replace(R.id.fragment, LoginFragment()).commit()
            R.id.register_email -> {
                val email = emailEdit.text.toString()
                val pass1 = passwordEdit1.text.toString()
                val pass2 = passwordEdit2.text.toString()
                if (email.matches(emailRegExp) && pass1.length >= 8 && pass1.equals(pass2))
                    createUser(email, pass1)
                else
                    Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()

            }
        }
    }
}