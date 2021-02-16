package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.DishesDao
import com.hungames.cookingsocial.util.TAG_DISH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
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

    fun getDishesFlow(uid: String): Flow<List<Dishes>>{
        Timber.tag(TAG_DISH).i("Returning flow of dishes from Repository")
        val result = dao.getDishFlow(uid)
        Timber.tag(TAG_DISH).i("Fechted result flow: $result")
        return result.conflate()
    }
}