package com.hungames.cookingsocial.settings

import androidx.preference.PreferenceDialogFragmentCompat
import javax.inject.Inject

class LogoutPreferenceDialog @Inject constructor(): PreferenceDialogFragmentCompat() {

    lateinit var logout: () -> Unit

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult){
            logout()
        }
    }
}