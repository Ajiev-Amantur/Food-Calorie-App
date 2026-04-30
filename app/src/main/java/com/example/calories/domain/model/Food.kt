package com.example.calories.domain.model

data class Food(
    val id: Int = 0,
    val name: String,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val description: String,
    val date: Long,
    val mealType: Int
)