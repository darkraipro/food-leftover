package com.hungames.cookingsocial.data

import com.hungames.cookingsocial.data.model.RegisteredUserDao
import com.hungames.cookingsocial.data.model.UserMinimal
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.util.TAG_MAP
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// in real situations, having an api service to access data in case it is not locally available in cache

@Singleton
class NeighborUserRepository @Inject constructor(private val dao: RegisteredUserDao) {

    suspend fun getNearbyUsers(location: String): Result<List<UserNeighbors>> {
        return try {
            Timber.tag(TAG_MAP).i("Accessing user database to search for username living in the same location")
            val res = dao.getNearbyUsers(location)
            Timber.tag(TAG_MAP).i("Received result from database for nearby users.")
            Result.Success(res)
        } catch (t: Throwable){
            Timber.tag(TAG_MAP).w("DB threw an error when trying to get nearby users.")
            Result.Error(IOException("No users for given location found."))
        }
    }
}