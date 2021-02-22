package com.hungames.cookingsocial.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.data.DataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
// TODO: Implement dataStore to match all options available in root_preferences.xml
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var dataStore: DataStore

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}