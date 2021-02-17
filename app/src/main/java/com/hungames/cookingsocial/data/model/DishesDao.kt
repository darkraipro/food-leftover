package com.hungames.cookingsocial.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {

    @Query("SELECT * FROM dish WHERE uid = :uid")
    suspend fun getDishes(uid: String): List<Dishes>?

    @Query("SELECT * FROM dish WHERE uid = :uid")
    fun getDishFlow(uid: String): Flow<List<Dishes>>

    @Query("SELECT * FROM dish")
    fun getAllDishFlow(): Flow<List<Dishes>>

    // if interested buyer wants to order dishes, try to see if the dishes quantity is still valid
    @Query("SELECT * FROM dish WHERE uid = :uid AND id = :id AND quantity >= :quantity")
    fun checkDishQuantity(uid:String, id:Int, quantity: Int): Flow<Dishes?>

    @Insert
    suspend fun insertDish(dish: Dishes)

    @Insert
    suspend fun insertDishes(dishes: List<Dishes>)

    @Update
    suspend fun updateDishes(dish: Dishes)

}