package com.example.calories.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calories.databinding.ItemSetupWeightBinding

class WeightSetupAdapter(private val list: List<String>)
    : RecyclerView.Adapter<WeightSetupAdapter.ViewHolder>(){
    class ViewHolder(val bind: ItemSetupWeightBinding): RecyclerView.ViewHolder(bind.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSetupWeightBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weight = list[position]
        holder.bind.tvWeight.text = weight
    }

    override fun getItemCount(): Int {
       return list.size
    }
}