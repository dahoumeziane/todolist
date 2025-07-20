package com.letsgo.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.letsgo.firebaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth


        binding.goRegister.setOnClickListener {
            val i = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(i)
        }
        binding.loginButton.setOnClickListener {
            if (checkFields()) {
                login()
            }
        }
        binding.forgetPassword.setOnClickListener {
            val i = Intent(this@MainActivity, ForgetPasswordActivity::class.java)
            startActivity(i)
        }
    }

    fun checkFields(): Boolean {
        if (binding.inputEmail.text.toString().isEmpty()) {
            return false
        } else if (binding.inputPassword.text.toString().isEmpty()) {
            return false
        } else {
            return true
        }
    }

    fun login() {
        val email = binding.inputEmail.text.toString()
        val pwd = binding.inputPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val i = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val i = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        }
    }


}