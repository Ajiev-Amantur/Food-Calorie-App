package com.example.calories.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calories.databinding.ItemFoodBinding
import com.example.calories.domain.model.Food

class FoodAdapter(private var food: List<Food>, val click : (Food) -> Unit): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return ViewHolder(binding)

   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val food = food[position]
       holder.Bind(food)
       holder.itemView.setOnClickListener {
           click(food)
       }
   }

   override fun getItemCount(): Int {
       return food.size
   }

   class ViewHolder(private val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root) {
       fun Bind(food: Food) {
           binding.tvName.text = food.name
//            binding.tvFats.text = "Жиры: ${food.fat}"
           binding.tvCalories.text = "Калории: ${food.calories}"
            var decs = food.description
//            binding.tvCarbs.text =  "Углеводы: ${food.carbs}"
//            binding.tvProteins.text =  "Белки: ${food.protein}"



       }
   }
   fun updateData(newFood: List<Food>) {
      food = newFood
       notifyDataSetChanged()
   }
}