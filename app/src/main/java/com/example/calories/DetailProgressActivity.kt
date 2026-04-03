package com.example.calories

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.calories.data.FoodDataBase
import com.example.calories.databinding.ActivityDetailProgressBinding
import com.example.calories.presentation.MainActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailProgressActivity : AppCompatActivity() {
    private lateinit var db: FoodDataBase
    private lateinit var binding: ActivityDetailProgressBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailProgressBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        db = FoodDataBase.getDataBase(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.DetailProgressActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ivNav.setOnClickListener {
            finish()
        }

        val selectedDate = intent.getLongExtra("Selected_Date", System.currentTimeMillis())
        setupCharts(selectedDate)
    }

    private fun setupCharts(date: Long) {
        lifecycleScope.launch {
            // 1. Получаем данные за неделю для BarChart
            val barEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()
            val sdf = SimpleDateFormat("dd.MM", Locale.getDefault())

            for (i in 6 downTo 0) {
                val cal = Calendar.getInstance()
                cal.timeInMillis = date
                cal.add(Calendar.DAY_OF_YEAR, -i)
                
                // Начало дня
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                val start = cal.timeInMillis

                // Конец дня
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                val end = cal.timeInMillis

                val dayFood = db.foodDao().getFoodByDate(start, end)
                val dayCalories = dayFood.sumOf { it.calories }.toFloat()
                
                barEntries.add(BarEntry((6 - i).toFloat(), dayCalories))
                labels.add(sdf.format(Date(start)))
            }

            updateBarChart(barEntries, labels)

            // 2. Получаем данные за текущий день для PieChart и Прогресса
            val calToday = Calendar.getInstance()
            calToday.timeInMillis = date
            calToday.set(Calendar.HOUR_OF_DAY, 0)
            val startT = calToday.timeInMillis
            calToday.set(Calendar.HOUR_OF_DAY, 23)
            val endT = calToday.timeInMillis

            val todayFood = db.foodDao().getFoodByDate(startT, endT)
            val totalK = todayFood.sumOf { it.calories }
            val totalP = todayFood.sumOf { it.protein.toDouble() }.toFloat()
            val totalF = todayFood.sumOf { it.fat.toDouble() }.toFloat()
            val totalC = todayFood.sumOf { it.carbs.toDouble() }.toFloat()

            binding.tvKcal.text = "${totalK} ккал"
            // Допустим норма 2500, если нет других данных
            binding.progressKcal.progress = if (totalK < 2500) (totalK * 100 / 2500) else 100

            updatePieChart(totalP, totalF, totalC)
        }
    }

    private fun updateBarChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Калории за неделю")
        dataSet.color = ContextCompat.getColor(this, R.color.green1)
        dataSet.valueTextSize = 10f
        dataSet.valueTextColor = Color.BLACK

        val barData = BarData(dataSet)
        barData.barWidth = 0.5f

        binding.BarChart.apply {
            data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setScaleEnabled(false)
            
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawAxisLine(true)
                granularity = 1f
            }
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            
            animateY(1000)
            invalidate()
        }
    }

    private fun updatePieChart(p: Float, f: Float, c: Float) {
        val pieEntries = ArrayList<PieEntry>()
        if (p > 0 || f > 0 || c > 0) {
            pieEntries.add(PieEntry(p, "Белки"))
            pieEntries.add(PieEntry(f, "Жиры"))
            pieEntries.add(PieEntry(c, "Углеводы"))
        } else {
            pieEntries.add(PieEntry(1f, "Нет данных"))
        }

        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.green1),
            ContextCompat.getColor(this, R.color.yellow),
            ContextCompat.getColor(this, R.color.blue)
        )
        dataSet.sliceSpace = 3f
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(binding.PieChart))

        binding.PieChart.apply {
            this.data = data
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            animateXY(1000, 1000)
            invalidate()
        }
    }
}
