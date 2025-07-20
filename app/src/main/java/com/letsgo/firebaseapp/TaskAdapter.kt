package com.letsgo.firebaseapp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.letsgo.firebaseapp.Utils.auth

class TaskAdapter(
    val context: Context,
    val tasks: ArrayList<Task>
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskTitle.text = tasks[position].title
        holder.taskDescription.text = tasks[position].description
        holder.taskCategory.text = tasks[position].category
        if (tasks[position].category == "Sport") {
            holder.taskCategory.setBackgroundResource(R.drawable.green_background)
        } else if (tasks[position].category == "Work") {
            holder.taskCategory.setBackgroundResource(R.drawable.blue_background)

        } else {
            holder.taskCategory.setBackgroundResource(R.drawable.orange_background)
        }
        if (tasks[position].done) {
            holder.taskCheckBox.isChecked = true
        } else {
            holder.taskCheckBox.isChecked = false
        }

        // when taskcheckbox change state ..
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Checkbox is checked
                Utils.taskRef.child(auth.currentUser!!.uid).child(tasks[position].id).child("done").setValue(true)
                Toast.makeText(context, "Checkbox is checked!", Toast.LENGTH_SHORT).show()
            } else {
                // Checkbox is unchecked
                Utils.taskRef.child(auth.currentUser!!.uid).child(tasks[position].id).child("done").setValue(false)
                Toast.makeText(context, "Checkbox is checked!", Toast.LENGTH_SHORT).show()

            }
        }

        //delete
        holder.itemView.setOnClickListener {
            val choices = arrayOf("Modify", "Delete")
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setSingleChoiceItems(choices, -1, DialogInterface
                .OnClickListener { dialogInterface, i ->
                    if (i == 0) {
                            // modify the task
                       val dialog = Utils.addTaskDialog(context,tasks[position],true)
                        dialog.show()
                    } else if (i == 1) {
                        // delete the task
                        Utils.taskRef.child(auth.currentUser!!.uid).child(tasks[position].id).removeValue()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Task removed", Toast.LENGTH_SHORT)
                                        .show()

                                } else {
                                    Toast.makeText(
                                        context,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                    }
                    dialogInterface.dismiss()

                })
            alertDialog.show()
        }

        // priority
        if (tasks[position].priority!=null){
            holder.taskPriority.text = tasks[position].priority
            if (tasks[position].priority=="Must do"){
                holder.taskPriority.setBackgroundResource(R.drawable.must_do_background)
            }else if (tasks[position].priority=="Should do"){
                holder.taskPriority.setBackgroundResource(R.drawable.should_do_background)

            }else{
                holder.taskPriority.setBackgroundResource(R.drawable.optional_background)

            }
        }else{
            holder.taskPriority.visibility = View.GONE
        }
    }
}