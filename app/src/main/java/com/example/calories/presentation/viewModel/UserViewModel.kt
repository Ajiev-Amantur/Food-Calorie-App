package com.example.calories.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.calories.domain.usecase.UserUseCase

class UserViewModel(private val userUseCase: UserUseCase): ViewModel() {

    fun saveParams(key: String,value: String){
        userUseCase.saveUserData(key,value)
    }
    fun isCompleted(){
        userUseCase.isCompleted()
    }

    fun getDailyKcal(): Map<String ,Int>{
        return userUseCase.calculateDailyCalories()
    }
}