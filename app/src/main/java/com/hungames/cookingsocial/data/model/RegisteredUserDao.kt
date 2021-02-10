package com.hungames.cookingsocial.data.model

import androidx.room.*


@Dao
interface RegisteredUserDao {

    @Query("SELECT username, email, uid, dateCreated, validated FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserMinimal?

    @Query("SELECT username, email, uid, dateCreated, validated FROM users WHERE email = :email")
    suspend fun getUserByMailAddress(email: String): UserMinimal?

    @Query("SELECT username, email, validated, street, postal, city, country, uid, userDescription FROM users WHERE city = :location")
    suspend fun getNearbyUsers(location: String): List<UserNeighbors>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: RegisteredUser)

    @Update
    suspend fun updateUser(user: RegisteredUser)

    @Delete
    suspend fun deleteUser(user: RegisteredUser)
}