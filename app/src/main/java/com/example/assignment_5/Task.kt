package com.example.assignment_5

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val dueDate: String,
    val isImportant: Boolean,
    var isCompleted: Boolean
)
