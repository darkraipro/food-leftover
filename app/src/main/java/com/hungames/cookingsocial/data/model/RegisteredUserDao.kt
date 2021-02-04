package com.hungames.cookingsocial.data.model

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RegisteredUserDao {

    @Query("SELECT username, email, uid, dateCreated, validated FROM users WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): UserMinimal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: RegisteredUser)

    @Update
    suspend fun updateUser(user: RegisteredUser)

    @Delete
    suspend fun deleteUser(user: RegisteredUser)
}