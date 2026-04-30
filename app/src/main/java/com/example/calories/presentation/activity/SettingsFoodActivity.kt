package com.example.calories.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.calories.R
import com.example.calories.data.FoodDataBase
import com.example.calories.data.cal1
import com.example.calories.data.carbs
import com.example.calories.data.fats
import com.example.calories.data.foodMapper.toDto
import com.example.calories.data.proteins
import com.example.calories.databinding.ActivitySettingsFoodBinding
import com.example.calories.domain.model.Food
import kotlinx.coroutines.launch

class SettingsFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsFoodBinding
    private lateinit var db: FoodDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsFoodBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = FoodDataBase.Companion.getDataBase(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val foodName = intent.getStringExtra("food_name") ?: ""
        val kcal = intent.getIntExtra("kcal", 0)
        val description = intent.getStringExtra("decs") ?: ""
        val protein = intent.getFloatExtra("protein", 0.0f)
        val fat = intent.getFloatExtra("fat", 0.0f)
        val carbs1 = intent.getFloatExtra("carbs", 0.0f)
        val mealType = intent.getIntExtra("MealType", 1)
        val selectedDate = intent.getLongExtra("Selected_Date", System.currentTimeMillis())

        binding.tvSetKcal.text = "Калории: $kcal"
        binding.tvSettingsFoodName.text = foodName
        binding.tvSetDecription.text = description

        val sum = protein + fat + carbs1
        if (sum > 0) {
            val pV = (protein / sum * 100).toInt()
            val fV = (fat / sum * 100).toInt()
            val cV = (carbs1 / sum * 100).toInt()

            binding.setProgress1.progress = pV
            binding.setProgress2.progress = fV
            binding.setProgress3.progress = cV
            binding.tvSetFoodProsent1.text = "$pV%"
            binding.tvSetFoodProsent2.text = "$fV%"
            binding.tvSetFoodProsent3.text = "$cV%"
        }

        binding.button.setOnClickListener {
            val weightStr = binding.edtSetGram.text.toString()
            val weight = weightStr.toIntOrNull() ?: 100
            val factor = weight.toFloat() / 100f

            val finalKcal = (kcal * factor).toInt()
            val finalProtein = protein * factor
            val finalFat = fat * factor
            val finalCarbs = carbs1 * factor

            lifecycleScope.launch {
                val eatenFoodDto = Food(
                    name = foodName,
                    calories = finalKcal,
                    protein = finalProtein,
                    fat = finalFat,
                    carbs = finalCarbs,
                    description = description,
                    date = selectedDate,
                    mealType = mealType
                )
                db.foodDao().addFood(eatenFoodDto.toDto())

                // Обновляем и глобальные переменные для мгновенного эффекта
                cal1 += finalKcal
                proteins += finalProtein.toInt()
                fats += finalFat.toInt()
                carbs += finalCarbs.toInt()

                finish()
            }
        }
    }
}