package com.hungames.cookingsocial.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class RegisteredUser @JvmOverloads constructor(@ColumnInfo(name = "username")
                                                    var username: String = "",

                                                    @ColumnInfo(name = "password")
                                                    var password: String = "123",

                                                    @ColumnInfo(name = "email")
                                                    var email: String = "",

                                                    @ColumnInfo(name = "validated")
                                                    var validated: Boolean = false,

                                                    @PrimaryKey @ColumnInfo(name = "uid")
                                                    var id: String = UUID.randomUUID().toString()) {
}


data class UserMinimal(@ColumnInfo(name = "username")val username: String,
                       @ColumnInfo(name = "email")val email: String,
                       @ColumnInfo(name = "uid")val id: String)