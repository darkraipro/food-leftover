package com.hungames.cookingsocial.ui.buyprocess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hungames.cookingsocial.data.model.Order
import com.hungames.cookingsocial.databinding.ItemOrderBinding

class ConfirmDishAdapter: ListAdapter<Order, ConfirmDishAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(order: Order){
            binding.order = order
            binding.executePendingBindings()
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.dish.id == newItem.dish.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
