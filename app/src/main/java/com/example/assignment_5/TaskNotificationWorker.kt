package com.example.assignment_5

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val dbHelper = DatabaseHelper(context)
        val tasks = dbHelper.getAllTasks()
        val today = LocalDate.now()

        tasks.forEach { task ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
            val dueDate = LocalDate.parse(task.dueDate, formatter)

            if (!task.isCompleted) {
                val daysLeft = dueDate.toEpochDay() - today.toEpochDay()
                if (daysLeft <= 2) {
                    sendNotification(task, daysLeft)
                }
            }
        }
        return Result.success()
    }

    private fun sendNotification(task: Task, daysLeft: Long) {
        val message = if (daysLeft < 0) {
            "Task ${task.name} is overdue!"
        } else {
            "Task ${task.name} is due in $daysLeft days!"
        }

        val notification = NotificationCompat.Builder(context, MyApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.task64)
            .setContentTitle("Task Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(task.id, notification)
    }
}
