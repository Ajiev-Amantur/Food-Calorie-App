package com.example.calories.data.repository

import android.content.Context
import com.example.calories.data.FoodDto
import com.example.calories.domain.repository.FoodRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodRepositoryImpl(private val context: Context) : FoodRepository {

    override suspend fun getAllFood(): List<FoodDto> {
        val foodDtoList = mutableListOf<FoodDto>()


        val jsonString = context.assets.open("produscts.json")
            .bufferedReader().use { it.readText() }

        val listType = object : TypeToken<List<FoodDto>>() {}.type

        return Gson().fromJson(jsonString,listType)
    }
}


