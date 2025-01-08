package com.example.finalproject_mealmatchapp

import android.app.Application
import com.example.finalproject_mealmatchapp.Utilities.ImageLoader

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        ImageLoader.init(this)
    }

}