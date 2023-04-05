package com.example.dailyplanner.database.daos

import androidx.room.*
import com.example.dailyplanner.database.entities.DailyPlan

@Dao
interface DailyPlannerDao {
    @Insert
    suspend fun insertItem(dailyPlan: DailyPlan)

    @Delete
    suspend fun delete(dailyPlan: DailyPlan)

    @Delete
    suspend fun deleteAll(dailyPlans: List<DailyPlan>)

    @Query("SELECT * From DailyPlan")
    fun getAllDailyPlanItems(): List<DailyPlan>

    @Query("UPDATE DailyPlan SET isDone = :isDone WHERE id = :id")
    suspend fun updateItem(isDone: Boolean, id: Int?)
}