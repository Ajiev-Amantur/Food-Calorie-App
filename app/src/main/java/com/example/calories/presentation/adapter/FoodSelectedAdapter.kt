package com.example.calories.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calories.data.Food
import com.example.calories.databinding.ItemFoodSelectedBinding

class FoodSelectedAdapter(
    private var foods: List<Food>,
    private val onDeleteClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodSelectedAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFoodSelectedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.binding.tvFoodName.text = food.name
        holder.binding.tvFoodKcal.text = "${food.calories} ккал"

        holder.binding.ivDelete.setOnClickListener {
            onDeleteClick(food)
        }
    }

    override fun getItemCount(): Int = foods.size

    fun updateData(newFoods: List<Food>) {
        foods = newFoods
        notifyDataSetChanged()
    }
}