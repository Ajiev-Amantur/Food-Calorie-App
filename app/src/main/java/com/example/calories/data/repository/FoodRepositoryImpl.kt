package com.example.calories.data.repository

import android.content.Context
import android.util.Log
import com.example.calories.data.model.FoodDto
import com.example.calories.data.foodMapper.toDomain
import com.example.calories.domain.model.Food
import com.example.calories.domain.repository.FoodRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodRepositoryImpl(private val context: Context) : FoodRepository {

    override suspend fun getAllFood(): List<Food> {
        return try {
            val jsonString = context.assets.open("produscts.json")
                .bufferedReader().use { it.readText() }

            val listType = object : TypeToken<List<FoodDto>>() {}.type
            val foodDtoList: List<FoodDto> = Gson().fromJson(jsonString, listType)

            foodDtoList.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("FOOD_ERROR", "Ошибка загрузки JSON: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}
