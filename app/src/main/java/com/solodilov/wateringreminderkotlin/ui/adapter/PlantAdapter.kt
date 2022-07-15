package com.solodilov.wateringreminderkotlin.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.ItemPlantBinding
import com.solodilov.wateringreminderkotlin.domain.entity.Plant

class PlantAdapter(private val onClick: (Plant) -> Unit): RecyclerView.Adapter<PlantItemViewHolder>() {

    var plantList: List<Plant> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantItemViewHolder =
        PlantItemViewHolder(ItemPlantBinding
                .inflate(LayoutInflater.from(parent.context), parent, false), onClick)


    override fun onBindViewHolder(holder: PlantItemViewHolder, position: Int) {
        holder.bind(plantList[position])
    }

    override fun getItemCount(): Int =
        plantList.size
}

class PlantItemViewHolder(private val binding: ItemPlantBinding, private val onClick: (Plant) -> Unit)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(plantItem: Plant) {

        if (!plantItem.imageUri.equals("empty", true)){
            binding.imagePlant.setImageURI(Uri.parse(plantItem.imageUri))
        } else {
            binding.imagePlant.setImageResource(R.drawable.ic_plant_in_pot)
        }

        binding.name.text = plantItem.name

        itemView.setOnClickListener { onClick(plantItem) }
    }
}
