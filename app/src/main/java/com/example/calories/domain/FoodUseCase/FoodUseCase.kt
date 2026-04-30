package com.example.calories.domain.FoodUseCase

import android.util.Log
import com.example.calories.domain.model.Food
import com.example.calories.domain.repository.FoodRepository

class FoodUseCase(private val foodRepository: FoodRepository) {

    suspend fun getAllFoods(): List<Food>{
       val food = foodRepository.getAllFood()
        if (food.isEmpty()){
            Log.d("FoodUseCase", "No food found")
            food.isEmpty()
            }
        return food
    }
}