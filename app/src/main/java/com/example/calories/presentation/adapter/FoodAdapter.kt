package com.example.calories.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calories.data.FoodDto
import com.example.calories.databinding.ItemFoodBinding

class FoodAdapter(private var foodDto: List<FoodDto>, val click : (FoodDto) -> Unit): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return ViewHolder(binding)

   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val food = foodDto[position]
       holder.Bind(food)
       holder.itemView.setOnClickListener {
           click(food)
       }
   }

   override fun getItemCount(): Int {
       return foodDto.size
   }

   class ViewHolder(private val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root) {
       fun Bind(foodDto: FoodDto) {
           binding.tvName.text = foodDto.name
//            binding.tvFats.text = "Жиры: ${food.fat}"
           binding.tvCalories.text = "Калории: ${foodDto.calories}"
            var decs = foodDto.description
//            binding.tvCarbs.text =  "Углеводы: ${food.carbs}"
//            binding.tvProteins.text =  "Белки: ${food.protein}"



       }
   }
   fun updateData(newFoodDto: List<FoodDto>) {
      foodDto = newFoodDto
       notifyDataSetChanged()
   }
}