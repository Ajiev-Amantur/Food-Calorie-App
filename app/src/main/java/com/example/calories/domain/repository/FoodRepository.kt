package com.example.calories.domain.repository

import com.example.calories.domain.model.Food

interface FoodRepository {
     suspend fun getAllFood(): List<Food>

}