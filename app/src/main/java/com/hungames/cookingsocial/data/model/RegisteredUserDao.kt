package com.hungames.cookingsocial.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface RegisteredUserDao {

    @Query("SELECT username, email, uid FROM users WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): UserMinimal?

    @Query("SELECT username, email, uid FROM users WHERE uid = :uid")
    fun observeUserById(uid: String): LiveData<UserMinimal>
}