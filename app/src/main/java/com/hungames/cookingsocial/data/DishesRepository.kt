package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.DishesDao
import javax.inject.Inject

class DishesRepository @Inject constructor(private val dao: DishesDao) {

    suspend fun insertDish(dish: Dishes){
        dao.insertDish(dish)
    }

    fun getDishes(uid: String){
        dao.getDishes(uid)
    }
}