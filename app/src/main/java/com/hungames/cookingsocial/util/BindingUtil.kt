package com.hungames.cookingsocial.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.ui.detail.DetailAdapter


@BindingAdapter("listDish")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Dishes>?){
    val adapter = recyclerView.adapter as DetailAdapter
    adapter.submitList(data)
}

@BindingAdapter("dishQuantity")
fun bindDishQuantity(textview: TextView, dish: Dishes){
    textview.text = convertDishQuantityToTextViewFormat(textview.resources, dish)
}

@BindingAdapter("dishPrice")
fun bindDishPrice(textview: TextView, dish: Dishes){
    textview.text = convertDishPriceToTextViewFormat(textview.resources, dish)
}