package com.example.calories.presentation.viewModel

import com.example.calories.domain.usecase.UserUseCase

class UserViewModel(private val userUseCase: UserUseCase) {

    fun load(){
        userUseCase.saveUserData()
    }
}