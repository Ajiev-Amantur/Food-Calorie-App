package com.example.calories.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calories.R
import com.example.calories.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreference = getSharedPreferences("UserData",MODE_PRIVATE)
        val weight = sharedPreference.getString("weight","")?.filter { it.isDigit() } ?:""
        val height = sharedPreference.getString("height","")?.filter { it.isDigit() }?:""
        binding.etWeight.setText(weight)
        binding.etHeight.setText(height)
        binding.apply {
            btnSave.setOnClickListener {
                val w = etWeight.text.toString()
                val h = etHeight.text.toString()
                if (w.isEmpty() || h.isEmpty()) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Поле пуст, введите только в цифровом виде", Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Сохранено", Toast.LENGTH_SHORT
                    ).show()
                    val weight = w.toString()
                    val height = h.toString()
                    val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                    sharedPreferences.edit().putString("weight", weight).apply()
                    sharedPreferences.edit().putString("height", height).apply()

                    binding.etWeight.setText(weight)
                    binding.etHeight.setText(height)
                }
            }
        }

    }
}