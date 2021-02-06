package com.hungames.cookingsocial.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.hungames.cookingsocial.R


// TODO: Implement Settings. Do not reference it via navigation
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}