package com.example.calories.data.repository

import android.content.Context
import com.example.calories.data.Food
import com.example.calories.domain.repository.FoodRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodRepositoryImpl(private val context: Context) : FoodRepository {

    override suspend fun getAllFood(): List<Food> {
        val foodList = mutableListOf<Food>()


        val jsonString = context.assets.open("produscts.json")
            .bufferedReader().use { it.readText() }

        val listType = object : TypeToken<List<Food>>() {}.type

        return Gson().fromJson(jsonString,listType)
    }
}


