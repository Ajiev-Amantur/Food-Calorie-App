package com.example.calories.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calories.data.FoodDataBase
import com.example.calories.databinding.ActivityFoodAddBinding
import com.example.calories.domain.model.Food
import com.example.calories.presentation.FoodViewModel.FoodViewModel
import com.example.calories.presentation.adapter.FoodAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodAddActivity : AppCompatActivity() {

    private val foodViewModel: FoodViewModel by viewModel()
    private lateinit var foodAdapter: FoodAdapter
    private var fullFoodList: List<Food> = emptyList() // Полный список для поиска
    private lateinit var binding: ActivityFoodAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFoodAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mealTypeFromIntent = intent.getIntExtra("MealType", 1)
        val selectedDate = intent.getLongExtra("Selected_Date", System.currentTimeMillis())

        foodAdapter = FoodAdapter(emptyList(), click = { food ->
            val intent = Intent(this@FoodAddActivity, SettingsFoodActivity::class.java)
            intent.putExtra("Selected_Date", selectedDate)
            intent.putExtra("MealType", mealTypeFromIntent)
            intent.putExtra("food_name", food.name)
            intent.putExtra("kcal", food.calories)
            intent.putExtra("decs", food.description)
            intent.putExtra("carbs", food.carbs)
            intent.putExtra("fat", food.fat)
            intent.putExtra("protein", food.protein)
            startActivity(intent)
        })

        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.RecyclerView.adapter = foodAdapter

        // Подписываемся на данные
        foodViewModel.food.observe(this) { foods ->
            if (foods != null) {
                fullFoodList = foods
                foodAdapter.updateData(foods)
                
                binding.RecyclerView.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE
            }
        }

        // ЗАПУСКАЕМ загрузку данных!
        binding.progressbar.visibility = View.VISIBLE
        foodViewModel.loadFoods()

        binding.frameSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                val filteredList = fullFoodList.filter { food ->
                    food.name.contains(query ?: "", ignoreCase = true)
                }
                foodAdapter.updateData(filteredList)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false
        })
    }
}
