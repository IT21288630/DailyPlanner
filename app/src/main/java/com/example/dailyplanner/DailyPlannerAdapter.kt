package com.example.dailyplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.database.DailyPlannerDatabase
import com.example.dailyplanner.database.entities.DailyPlan
import com.example.dailyplanner.database.repositories.DailyPlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyPlannerAdapter() : RecyclerView.Adapter<DailyPlannerAdapter.DailyPlannerViewHolder>() {

    lateinit var data: List<DailyPlan>
    lateinit var context: Context

    inner class DailyPlannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvDate: TextView
        val doneBtn: Button
        val deleteBtn: ImageButton
        val tvCompleted: TextView
        val taskBg: View

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDate = itemView.findViewById(R.id.tvDate)
            doneBtn = itemView.findViewById(R.id.doneBtn)
            tvCompleted = itemView.findViewById(R.id.tvCompleted)
            taskBg = itemView.findViewById(R.id.taskBg)
            deleteBtn = itemView.findViewById(R.id.deleteBtn)
        }
    }

    fun setData(data: List<DailyPlan>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyPlannerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_plan, parent, false)
        return DailyPlannerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DailyPlannerViewHolder, position: Int) {
        holder.apply {
            tvTitle.text = data[position].task
            tvDate.text = "Due At " + data[position].dueTime

            if (data[position].isDone) {
                doneBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                tvCompleted.text = "Completed"
                tvCompleted.visibility = View.VISIBLE
                taskBg.background = itemView.resources.getDrawable(R.drawable.completed_task)
            } else {
                doneBtn.text = "Done"
                doneBtn.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE
                tvCompleted.visibility = View.INVISIBLE
                taskBg.background = itemView.resources.getDrawable(R.drawable.rounded_corners)

                doneBtn.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val repository =
                            DailyPlannerRepository(DailyPlannerDatabase.getInstance(context))
                        repository.updateItem(true, data[position].id)
                        val data = repository.getAllDailyPlanItems()

                        withContext(Dispatchers.Main) {
                            setData(data, context)
                        }
                    }
                }

                deleteBtn.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val repository =
                            DailyPlannerRepository(DailyPlannerDatabase.getInstance(context))
                        repository.delete(data[position])
                        val data = repository.getAllDailyPlanItems()

                        withContext(Dispatchers.Main) {
                            setData(data, context)
                        }
                    }
                }
            }




        }
    }
}