package com.example.calories.domain.repository

import com.example.calories.data.Food

interface FoodRepository {
     suspend fun getAllFood(): List<Food>

}