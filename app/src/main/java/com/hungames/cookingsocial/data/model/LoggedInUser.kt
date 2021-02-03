package com.hungames.cookingsocial.data.model

import androidx.room.Entity

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */


data class LoggedInUser(
        val userId: String,
        val displayName: String
)