package com.hungames.cookingsocial.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.data.DataStore
import com.hungames.cookingsocial.util.LoginConstants
import com.hungames.cookingsocial.util.TAG_LOGIN
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UserSettingFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var dataStore: DataStore

    @Inject
    lateinit var logoutPrefDialog: LogoutPreferenceDialog

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        val logoutPreference = preference as? LogoutPreference
        if (logoutPreference != null){
            logoutPrefDialog.logout = {dataStore.putBoolean(LoginConstants.LOGGED_IN, false)
            Timber.tag(TAG_LOGIN).i("User has been logged out. Is fragment added: $isAdded")}
            if (isAdded){
                logoutPrefDialog.show(parentFragmentManager, null)
            }
        }else{
            super.onDisplayPreferenceDialog(preference)
        }
    }

    // TODO: Rename and change types of parameters
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = dataStore
        setPreferencesFromResource(R.xml.user_preferences, rootKey)
    }
}