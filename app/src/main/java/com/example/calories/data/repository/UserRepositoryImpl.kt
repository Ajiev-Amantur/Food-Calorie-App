package com.example.calories.data.repository

import android.content.Context
import com.example.calories.domain.repository.UserRepository

class UserRepositoryImpl(private val context: Context): UserRepository {

    override fun saveUserParams(key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun isCompleted(goal: String) {
        val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("IsSetupComplete", true).apply()
        saveUserParams("goal",goal)
    }

}
