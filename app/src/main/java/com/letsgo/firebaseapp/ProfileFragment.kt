package com.letsgo.firebaseapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.letsgo.firebaseapp.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage: StorageReference
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    var ImageUri: Uri? = null
    lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root
        //code of the fragment
        auth = Firebase.auth
        database = Firebase.database.reference
        storage = Firebase.storage.reference.child("Profile pictures")
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null) {
                    ImageUri = result.data!!.data!!

                    binding.profilePic.setImageURI(ImageUri)
                }

            }
        retrieveUserData()

        binding.logout.setOnClickListener {
            logout()
        }

        binding.update.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.update.visibility = View.GONE
            if (ImageUri != null) {
                val userId = auth.currentUser!!.uid
                storage.child("$userId.jpeg")
                    .putFile(ImageUri!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            storage.child("$userId.jpeg")
                                .downloadUrl.addOnSuccessListener { link ->
                                    val profileURL = link.toString()
                                    val user = User(
                                        binding.name.text.toString(),
                                        binding.email.text.toString(),
                                        auth.currentUser!!.uid,
                                        profileURL
                                    )
                                    updateUserData(user)
                                }

                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.update.visibility = View.VISIBLE
                        }

                    }
            } else {
                val user = User(
                    binding.name.text.toString(),
                    binding.email.text.toString(),
                    auth.currentUser!!.uid,
                    null
                )
                updateUserData(user)
            }

        }

        binding.profilePic.setOnClickListener {
            openGallery(resultLauncher)

        }


        return view
    }

   fun logout() {
        auth.signOut()
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
        activity?.finish()


    }

    fun retrieveUserData() {
        val currentUserId = auth.currentUser!!.uid
        database
            .child("Users")
            .child(currentUserId)
            .get().addOnSuccessListener { data ->
                val user = data.getValue<User>()
                binding.name.setText(user!!.username)
                binding.email.setText(user!!.email)
                if (user.profilePic!=null){
                    Picasso.get().load(user.profilePic).into(binding.profilePic)
                }

            }.addOnFailureListener {

            }
    }

    fun updateUserData(user: User) {
        database.child("Users")
            .child(auth.currentUser!!.uid)
            .setValue(user).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                binding.update.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
    }

    fun openGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        launcher.launch(intent)
    }
}