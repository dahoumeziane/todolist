package com.letsgo.firebaseapp

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(itemView : View):RecyclerView.ViewHolder(itemView) {
    val taskTitle = itemView.findViewById<TextView>(R.id.task_title)
    val taskDescription = itemView.findViewById<TextView>(R.id.task_description)
    val taskCategory = itemView.findViewById<TextView>(R.id.task_category)
    val taskCheckBox = itemView.findViewById<CheckBox>(R.id.checkbox)
    val taskPriority = itemView.findViewById<TextView>(R.id.task_priority)
}