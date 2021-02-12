package com.hungames.cookingsocial.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


data class PreferencesStore(val loginStatus: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_settings")

    val preferenceFlow = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Timber.e("Error reading preferences: $exception")
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
        .map { preferences ->
            val loginStatus = preferences[PreferencesKeys.LOGGED_IN] ?: false

            // data class as a Wrapper if you need more than one value from the preferences
            PreferencesStore(loginStatus)
        }

    suspend fun setLoginStatus(success: Boolean){
        dataStore.edit { preference ->
            preference[PreferencesKeys.LOGGED_IN] = success
        }
    }

    private object PreferencesKeys {
        val LOGGED_IN = preferencesKey<Boolean>("logged_in")
    }
}