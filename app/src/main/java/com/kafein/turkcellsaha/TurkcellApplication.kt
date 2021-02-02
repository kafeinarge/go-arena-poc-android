package com.kafein.turkcellsaha

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TurkcellApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: TurkcellApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}