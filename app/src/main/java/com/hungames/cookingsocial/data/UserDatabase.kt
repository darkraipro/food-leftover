package com.hungames.cookingsocial.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.data.model.UserMinimal


@Database(entities = [RegisteredUser::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): RegisteredUserDao
}