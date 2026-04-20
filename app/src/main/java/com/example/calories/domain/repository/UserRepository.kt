package com.example.calories.domain.repository

interface UserRepository {
    fun saveUserParams(key: String,value: String)

    fun isCompleted()
}