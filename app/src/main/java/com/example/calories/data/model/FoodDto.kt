package com.example.calories.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val description: String = "",
    val date: Long,
    val mealType: Int // 1-завтрак, 2-обед, 3-ужин, 4-перекус
)