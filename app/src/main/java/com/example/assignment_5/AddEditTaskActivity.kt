package com.example.assignment_5

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddEditTaskActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextDueDate: EditText
    private lateinit var checkBoxImportant: CheckBox
    private lateinit var buttonSave: Button
    private lateinit var imageViewCalendar: ImageView

    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        editTextName = findViewById(R.id.edit_text_name)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextDueDate = findViewById(R.id.edit_text_due_date)
        checkBoxImportant = findViewById(R.id.check_box_important)
        buttonSave = findViewById(R.id.button_save)
        imageViewCalendar = findViewById(R.id.image_view_calendar)

        taskId = intent.getIntExtra("taskId", -1)

        if (taskId != -1) {
            loadTask()
            buttonSave.text = getString(R.string.update_task)
        } else {
            buttonSave.text = getString(R.string.create_task)
        }

        editTextDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        imageViewCalendar.setOnClickListener {
            showDatePickerDialog()
        }

        buttonSave.setOnClickListener {
            if (validateInputs()) {
                saveTask()
            }
        }
    }

    private fun loadTask() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ?", arrayOf(taskId.toString()))

        if (cursor.moveToFirst()) {
            editTextName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")))
            editTextDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")))
            editTextDueDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("dueDate")))
            checkBoxImportant.isChecked = cursor.getInt(cursor.getColumnIndexOrThrow("isImportant")) == 1
        }

        cursor.close()
        db.close()
    }

    private fun saveTask() {
        val name = editTextName.text.toString()
        val description = editTextDescription.text.toString()
        val dueDate = editTextDueDate.text.toString()
        val isImportant = checkBoxImportant.isChecked

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("dueDate", dueDate)
            put("isImportant", if (isImportant) 1 else 0)
        }

        if (taskId == -1) {
            db.insert("tasks", null, contentValues)
        } else {
            db.update("tasks", contentValues, "id = ?", arrayOf(taskId.toString()))
        }

        db.close()
        finish()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                editTextDueDate.setText(date)
            },
            year, month, day
        )

        // Set the minimum date to today's date
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        if (editTextName.text.isEmpty()) {
            Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (editTextDueDate.text.isEmpty()) {
            Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
