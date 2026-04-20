package com.example.calories.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.calories.databinding.ActivityWeightSetupActivtiyBinding
import com.example.calories.presentation.adapter.WeightSetupAdapter
import com.example.calories.presentation.viewModel.UserViewModel

class WeightSetupActivtiy : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private var isSelectingGoal = true // Сначала выбираем цель, потом активность

    private lateinit var adapter: WeightSetupAdapter
    private lateinit var binding: ActivityWeightSetupActivtiyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        if (prefs.getBoolean("isSetupDone",false)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        enableEdgeToEdge()
        binding = ActivityWeightSetupActivtiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.Frame1.visibility = View.GONE
        binding.Frame2.visibility = View.GONE
        binding.Frame3.visibility = View.GONE
        binding.RecyclerViewCon.visibility = View.GONE

        adapter = WeightSetupAdapter(emptyList())
        val weightList = (10..150).map { "$it кг" }
        val ageList = (10..100).map { "$it лет" }
        val HeightList = (100..220).map { "$it см" }

        var currentStep = 1
        updateAdapter(weightList)
        binding.tvSetup.text = "Выберите вес"
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerView.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.RecyclerView)

        binding.Button.setOnClickListener {
            val centerView = snapHelper.findSnapView(layoutManager)
            val pos = layoutManager.getPosition(centerView!!)
            val selected = when (currentStep) {
                1 -> weightList[pos]
                2 -> ageList[pos]
                else -> HeightList[pos]
            }
            when(currentStep) {
                1 -> {
                    saveUserData("weight", selected)
                    Log.d("ololo", "Вес: $selected")

                    currentStep = 2
                    updateAdapter(ageList)

                    binding.tvSetup.text = "Выберите возраст"
                }

                2 -> {
                    saveUserData("age", selected)
                    currentStep = 3
                    Log.d("ololo", "Выбран Возраст: $selected")
                    updateAdapter(HeightList)
                    binding.tvSetup.text = "Выберите рост"
                }

                3 -> {
                    saveUserData("height", selected)
                    Log.d("ololo", "Выбран Рост: $selected")
                    binding.tvSetup.visibility = View.GONE
                    binding.RecyclerView.visibility = View.GONE
                    binding.Button.visibility = View.GONE
                    binding.RecyclerViewCon.visibility = View.VISIBLE
                    binding.Frame1.visibility = View.VISIBLE
                    binding.Frame2.visibility = View.VISIBLE
                    binding.Frame3.visibility = View.VISIBLE


                    updateFrameTexts("Хочу похудеть", "Хочу набрать массу", "Поддержание веса")
                    binding.Frame1.setOnClickListener {
                        handleFrameClick("weightLess", "moveLess") }
                    binding.Frame2.setOnClickListener {
                        handleFrameClick("weightMore", "moveMore") }
                    binding.Frame3.setOnClickListener {
                        handleFrameClick("weightNormal", "moveNormal")}
//                    val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
//                    sharedPreferences.edit().putBoolean("isSetupDone",true).apply()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }

                    }

                }


            }
    private fun handleFrameClick(goalValue: String, activityValue: String) {
        if (isSelectingGoal) {
            saveUserData("goal", goalValue) // Сохраняем цель
            isSelectingGoal = false         // Переключаем режим на выбор активности
            // Скрываем картинки целей
            binding.ivSetLessWeight.visibility = View.GONE
            binding.ivSetMoreWeight.visibility = View.GONE
            binding.ivSetNormalWeight.visibility = View.GONE
            // Меняем тексты на активность
            updateFrameTexts("Сидячий образ", "Легкая активность", "Высокая активность")
        } else {
            setGoalLife(activityValue)     // Финальное сохранение и выход
        }
    }

    // Просто меняет текст в твоих TextView внутри фреймов
    private fun updateFrameTexts(t1: String, t2: String, t3: String) {
        binding.tvSetLessWeight.text = t1
        binding.tvSetMoreWeight.text = t2
        binding.tvSetNormalWeight.text = t3
    }


    fun updateAdapter(list: List<String>){
       binding.RecyclerView.adapter = WeightSetupAdapter(list)
        binding.RecyclerView.scrollToPosition(0)
    }
    fun saveUserData(key: String,value: String){
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        sharedPreferences.edit {
            putString(key, value)
        }
    }
    fun setGoalLife(goal: String){
        saveUserData("goal",goal)
        val sharedPreference = getSharedPreferences("UserData", MODE_PRIVATE)
        sharedPreference.edit().putBoolean("isSetupDone",true).apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}