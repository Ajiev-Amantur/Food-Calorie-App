package com.example.calories.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.calories.databinding.ActivityWeightSetupActivtiyBinding
import com.example.calories.presentation.adapter.WeightSetupAdapter
import com.example.calories.presentation.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeightSetupActivtiy : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()
    private var isSelectingGoal = true

    private lateinit var binding: ActivityWeightSetupActivtiyBinding
    private val snapHelper = LinearSnapHelper()

    // Списки данных
    private val weightList = (10..150).map { "$it кг" }
    private val ageList = (10..100).map { "$it лет" }
    private val heightList = (100..220).map { "$it см" }
    
    private var currentList = weightList
    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("UserData", MODE_PRIVATE)
        if (prefs.getBoolean("IsSetupComplete", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityWeightSetupActivtiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Прячем карточки выбора цели в самом начале
        binding.RecyclerViewCon.visibility = View.GONE
        binding.Frame1.visibility = View.GONE
        binding.Frame2.visibility = View.GONE
        binding.Frame3.visibility = View.GONE

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация RecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerView.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(binding.RecyclerView)

        // Начальный этап - Вес
        updateStep(1)

        binding.Button.setOnClickListener {
            val centerView = snapHelper.findSnapView(layoutManager)
            val pos = centerView?.let { layoutManager.getPosition(it) } ?: 0
            val selected = currentList[pos]

            when (currentStep) {
                1 -> {
                    userViewModel.saveParams("weight", selected)
                    updateStep(2)
                }
                2 -> {
                    userViewModel.saveParams("age", selected)
                    updateStep(3)
                }
                3 -> {
                    userViewModel.saveParams("height", selected)
                    showGoalSelection()
                }
            }
        }
    }

    private fun updateStep(step: Int) {
        currentStep = step
        when (step) {
            1 -> {
                currentList = weightList
                binding.tvSetup.text = "Выберите вес"
            }
            2 -> {
                currentList = ageList
                binding.tvSetup.text = "Выберите возраст"
            }
            3 -> {
                currentList = heightList
                binding.tvSetup.text = "Выберите рост"
            }
        }
        
        binding.RecyclerView.adapter = WeightSetupAdapter(currentList)
        // Скроллим к середине списка для удобства
        binding.RecyclerView.scrollToPosition(currentList.size / 2)
    }

    private fun showGoalSelection() {
        binding.tvSetup.visibility = View.GONE
        binding.RecyclerView.visibility = View.GONE
        binding.Button.visibility = View.GONE
        
        binding.RecyclerViewCon.visibility = View.VISIBLE
        binding.Frame1.visibility = View.VISIBLE
        binding.Frame2.visibility = View.VISIBLE
        binding.Frame3.visibility = View.VISIBLE

        updateFrameTexts("Хочу похудеть", "Хочу набрать массу", "Поддержание веса")
        
        binding.Frame1.setOnClickListener { handleFrameClick("weightLess", "moveLess") }
        binding.Frame2.setOnClickListener { handleFrameClick("weightMore", "moveMore") }
        binding.Frame3.setOnClickListener { handleFrameClick("weightNormal", "moveNormal") }
    }

    private fun handleFrameClick(goalValue: String, activityValue: String) {
        if (isSelectingGoal) {
            userViewModel.saveParams("goal", goalValue)
            isSelectingGoal = false
            binding.ivSetLessWeight.visibility = View.GONE
            binding.ivSetMoreWeight.visibility = View.GONE
            binding.ivSetNormalWeight.visibility = View.GONE
            updateFrameTexts("Сидячий образ", "Легкая активность", "Высокая активность")
        } else {
            userViewModel.saveParams("activity", activityValue)
            userViewModel.isCompleted()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateFrameTexts(t1: String, t2: String, t3: String) {
        binding.tvSetLessWeight.text = t1
        binding.tvSetMoreWeight.text = t2
        binding.tvSetNormalWeight.text = t3
    }
}
