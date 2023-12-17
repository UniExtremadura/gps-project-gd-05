package com.gd05.brickr

import android.app.Application
import com.gd05.brickr.util.AppContainer

class BrickrApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
