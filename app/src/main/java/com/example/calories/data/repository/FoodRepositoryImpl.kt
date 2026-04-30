package com.example.calories.data.repository

import android.content.Context
import com.example.calories.data.model.FoodDto
import com.example.calories.data.foodMapper.toDomain
import com.example.calories.domain.model.Food
import com.example.calories.domain.repository.FoodRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodRepositoryImpl(private val context: Context) : FoodRepository {

    override suspend fun getAllFood(): List<Food> {

        val jsonString = context.assets.open("produscts.json")
            .bufferedReader().use { it.readText() }

        // 1. Парсим JSON в DTO
        val listType = object : TypeToken<List<FoodDto>>() {}.type
        val foodDtoList: List<FoodDto> = Gson().fromJson(jsonString, listType)

        // 2. Превращаем DTO в чистые модели с помощью маппера
        return foodDtoList.map { it.toDomain() }
    }
}


