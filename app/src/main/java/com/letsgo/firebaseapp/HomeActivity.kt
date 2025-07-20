package com.letsgo.firebaseapp

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.letsgo.firebaseapp.databinding.ActivityHomeBinding
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()

        binding.home.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
        }
        binding.profile.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment()).commit()
        }

    }

}



