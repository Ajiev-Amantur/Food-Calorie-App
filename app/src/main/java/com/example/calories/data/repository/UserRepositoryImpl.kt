package com.example.calories.data.repository

import android.content.Context
import com.example.calories.domain.repository.UserRepository

class UserRepositoryImpl(private val context: Context): UserRepository {

    override fun saveUserParams(key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun isCompleted() {
        val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("IsSetupComplete", true).apply()
    }

    override fun getParams(key: String, def: String):String {
        val data = context.getSharedPreferences("Data",Context.MODE_PRIVATE)
        return data.getString(key,def)?: def
    }
}
