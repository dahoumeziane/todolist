package com.letsgo.firebaseapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.letsgo.firebaseapp.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.recoverButton.setOnClickListener {
            sendRecoveryEmail()
        }
        auth=Firebase.auth
    }
    fun sendRecoveryEmail(){
        val email = binding.inputEmail.text.toString()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Recover your account")
                    dialog.setMessage("We sent you an email to recover your account,check your inbox !")
                    dialog.setPositiveButton("Login",DialogInterface.OnClickListener{dialog,_ ->
                        val i = Intent(this@ForgetPasswordActivity,MainActivity::class.java)
                        startActivity(i)

                    })
                    dialog.show()
                }else{
                    Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }

        }
    }
}