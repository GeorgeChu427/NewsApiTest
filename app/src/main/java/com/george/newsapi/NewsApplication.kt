package com.george.newsapi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}