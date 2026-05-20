package com.example.calories.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.calories.data.model.FoodDto

@Dao
interface FoodDao {
    @Query("SELECT * FROM food")
    suspend fun getAllFood(): List<FoodDto>

    @Query("SELECT * FROM food WHERE date >= :startTime AND date <= :endTime")
    suspend fun getFoodByDate(startTime: Long, endTime: Long): List<FoodDto>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addFood(foodDto: FoodDto)

    @Delete
    suspend fun deleteFood(foodDto: FoodDto)
}