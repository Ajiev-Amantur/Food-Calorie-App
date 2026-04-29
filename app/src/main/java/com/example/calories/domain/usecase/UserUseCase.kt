package com.example.calories.domain.usecase

import com.example.calories.domain.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {

    fun saveUserData(key: String, value: String) {
        userRepository.saveUserParams(key, value)
    }

    fun isCompleted() {
        userRepository.isCompleted()
    }
    fun calculateDailyCalories(): Map<String,Int>{

        val weightStr = userRepository.getParams("weight", "70")
        val heightStr = userRepository.getParams("height", "175")
        val ageStr = userRepository.getParams("age", "25")
        val goalMode = userRepository.getParams("goal", "weightNormal")
        val activityMode = userRepository.getParams("activity", "moveNormal")

        val weight = weightStr.filter { it.isDigit() }.toIntOrNull() ?: 70
        val height = heightStr.filter { it.isDigit() }.toIntOrNull() ?: 175
        val age = ageStr.filter { it.isDigit() }.toIntOrNull() ?: 25

        val bmr = (10* weight) + (6.25 * height) - (5 * age) + 5
        val move = when(activityMode){
            "moveLess" -> 1.2
            "moveMore" -> 1.55
            else -> 1.35
        }
        var finalKcal = bmr * move
        when(goalMode){
            "weightLess" -> finalKcal -= 500
            "weightMore" -> finalKcal +=500
        }

        return mapOf(
            "kcal" to finalKcal.toInt(),
            "protein" to (finalKcal * 0.20/4).toInt(),
            "fat" to (finalKcal * 0.30 / 9).toInt(),
            "carbs" to (finalKcal * 0.50 / 4).toInt()
        )
    }
}


