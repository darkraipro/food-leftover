package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        Timber.tag(TAG_DISH).i("Intent that started this DetailActivity: $intent")
    }

}