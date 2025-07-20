package com.letsgo.firebaseapp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

object Utils {
    val auth = Firebase.auth
    val taskRef = Firebase.database.reference.child("Tasks")
    fun addTaskDialog(context: Context , task: Task, isUpdate: Boolean):Dialog{
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_dialog)
        val task_name = dialog.findViewById<EditText>(R.id.task_input_name)
        val task_category = dialog.findViewById<TextView>(R.id.task_input_category)
        val task_description = dialog.findViewById<EditText>(R.id.task_input_description)
        val task_add = dialog.findViewById<Button>(R.id.add_task)
        if (isUpdate){
            task_add.text = "Update"
            task_name.setText(task!!.title)
            task_category.setText(task!!.category)
            task_description.setText(task.description)
        }

        task_add.setOnClickListener {
            val task = Task(
                task_name.text.toString(), task_description.text.toString(),
                task_category.text.toString(), false, task.id
            )
            Utils.taskRef.child(Firebase.auth.currentUser!!.uid).child(task.id).setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
        }
        val cateogries = arrayOf("Sport", "Work", "Others")
        task_category.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Select the category of the task")
            alertDialog.setSingleChoiceItems(cateogries, -1, DialogInterface.OnClickListener
            { dialogInterface, i ->
                task_category.text = cateogries[i]
                dialogInterface.dismiss()

            })
            alertDialog.show()
        }
        return dialog
    }
}