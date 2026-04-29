package com.example.calories.presentation.FoodViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calories.data.FoodDto
import com.example.calories.domain.FoodUseCase.FoodUseCase
import kotlinx.coroutines.launch

class FoodViewModel(private val foodUseCase: FoodUseCase): ViewModel() {
    private var _foodDto = MutableLiveData<List<FoodDto>>()
    val foodDto : LiveData<List<FoodDto>> = _foodDto

    fun loadFoods() {
        viewModelScope.launch {
            try {
                val foods = foodUseCase.getAllFoods()
                _foodDto.value = foods
            }catch (e: Exception){
                e.printStackTrace()
            }
            }
        }
}