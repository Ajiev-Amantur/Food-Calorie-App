package com.example.calories.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.calories.R
import com.example.calories.data.Food
import com.example.calories.data.FoodDataBase
import com.example.calories.data.cal1
import com.example.calories.data.carbs
import com.example.calories.data.fats
import com.example.calories.data.proteins
import com.example.calories.data.repository.FoodRepositoryImpl
import com.example.calories.databinding.ActivityFoodAddBinding
import com.google.android.material.animation.AnimatableView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale.filter

class FoodAddActivity : AppCompatActivity() {

    private lateinit var foodAdapter: FoodAdapter
    private var FoodList: List<Food> = emptyList()
    private lateinit var binding: ActivityFoodAddBinding
    private lateinit var db: FoodDataBase
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
        val mealType = this.intent.getIntExtra("MealType", 0)
        val selectedDate = intent.getLongExtra("Selected_Date", System.currentTimeMillis())
        db = FoodDataBase.getDataBase(this)
        foodAdapter = FoodAdapter(emptyList(), click = {
            val intent = Intent(this@FoodAddActivity, SettingsFoodActivity::class.java)
            val mealType = this.intent.getIntExtra("MealType", 1)
            val selectedDate = this.intent.getLongExtra("Selected_Date", System.currentTimeMillis())
            intent.putExtra("Selected_Date", selectedDate)
            intent.putExtra("food_name", it.name)
            intent.putExtra("kcal", it.calories)
            intent.putExtra("decs",it.description)
            intent.putExtra("carbs",it.carbs)
            intent.putExtra("fat",it.fat)
            intent.putExtra("protein",it.protein)
            startActivity(intent)

        })

        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.RecyclerView.adapter = foodAdapter

        val repository = FoodRepositoryImpl(this)
        lifecycleScope.launch {
            try {
                binding.progressbar.visibility = View.VISIBLE
                binding.RecyclerView.visibility = View.GONE
                val foodLoad = repository.getAllFood()
                FoodList = foodLoad


                foodAdapter.updateData(foodLoad)


                binding.RecyclerView.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.frameSearch.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                val search = FoodList.filter { food ->
                    food.name.contains(p0 ?: "", ignoreCase = true )
                }
               foodAdapter.updateData(search)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
            }
        })

    }
}