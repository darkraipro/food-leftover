package com.hungames.cookingsocial.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {

    @Query("SELECT * FROM Dishes WHERE uid = :uid")
    fun getDishes(uid: String): Flow<List<Dishes>>

    // if interested buyer wants to order dishes, try to see if the dishes quantity is still valid
    @Query("SELECT * FROM Dishes WHERE uid = :uid AND id = :id AND quantity >= :quantity")
    suspend fun checkDishQuantity(uid:String, id:Int, quantity: Int): Dishes?

    @Insert
    suspend fun insertDish(dish: Dishes)

    @Insert
    suspend fun insertDishes(dishes: List<Dishes>)

    @Update
    suspend fun updateDishes(dish: Dishes)

}