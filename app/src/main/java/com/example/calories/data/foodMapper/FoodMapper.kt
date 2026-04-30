package com.example.calories.data.foodMapper

import com.example.calories.data.model.FoodDto
import com.example.calories.domain.model.Food

fun FoodDto.toDomain(): Food {
    return Food(
        id = this.id,
        name = this.name,
        calories = this.calories,
        carbs = this.carbs,
        protein = this.protein,
        fat = this.fat,
        description = this.description,
        date = this.date,
        mealType = this.mealType
    )
}
    fun Food.toDto(): FoodDto{
        return FoodDto(
            id = this.id,
            name = this.name,
            calories = this.calories,
            carbs = this.carbs,
            protein = this.protein,
            fat = this.fat,
            description = this.description,
            date = this.date,
            mealType = this.mealType
        )
    }
