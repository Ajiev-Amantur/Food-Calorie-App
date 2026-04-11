package com.example.calories.presentation

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calories.presentation.DetailProgressActivity
import com.example.calories.R
import com.example.calories.data.Food
import com.example.calories.data.FoodDataBase
import com.example.calories.databinding.ActivityMainBinding
import com.example.calories.presentation.adapter.FoodSelectedAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FoodDataBase
    private var selectedDate: Long = MaterialDatePicker.todayInUtcMilliseconds()

    // 4 отдельных адаптера для списков под кнопками
    private lateinit var breakfastAdapter: FoodSelectedAdapter
    private lateinit var lunchAdapter: FoodSelectedAdapter
    private lateinit var dinnerAdapter: FoodSelectedAdapter
    private lateinit var snacksAdapter: FoodSelectedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FoodDataBase.getDataBase(this)
        setupMealRecyclerViews()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(selectedDate)
            .build()

        binding.tvCalendarData.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(selectedDate))

        binding.tvCalendarData.setOnClickListener {
            datePicker.show(supportFragmentManager, "Material_Date_Picker")
        }

        datePicker.addOnPositiveButtonClickListener { selected ->
            selectedDate = selected
            binding.tvCalendarData.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(selectedDate))
            dataUpdates()
        }

        binding.navigationGrafic.setOnClickListener {
            val intent = Intent(this, DetailProgressActivity::class.java)
            intent.putExtra("Selected_Date", selectedDate)
            // Передаем текущий прогресс для BarChart
            val goals = FilterFood()
            intent.putExtra("kcalProgress", (binding.progressCallories.progress))
            startActivity(intent)
        }

        // Клики по кнопкам "Добавить"
        binding.frameAddFood1.setOnClickListener { openAddFood(1) } // Завтрак
        binding.frameAddFood2.setOnClickListener { openAddFood(2) } // Обед
        binding.frameAddFood3.setOnClickListener { openAddFood(3) } // Ужин
        binding.frameAddFood4.setOnClickListener { openAddFood(4) } // Перекус
        
        binding.navigationFood.setOnClickListener { openAddFood(0) }
    }

    private fun setupMealRecyclerViews() {
        // Инициализируем адаптеры с функцией удаления
        breakfastAdapter = FoodSelectedAdapter(emptyList()) { deleteFood(it) }
        binding.rvBreakfast.layoutManager = LinearLayoutManager(this)
        binding.rvBreakfast.adapter = breakfastAdapter

        lunchAdapter = FoodSelectedAdapter(emptyList()) { deleteFood(it) }
        binding.rvLunch.layoutManager = LinearLayoutManager(this)
        binding.rvLunch.adapter = lunchAdapter

        dinnerAdapter = FoodSelectedAdapter(emptyList()) { deleteFood(it) }
        binding.rvDinner.layoutManager = LinearLayoutManager(this)
        binding.rvDinner.adapter = dinnerAdapter

        snacksAdapter = FoodSelectedAdapter(emptyList()) { deleteFood(it) }
        binding.rvSnacks.layoutManager = LinearLayoutManager(this)
        binding.rvSnacks.adapter = snacksAdapter
    }

    private fun deleteFood(food: Food) {
        lifecycleScope.launch {
            db.foodDao().deleteFood(food)
            dataUpdates() // Обновляем всё после удаления
            Toast.makeText(this@MainActivity, "${food.name} удалено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        dataUpdates()
    }

    fun dataUpdates() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endTime = calendar.timeInMillis

        lifecycleScope.launch {
            val dayFood = db.foodDao().getFoodByDate(startTime, endTime)
            
            // Распределяем еду по маленьким спискам
            breakfastAdapter.updateData(dayFood.filter { it.mealType == 1 })
            lunchAdapter.updateData(dayFood.filter { it.mealType == 2 })
            dinnerAdapter.updateData(dayFood.filter { it.mealType == 3 })
            snacksAdapter.updateData(dayFood.filter { it.mealType == 4 })

            val goals = FilterFood()
            val totalKcal = dayFood.sumOf { it.calories }
            val totalCarbs = dayFood.sumOf { it.carbs.toDouble() }
            val totalProtein = dayFood.sumOf { it.protein.toDouble() }
            val totalFat = dayFood.sumOf { it.fat.toDouble() }

            val kcalNormal = goals["kcal"] ?: 2000
            val proteinNormal = (goals["protein"] ?: 100).toFloat()
            val fatNormal = (goals["fat"] ?: 70).toFloat()
            val carbsNormal = (goals["carbs"] ?: 100).toFloat()

            binding.apply {
                tvCalories.text = totalKcal.toString()
                tvCarbsProcess.text = "${totalCarbs.toInt()}/"
                tvProteinProcess.text = "${totalProtein.toInt()}/"
                tvFatsProcess.text = "${totalFat.toInt()}/"

                tvCaloriesNormal.text = kcalNormal.toString()
                tvProteinNormal.text = "${proteinNormal.toInt()} g"
                tvFatNormal.text = "${fatNormal.toInt()} g"
                tvCarbsNormal.text = "${carbsNormal.toInt()} g"

                progressCarbs.progress = if (carbsNormal > 0) (totalCarbs / carbsNormal * 100).toInt() else 0
                prosentCarbs.text = "${progressCarbs.progress}%"

                proteinProgress.progress = if (proteinNormal > 0) (totalProtein / proteinNormal * 100).toInt() else 0
                prosentProtein.text = "${proteinProgress.progress}%"

                fatProgress.progress = if (fatNormal > 0) (totalFat / fatNormal * 100).toInt() else 0
                prosentFat.text = "${fatProgress.progress}%"

                progressCallories.progress = if (kcalNormal > 0) (totalKcal.toFloat() / kcalNormal * 100).toInt() else 0
                tvCaloriesProsent.text = "${progressCallories.progress}%"
            }
        }
    }

    fun FilterFood(): Map<String, Int> {
        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val weight = prefs.getString("weight", "70")?.filter { it.isDigit() }?.toIntOrNull() ?: 70
        val height = prefs.getString("height", "175")?.filter { it.isDigit() }?.toIntOrNull() ?: 175
        val age = prefs.getString("age", "25")?.filter { it.isDigit() }?.toIntOrNull() ?: 25
        val goalMode = prefs.getString("goal", "weightNormal") ?: "weightNormal"
        val moveMode = prefs.getString("movie", "moveNormal") ?: "moveNormal"

        return calculateMifflin(weight, height, age, goalMode, moveMode)
    }

    fun calculateMifflin(weight: Int, height: Int, age: Int, mode: String, movie: String): Map<String, Int> {
        val bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5
        val factor = when (movie) {
            "moveLess" -> 1.2
            "moveMore" -> 1.55
            else -> 1.375
        }
        var finalGoal = bmr * factor
        when (mode) {
            "weightLess" -> finalGoal -= 500
            "weightMore" -> finalGoal += 500
        }

        return mapOf(
            "kcal" to finalGoal.toInt(),
            "protein" to (finalGoal * 0.20 / 4).toInt(),
            "fat" to (finalGoal * 0.30 / 9).toInt(),
            "carbs" to (finalGoal * 0.50 / 4).toInt()
        )
    }

    private fun openAddFood(mealType: Int) {
        val intent = Intent(this, FoodAddActivity::class.java)
        intent.putExtra("Selected_Date", selectedDate)
        intent.putExtra("MealType", mealType)
        startActivity(intent)
    }
}
