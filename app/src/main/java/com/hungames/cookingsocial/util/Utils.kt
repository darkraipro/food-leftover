package com.hungames.cookingsocial.util

import android.content.res.Resources
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.data.model.Dishes

fun convertDishQuantityToTextViewFormat(res: Resources, dish: Dishes): CharSequence{
    val txt = if (dish.quantity != 1) "dishes left" else "dish left"
    return res.getString(R.string.dish_quantity_left, dish.quantity, txt)
}

fun convertDishPriceToTextViewFormat(res: Resources, dish: Dishes): CharSequence{
    return res.getString(R.string.dish_price_euro, dish.dishPrice)
}