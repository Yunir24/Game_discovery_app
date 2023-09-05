package com.dauto.gamediscoveryapp

import android.app.Application
import com.dauto.gamediscoveryapp.di.DaggerApplicationComponent
import leakcanary.AppWatcher
import leakcanary.LeakCanary

class GameDiscoveryApp : Application(){

    val component by lazy{
        DaggerApplicationComponent.factory().create(this)
    }

}