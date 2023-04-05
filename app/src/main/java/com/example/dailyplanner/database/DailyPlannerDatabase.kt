package com.example.dailyplanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailyplanner.database.daos.DailyPlannerDao
import com.example.dailyplanner.database.entities.DailyPlan

@Database(entities = [DailyPlan::class], version = 1)
abstract class DailyPlannerDatabase : RoomDatabase() {

    abstract fun getDailyPlanDao(): DailyPlannerDao

    companion object {
        @Volatile
        private var INSTANCE: DailyPlannerDatabase? = null

        fun getInstance(context: Context): DailyPlannerDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DailyPlannerDatabase::class.java,
                    "daily_plan_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}