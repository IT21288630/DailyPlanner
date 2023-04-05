package com.example.dailyplanner.database.repositories

import com.example.dailyplanner.database.DailyPlannerDatabase
import com.example.dailyplanner.database.entities.DailyPlan

class DailyPlannerRepository(
    private val db: DailyPlannerDatabase
) {
    suspend fun insert(dailyPlan: DailyPlan) = db.getDailyPlanDao().insertItem(dailyPlan)
    suspend fun delete(dailyPlan: DailyPlan) = db.getDailyPlanDao().delete(dailyPlan)
    suspend fun deleteAll(dailyPlans: List<DailyPlan>) = db.getDailyPlanDao().deleteAll(dailyPlans)
    suspend fun updateItem(isDone: Boolean, id: Int?) = db.getDailyPlanDao().updateItem(isDone, id)
    fun getAllDailyPlanItems() = db.getDailyPlanDao().getAllDailyPlanItems()
}