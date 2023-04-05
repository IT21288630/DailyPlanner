package com.example.dailyplanner.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyPlan(
    val task: String,
    val dueTime: String,
    val isDone: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
