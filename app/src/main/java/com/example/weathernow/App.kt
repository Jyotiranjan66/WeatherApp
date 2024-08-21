package com.example.weathernow

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        initTasks()
    }

    private fun initTasks() {
        // For Firebase Initialization
    }
}