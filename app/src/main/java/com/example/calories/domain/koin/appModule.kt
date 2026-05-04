package com.example.calories.domain.koin

import com.example.calories.data.repository.FoodRepositoryImpl
import com.example.calories.data.repository.UserRepositoryImpl
import com.example.calories.domain.FoodUseCase.FoodUseCase
import com.example.calories.domain.repository.FoodRepository
import com.example.calories.domain.repository.UserRepository
import com.example.calories.domain.usecase.UserUseCase
import com.example.calories.presentation.FoodViewModel.FoodViewModel
import com.example.calories.presentation.viewModel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single<UserRepository> { UserRepositoryImpl(androidContext()) }
    factory { UserUseCase(get()) }
    viewModel { UserViewModel(get()) }

    single<FoodRepository> { FoodRepositoryImpl(androidContext()) }

    factory { FoodUseCase(get()) }

    viewModel { FoodViewModel(get()) }
}