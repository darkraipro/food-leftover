package com.hungames.cookingsocial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hungames.cookingsocial.databinding.ActivityMain2Binding
import com.hungames.cookingsocial.settings.SettingsFragmentDirections
import com.hungames.cookingsocial.ui.login.LoginActivity
import com.hungames.cookingsocial.util.TAG_SETTING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference): Boolean {
        Timber.tag(TAG_SETTING).i(pref.fragment)
        val navController = findNavController(R.id.nav_host_fragment)
        when (pref.fragment){
            "com.hungames.cookingsocial.settings.UserSettingFragment" -> {
                navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationUserSettings())
            }
            else -> {
                Timber.tag(TAG_SETTING).w("Impossible branch entered ${pref.fragment}")
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Singleton
    private val loginStatusViewModel: LoginStatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMain2Binding>(this, R.layout.activity_main2)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_maps, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        loginStatusViewModel.statusFlow.observe(this){
            if (!it){
                loginStatusViewModel.sendEventLogout()
            }
        }

        lifecycleScope.launchWhenStarted {
            loginStatusViewModel.statusEvent.collect {
                when (it){
                    LoginStatusViewModel.StatusEvent.Logout -> {
                        navigateToLoginActivity()
                    }
                    else -> { }
                }
            }
        }


    }

    private fun navigateToLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        setResult(Activity.RESULT_OK)
        finish()
    }
}