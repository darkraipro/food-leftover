package com.hungames.cookingsocial.data.interfaces

import com.hungames.cookingsocial.data.Result
import com.hungames.cookingsocial.data.model.UserNeighbors
import kotlinx.coroutines.flow.Flow

interface NeighborUser {

    fun getNearbyUsers(location: String): Result<Flow<UserNeighbors>>
}