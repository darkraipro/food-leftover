package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.data.model.UserMinimal
import com.hungames.cookingsocial.util.TAG_LOGIN
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information for database
 * Also registers users and logs them automatically in
 */
class LoginDataSource @Inject constructor(private val userDao: RegisteredUserDao) {

    suspend fun login(username: String, password: String): Result<UserMinimal> {
        return try {

            Timber.tag(TAG_LOGIN).i("LoginDataSource about to get the user")
            val result = userDao.getUser(username, hash(password))
            Timber.tag(TAG_LOGIN)
                .i("Getting result finished. If it is null, one of the things are wrong: $result")
            if (result != null) {
                Result.Success(result)
            } else {
                Result.Error(Exception("Password or username is/are wrong."))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
        // e.g. deleting entry from database, to implement "keep signed in" behavior
        // DB will only have one entry and when there is None -> None is logged in
        // keep only relevant user

    }

    suspend fun register(username: String, password: String): Result<UserMinimal> {
        return try {
            Timber.tag(TAG_LOGIN).i("LoginDataSource attempting to registering user")
            val result = userDao.getUserByMailAddress(username)
            if (result != null) {
                Result.Error(Exception("User already exists."))
            } else {

                userDao.insertUser(RegisteredUser(email = username, password = hash(password)))
                val getUser = userDao.getUser(username, hash(password))
                if (getUser != null) {
                    Result.Success(getUser)
                } else {
                    Result.Error(Exception("DB error. Somehow cant retrieve user."))
                }

            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error registering", e))
        }
    }

    private fun hash(pw: String): String {
        return pw.hashCode().toString()
    }
}