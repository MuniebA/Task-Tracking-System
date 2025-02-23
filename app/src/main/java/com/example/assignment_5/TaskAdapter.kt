package com.example.assignment_5

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TaskAdapter(
    private val taskList: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_COMPLETED = 1
        private const val TYPE_INCOMPLETE = 2
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskIcon: ImageView = itemView.findViewById(R.id.task_icon)
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDueDate: TextView = itemView.findViewById(R.id.task_due_date)
        val taskAlertIcon: ImageView? = itemView.findViewById(R.id.task_alert_icon)
        val taskCompleteLabel: TextView? = itemView.findViewById(R.id.task_complete_label)
        val taskCompleteSwitch: Switch? = itemView.findViewById(R.id.task_complete_switch)
        val taskDaysLeft: TextView? = itemView.findViewById(R.id.task_days_left)
    }

    override fun getItemViewType(position: Int): Int {
        return if (taskList[position].isCompleted) TYPE_COMPLETED else TYPE_INCOMPLETE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = if (viewType == TYPE_COMPLETED) R.layout.completed_task_item else R.layout.task_item
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = taskList[position]
        val taskViewHolder = holder as TaskViewHolder
        taskViewHolder.taskName.text = task.name
        taskViewHolder.taskDueDate.text = task.dueDate

        if (holder.itemViewType == TYPE_COMPLETED) {
            taskViewHolder.taskIcon.setImageResource(R.drawable.checklist_64)
        } else {
            // Calculate days left
            val daysLeft = calculateDaysLeft(task.dueDate)
            taskViewHolder.taskDaysLeft?.text = "$daysLeft days"

            // Set task completion switch and label color
            taskViewHolder.taskCompleteSwitch?.isChecked = task.isCompleted
            if (task.isCompleted) {
                taskViewHolder.taskCompleteLabel?.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_green_dark))
            } else {
                taskViewHolder.taskCompleteLabel?.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_red_dark))
            }

            // Show alert icon if the task is important
            if (task.isImportant) {
                taskViewHolder.taskAlertIcon?.visibility = View.VISIBLE
            } else {
                taskViewHolder.taskAlertIcon?.visibility = View.GONE
            }

            // Handle item click
            holder.itemView.setOnClickListener {
                onTaskClick(task)
            }

            // Handle item long click for deletion
            holder.itemView.setOnLongClickListener {
                onTaskLongClick(task)
                true
            }

            // Update task completion status in database when the switch is toggled
            taskViewHolder.taskCompleteSwitch?.setOnCheckedChangeListener { _, isChecked ->
                if (task.isCompleted != isChecked) {
                    task.isCompleted = isChecked
                    updateTaskCompletionStatus(holder.itemView.context, task)
                }
            }
        }
    }

    override fun getItemCount() = taskList.size

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDaysLeft(dueDate: String): Int {
        // Define a formatter that accepts both zero-padded and non-zero-padded dates
        val formatter = DateTimeFormatter.ofPattern("[yyyy-M-d][yyyy-MM-dd]")
        val dueDateParsed = LocalDate.parse(dueDate, formatter)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, dueDateParsed).toInt()
    }

    private fun updateTaskCompletionStatus(context: Context, task: Task) {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        db.update("tasks", contentValues, "id = ?", arrayOf(task.id.toString()))
        db.close()
    }
}
