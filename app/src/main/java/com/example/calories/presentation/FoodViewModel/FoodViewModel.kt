package com.example.calories.presentation.FoodViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calories.domain.FoodUseCase.FoodUseCase
import com.example.calories.domain.model.Food
import kotlinx.coroutines.launch

class FoodViewModel(private val foodUseCase: FoodUseCase): ViewModel() {
    private var _food = MutableLiveData<List<Food>>()
    val food : LiveData<List<Food>> = _food

    fun loadFoods() {
        viewModelScope.launch {
            try {
                val foods = foodUseCase.getAllFoods()
                _food.postValue(foods) // Используем postValue для безопасности потоков
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
