package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.data.model.UserMinimal
import com.hungames.cookingsocial.util.TAG_LOGIN
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val userDao: RegisteredUserDao) {

    suspend fun login(username: String, password: String): Result<UserMinimal> {
        try {

            Timber.tag(TAG_LOGIN).i("LoginDataSource about to get the user")
            val passwordHash = password.hashCode().toString()
            val result = userDao.getUser(username, passwordHash)
            Timber.tag(TAG_LOGIN).i("Getting result finished. If it is null, one of the things are wrong: $result")
            return if (result != null) {
                Result.Success(result)
            } else {
                Result.Error(Exception("Password or username is/are wrong."))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication

    }
}