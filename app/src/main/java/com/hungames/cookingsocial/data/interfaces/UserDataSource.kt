package com.hungames.cookingsocial.data.interfaces

import com.hungames.cookingsocial.data.Result
import com.hungames.cookingsocial.data.model.UserMinimal


interface UserDataSource {

    fun login(email: String, password: String): Result<UserMinimal>
    fun logout()
}