package com.george.newsapi.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.george.newsapi.R
import com.george.newsapi.ui.fragment.config.ConfigFragment
import com.george.newsapi.ui.fragment.headlines.HeadlinesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, HeadlinesFragment.newInstance())
            .replace(R.id.fragment_container, ConfigFragment.newInstance())
            .commit()
    }

}

