package com.example.calories.domain.repository

import com.example.calories.data.FoodDto

interface FoodRepository {
     suspend fun getAllFood(): List<FoodDto>

}