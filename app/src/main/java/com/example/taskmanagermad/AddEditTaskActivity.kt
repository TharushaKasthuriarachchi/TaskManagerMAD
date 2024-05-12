package com.example.taskmanagermad

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddEditTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    lateinit var taskTitleEdt: EditText
    lateinit var taskDescriptionEdt: EditText
    lateinit var spinnerPriority: Spinner
    lateinit var selectDueDateBtn: Button
    lateinit var taskDueDateEdt: EditText
    lateinit var addUpdateBtn: Button
    lateinit var viewModel: TaskViewModel
    var taskID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        taskTitleEdt = findViewById(R.id.idEditTaskTitle)
        taskDescriptionEdt = findViewById(R.id.idEditTaskDescription)
        spinnerPriority = findViewById(R.id.idSpinnerPriority)
        selectDueDateBtn = findViewById(R.id.idBtnSelectDueDate)
        taskDueDateEdt = findViewById(R.id.idEditTaskDueDate)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(TaskViewModel::class.java)

        val taskType = intent.getStringExtra("taskType")
        if (taskType.equals("Edit")) {
            val taskTitle = intent.getStringExtra("taskTitle")
            val taskDesc = intent.getStringExtra("taskDescription")
            val taskPriority = intent.getStringExtra("taskPriority")
            val taskDueDate = intent.getStringExtra("taskDueDate")
            taskID = intent.getIntExtra("taskID", -1)
            addUpdateBtn.setText("Update Task")
            taskTitleEdt.setText(taskTitle)
            taskDescriptionEdt.setText(taskDesc)
            val priorityArray = resources.getStringArray(R.array.priority_array)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityArray)
            spinnerPriority.adapter = adapter
            spinnerPriority.setSelection(priorityArray.indexOf(taskPriority))
            taskDueDateEdt.setText(taskDueDate)
        } else {
            addUpdateBtn.setText("Save Task")
            val priorityArray = resources.getStringArray(R.array.priority_array)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityArray)
            spinnerPriority.adapter = adapter
        }

        selectDueDateBtn.setOnClickListener {
            showDatePickerDialog()
        }

        addUpdateBtn.setOnClickListener {
            val taskTitle = taskTitleEdt.text.toString()
            val taskDescription = taskDescriptionEdt.text.toString()
            val taskPriority = spinnerPriority.selectedItem.toString()
            val taskDueDate = taskDueDateEdt.text.toString()

            if (taskType.equals("Edit")) {
                if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    val updateTask = Task(taskTitle, taskDescription, taskPriority, taskDueDate, currentDate)
                    updateTask.id = taskID
                    viewModel.updateTask(updateTask)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    viewModel.addTask(Task(taskTitle, taskDescription, taskPriority, taskDueDate, currentDate))
                    Toast.makeText(this, "Note Added..", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$dayOfMonth ${getMonthName(month)}, $year"
        taskDueDateEdt.setText(selectedDate)
    }

    private fun getMonthName(month: Int): String {
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return months[month]
    }
}
