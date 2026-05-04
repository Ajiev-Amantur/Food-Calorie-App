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
    private var Listfood: List<Food> = emptyList()
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
        db = FoodDataBase.Companion.getDataBase(this)
        foodAdapter = FoodAdapter(emptyList(), click = {
            val intent = Intent(this@FoodAddActivity, SettingsFoodActivity::class.java)
            val mealType = this.intent.getIntExtra("MealType", 1)
            val selectedDate = this.intent.getLongExtra("Selected_Date", System.currentTimeMillis())
            intent.putExtra("Selected_Date", selectedDate)
            intent.putExtra("food_name", it.name)
            intent.putExtra("kcal", it.calories)
            intent.putExtra("decs", it.description)
            intent.putExtra("carbs", it.carbs)
            intent.putExtra("fat", it.fat)
            intent.putExtra("protein", it.protein)
            startActivity(intent)

        })

        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.RecyclerView.adapter = foodAdapter

        foodViewModel.food.observe(this){food->
            Listfood = food
            foodAdapter.updateData(food)
        }
        
        foodViewModel.loadFoods()

        binding.RecyclerView.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE

        binding.frameSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                val search = Listfood.filter { food ->
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