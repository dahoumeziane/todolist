package com.letsgo.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

import com.letsgo.firebaseapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.registerButton.setOnClickListener {
            if(checkFields()){
                // let's now open an account
                register()
            }
        }

    }
    fun checkFields():Boolean{
        if (binding.inputName.text.toString().isEmpty()){
            return false
        }else if (binding.inputEmail.text.toString().isEmpty()){
            return false
        }else if (binding.inputPassword.text.toString().isEmpty()){
            return false
        }else if (binding.inputConfirmPassword.text.toString().isEmpty()){
            return false
        }else if (binding.inputPassword.text.toString()
            != binding.inputConfirmPassword.text.toString()){
            return false
        }else{
            return true
        }
    }
    fun register(){
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                saveUserData(User(binding.inputName.text.toString(),email,auth.currentUser!!.uid,null))

             Toast.makeText(this,"Account opened successfully",Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun saveUserData(user : User){
        val userID = auth.currentUser!!.uid
        database.child("Users")
            .child(userID)
            .setValue(user)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val i = Intent(this@RegisterActivity,MainActivity::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
    }
}