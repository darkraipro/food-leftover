package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.RegisteredUser
import com.hungames.cookingsocial.data.model.UserMinimal
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(){

    fun login(username: String, password: String): Result<UserMinimal> {
        try {
            // TODO: handle loggedInUser authentication
            return Result.Success(UserMinimal(email="viet.hungdinh@yahoo.de", username = "", isValidated = false, date = 203241, uid = "32441-351-uid"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}