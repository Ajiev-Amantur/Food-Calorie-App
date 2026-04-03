package com.example.calories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM food")
    suspend fun getAllFood(): List<Food>

    @Query("SELECT * FROM food WHERE date >= :startTime AND date <= :endTime")
    suspend fun getFoodByDate(startTime: Long, endTime: Long): List<Food>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)
}