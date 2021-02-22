package com.hungames.cookingsocial.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import androidx.preference.PreferenceDataStore
import com.hungames.cookingsocial.data.di.DatabaseModule.ApplicationScope
import com.hungames.cookingsocial.util.LoginConstants
import com.hungames.cookingsocial.util.TAG_SETTING
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStore @Inject constructor(private val preferenceManager: PreferencesManager, @ApplicationScope val scope: CoroutineScope): PreferenceDataStore(){
    override fun putBoolean(key: String?, value: Boolean) {
        key ?: return
        when (key){
            LoginConstants.LOGGED_IN -> scope.launch{
                preferenceManager.setLoginStatus(false)
            }
        }
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        key ?: return false
        val defaultResult: Boolean = false
        return when (key){
            LoginConstants.LOGGED_IN -> {
                try {
                    val deferred = scope.async {
                        preferenceManager.preferenceFlow.first().loginStatus
                    }
                    runBlocking {
                        deferred.await()
                    }
                    deferred.getCompleted()
                }catch (i: IllegalStateException){
                    Timber.tag(TAG_SETTING).w("Deferred obj has been called even though its not finished yet: $i")
                    defaultResult
                }catch (t: Throwable){
                    Timber.tag(TAG_SETTING).w("An error occurred inside the PreferenceDataStore getBoolean impl: $t")
                    defaultResult
                }
            }
            else -> {
                defaultResult
            }
        }
    }
}


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
        val LOGGED_IN = preferencesKey<Boolean>(LoginConstants.LOGGED_IN)
    }
}