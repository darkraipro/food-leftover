package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    /**
     * do not instantiate navController in onCreate via findNavController. It will crash the app
     * Instead use supportFragmentManager
      */

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        Timber.tag(TAG_DISH).i("Intent that started this DetailActivity: $intent")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_detail) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.detailFragment)).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // up button does the same as pressing back button on Android
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val shouldPop = supportFragmentManager.backStackEntryCount > 0
        if (shouldPop){
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}