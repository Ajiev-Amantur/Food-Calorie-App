package com.example.calories.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calories.data.model.FoodDto
import com.example.calories.domain.model.Food

@Database(entities = [FoodDto::class],version = 1)
abstract class FoodDataBase: RoomDatabase() {

    abstract fun foodDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: FoodDataBase? = null
        fun getDataBase(context: Context): FoodDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDataBase::class.java,
                    "food_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}