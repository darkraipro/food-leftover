package com.hungames.cookingsocial.util

import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.Order
import com.hungames.cookingsocial.ui.buyprocess.ConfirmDishAdapter
import com.hungames.cookingsocial.ui.detail.DetailAdapter


// when dishes has not been finished loading yet, it is null, thus submit temporarily emptyList
@BindingAdapter("listDish")
fun bindRecyclerView(recyclerView: RecyclerView, dishes: List<Dishes>?){
    val adapter = recyclerView.adapter as DetailAdapter
    if (dishes != null) {
        adapter.submitList(dishes)
    } else {
        adapter.submitList(emptyList())
    }
}

@BindingAdapter("dishQuantity")
fun bindDishQuantity(textview: TextView, dish: Dishes){
    textview.text = convertDishQuantityToTextViewFormat(textview.resources, dish)
}

@BindingAdapter("dishPrice")
fun bindDishPrice(textview: TextView, dish: Dishes){
    textview.text = convertDishPriceToTextViewFormat(textview.resources, dish)
}

@BindingAdapter("spinWhileLoading")
fun bindSpinner(spinner: ProgressBar, dishes: List<Dishes>?){
    if (dishes != null){
        spinner.visibility = ProgressBar.GONE
    } else {
        spinner.visibility = ProgressBar.VISIBLE
    }
}

@BindingAdapter("spinWhileLoadingBool")
fun bindProgressBar(spinner: ProgressBar, show: Boolean){
    if (show) spinner.visibility = ProgressBar.VISIBLE else spinner.visibility = ProgressBar.GONE
}


@BindingAdapter("totalPriceOrder")
fun bindTotalPriceOrder(textview: TextView, totalPrice: Float){
    textview.text = convertFloatToTextViewFormat(textview.resources, totalPrice)
}

@BindingAdapter("listOrder")
fun bindOrderRecyclerView(recyclerView: RecyclerView, order: List<Order>?){
    val adapter = recyclerView.adapter as ConfirmDishAdapter
    if (order != null){
        adapter.submitList(order)
    } else {
        adapter.submitList(emptyList())
    }
}

@BindingAdapter("totalDishPrice")
fun bindTotalDishPrice(textview: TextView, order: Order){
    textview.text = convertOrderPriceToTextViewFormat(textview.resources, order)
}

@BindingAdapter("setOrderQuantity")
fun bindOrderQuantity(textview: TextView, order: Order){
    textview.text = order.quantity.toString()
}