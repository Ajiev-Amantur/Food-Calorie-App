package com.example.calories.domain.usecase

import com.example.calories.domain.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {

    fun saveUserData(key: String,value: String){
        userRepository.saveUserParams(key,value)
    }
    fun isCompleted(){
        userRepository.isCompleted()
    }
}