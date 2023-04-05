package com.example.dailyplanner

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.database.DailyPlannerDatabase
import com.example.dailyplanner.database.entities.DailyPlan
import com.example.dailyplanner.database.repositories.DailyPlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = DailyPlannerRepository(DailyPlannerDatabase.getInstance(this))
        val ui = this
        val adapter = DailyPlannerAdapter()
        val rvDailyItems = findViewById<RecyclerView>(R.id.rvDailyItems)
        val addTaskBtn = findViewById<ImageButton>(R.id.addTaskBtn)
        val clearAllBtn = findViewById<Button>(R.id.clearAllBtn)

        rvDailyItems.adapter = adapter
        rvDailyItems.layoutManager = LinearLayoutManager(this)

        addTaskBtn.setOnClickListener {
            displayDialog(repository, adapter)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllDailyPlanItems()
            adapter.setData(data, ui)
        }

        //Clear all plans
        clearAllBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                repository.deleteAll(repository.getAllDailyPlanItems())
                val data = repository.getAllDailyPlanItems()

                runOnUiThread {
                    adapter.setData(data, this@MainActivity)
                }
            }
        }
    }

    fun displayDialog(repository: DailyPlannerRepository, adapter: DailyPlannerAdapter) {
        // Create a new instance of AlertDialog.Builder
        val builder = AlertDialog.Builder(this)
        val inflator = layoutInflater
        val view: View = inflator.inflate(R.layout.dialog_layout, null)

        // Set the view
        builder.setView(view)

        val etTask = view.findViewById<EditText>(R.id.etTask)
        val tpDueTime= view.findViewById<TimePicker>(R.id.tpDueTime)

        // Set the positive button action
        builder.setPositiveButton("OK") { dialog, which ->
            //get the data
            val task = etTask.text.toString()
            var hour = tpDueTime.hour
            val min = tpDueTime.minute
            var am_pm = "AM"

            if (hour > 12){
                hour -= 12
                am_pm = "PM"
            }

            val dueTime = "$hour:$min$am_pm"

            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(DailyPlan(task, dueTime, false))
                val data = repository.getAllDailyPlanItems()

                runOnUiThread {
                    adapter.setData(data, this@MainActivity)
                }
            }
        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        // Create and show the alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }
}