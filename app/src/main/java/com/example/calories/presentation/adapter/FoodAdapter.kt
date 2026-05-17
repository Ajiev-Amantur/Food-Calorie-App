package com.example.calories.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calories.databinding.ItemFoodBinding
import com.example.calories.domain.model.Food

class FoodAdapter(private var food: List<Food>, val click : (Food) -> Unit): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {
   
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = food[position]
       holder.bind(item)
       holder.itemView.setOnClickListener {
           click(item)
       }
   }

   override fun getItemCount(): Int = food.size

   class ViewHolder(private val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root) {
       fun bind(food: Food) {
           binding.tvName.text = food.name
           binding.tvCalories.text = "Калории: ${food.calories}"
       }
   }

   fun updateData(newFood: List<Food>) {
      food = newFood
      notifyDataSetChanged()
   }
}
