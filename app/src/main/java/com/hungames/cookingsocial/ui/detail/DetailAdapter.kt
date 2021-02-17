package com.hungames.cookingsocial.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.databinding.ItemDishBinding
import com.hungames.cookingsocial.util.TAG_DISH
import timber.log.Timber

class DetailAdapter(private val listener: OnItemClickListener): ListAdapter<Dishes, DetailAdapter.DishViewHolder>(DiffCallback()){

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val currentItem = getItem(position)
        Timber.tag(TAG_DISH).i("CurrentItem onBindViewHolder: $currentItem")
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }





    inner class DishViewHolder(private val binding: ItemDishBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val position = adapterPosition
                        // E.g , while an item is deleted and animated off the list, it has NO_POSITION, otherwise if cliced while animated, it will crash app
                        if (position != RecyclerView.NO_POSITION){
                            val dish = getItem(position)
                            listener.onItemClick(dish)

                        }
                    }
                }
            }
        }
        fun bind(dish: Dishes){
            Timber.tag(TAG_DISH).i("What is the dish to get bind: $dish")
            binding.dish = dish
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener{
        fun onItemClick(dish: Dishes)
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