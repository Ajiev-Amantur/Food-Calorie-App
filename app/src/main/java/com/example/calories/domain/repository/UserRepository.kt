package com.example.calories.domain.repository


interface UserRepository {
    fun saveUserParams(key: String,value: String)

    fun isCompleted()


    fun getParams(key: String,def: String): String
}