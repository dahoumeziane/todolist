package com.letsgo.firebaseapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.letsgo.firebaseapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var auth: FirebaseAuth
    var tasks = ArrayList<Task>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        binding.addtask.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.custom_dialog)
            val task_name = dialog.findViewById<EditText>(R.id.task_input_name)
            val task_category = dialog.findViewById<TextView>(R.id.task_input_category)
            val task_description = dialog.findViewById<EditText>(R.id.task_input_description)
            val task_add = dialog.findViewById<Button>(R.id.add_task)
            val task_priority = dialog.findViewById<TextView>(R.id.task_input_priority)

            task_add.setOnClickListener {
                val taskId = Utils.taskRef.child(Utils.auth.currentUser!!.uid).push().key
                val task = Task(
                    task_name.text.toString(), task_description.text.toString(),
                    task_category.text.toString(), false, taskId!!,task_priority.text.toString()
                )
                addTaskToDb(task)
                dialog.dismiss()

            }
            val cateogries = arrayOf("Sport", "Work", "Others")
            task_category.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Select the category of the task")
                alertDialog.setSingleChoiceItems(cateogries, -1, DialogInterface.OnClickListener
                { dialogInterface, i ->
                    task_category.text = cateogries[i]
                    dialogInterface.dismiss()

                })
                alertDialog.show()
            }
            val priorities= arrayOf("Must do","Should do","Optional")
            task_priority.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Select your task priority")
                alertDialog.setSingleChoiceItems(priorities,-1,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        task_priority.text = priorities[i]
                        dialogInterface.dismiss()
                    })
                alertDialog.show()
            }
            dialog.show()
        }

        Utils.taskRef.child(Utils.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks=ArrayList()
                if (snapshot.exists()){
                    val data = snapshot.children
                    data.forEach { child->
                        tasks.add(child.getValue<Task>()!!)
                    }
                    if(tasks.isNotEmpty()){
                        val adapter = TaskAdapter(requireContext(),tasks)
                        binding.taskList.adapter = adapter
                        val manager = LinearLayoutManager(requireContext())
                        binding.taskList.layoutManager=manager
                        binding.emptyLayout.visibility= View.GONE
                        binding.taskList.visibility=View.VISIBLE
                    }
                }else{
                    binding.taskList.visibility=View.GONE
                    binding.emptyLayout.visibility= View.VISIBLE
                }






            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }

    fun addTaskToDb(task: Task) {
        Utils.taskRef.child(Utils.auth.currentUser!!.uid).child(task.id).setValue(task).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    task.exception!!.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

}