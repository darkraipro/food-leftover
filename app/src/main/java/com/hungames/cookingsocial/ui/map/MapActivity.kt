package com.hungames.cookingsocial.ui.map

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationView
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    lateinit var activityMapBinding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_map)

    }
}