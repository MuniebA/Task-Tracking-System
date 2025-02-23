package com.example.assignment_5

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImportantFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_important, container, false)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Initialize the adapter with an empty list
        taskAdapter = TaskAdapter(emptyList(), this::onTaskClick, this::onTaskLongClick)
        recyclerView.adapter = taskAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        val dbHelper = DatabaseHelper(requireContext())
        val tasks = dbHelper.getAllTasks().filter { it.isImportant && !it.isCompleted }
        taskAdapter = TaskAdapter(tasks, this::onTaskClick, this::onTaskLongClick)
        recyclerView.adapter = taskAdapter
    }


    private fun onTaskClick(task: Task) {
        val intent = Intent(activity, AddEditTaskActivity::class.java).apply {
            putExtra("taskId", task.id)
        }
        startActivity(intent)
    }

    private fun onTaskLongClick(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteTask(task)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteTask(task: Task) {
        val dbHelper = DatabaseHelper(requireContext())
        val db = dbHelper.writableDatabase
        db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
        db.close()
        loadTasks() // Refresh the task list
        Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
    }
}
