package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.DishesDao
import com.hungames.cookingsocial.util.TAG_DISH
import timber.log.Timber
import javax.inject.Inject

class DishesRepository @Inject constructor(private val dao: DishesDao) {

    suspend fun insertDish(dish: Dishes){
        dao.insertDish(dish)
    }

    suspend fun getDishes(uid: String): List<Dishes>? {
        Timber.tag(TAG_DISH).i("uid: $uid \n Using dao to get dishes")
        return dao.getDishes(uid)
    }
}