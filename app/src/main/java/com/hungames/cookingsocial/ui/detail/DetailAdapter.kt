package com.hungames.cookingsocial.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.databinding.ItemDishBinding

class DetailAdapter: ListAdapter<Dishes, DetailAdapter.DishViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }



    class DishViewHolder(private val binding: ItemDishBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dish: Dishes){
            binding.apply {
                textviewDishName.text = dish.dishName
                textviewDishDesc.text = dish.dishDescription
                textviewQuantity.text = if (dish.quantity != 1) "${dish.quantity.toString()} dishes left" else "${dish.quantity} dish left"
                textviewPrice.text = dish.dishPrice.toString()
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Dishes>(){
        override fun areItemsTheSame(oldItem: Dishes, newItem: Dishes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dishes, newItem: Dishes): Boolean {
            return oldItem == newItem
        }
    }
}